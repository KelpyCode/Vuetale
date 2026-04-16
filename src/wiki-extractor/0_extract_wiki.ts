import { DOMParser, Element } from "jsr:@b-fuze/deno-dom";

const DEFAULT_INDEX_URL =
  "https://hytalemodding.dev/en/docs/official-documentation/custom-ui/type-documentation/";

const PRIMITIVE_TYPES = new Set([
  "string",
  "boolean",
  "integer",
  "float",
  "number",
  "color",
  "vector2",
  "vector3",
  "ui",
  "path",
]);

const GENERIC_CONTAINER_TYPES = new Set([
  "list",
  "array",
  "dictionary",
  "hashset",
  "set",
  "map",
]);

export type ElementProperty = {
  name: string;
  type: string;
  description: string;
};

export type EventCallback = {
  name: string;
  description: string;
};

export type ParsedElement = {
  name: string;
  acceptsChildElements: boolean;
  properties: ElementProperty[];
  callbacks: EventCallback[];
};

export type ParsedObjectType = {
  kind: "object";
  name: string;
  fields: Record<string, string>;
  descriptions: Record<string, string>;
};

export type ParsedEnumType = {
  kind: "enum";
  name: string;
  values: string[];
  descriptions: Record<string, string>;
};

export type ParsedType = ParsedObjectType | ParsedEnumType;

export type ExtractionResult = {
  types: Record<string, Record<string, string> | string[]>;
  elements: Record<string, { properties: ElementProperty[]; acceptsChildElements: boolean }>;
  eventCallbacks: Record<string, EventCallback[]>;
  metadata: {
    generatedAt: string;
    sourceIndexUrl: string;
    counts: {
      elements: number;
      types: number;
      callbacks: number;
      unresolvedTypeReferences: number;
      failedPages: number;
    };
    unresolvedTypeReferences: string[];
    failedPages: string[];
  };
};

type ExtractedLinks = {
  elements: string[];
  propertyTypes: string[];
  enums: string[];
};

type FetchFailure = {
  url: string;
  reason: string;
};

function normalizeWhitespace(input: string): string {
  return input.replace(/\s+/g, " ").trim();
}

function cleanTypeText(input: string): string {
  return normalizeWhitespace(input).replace(/\s*\/\s*/g, " / ");
}

function isPrimitiveType(input: string): boolean {
  return PRIMITIVE_TYPES.has(input.toLowerCase());
}

function extractTableRows(table: Element): string[][] {
  const rows = [...table.querySelectorAll("tbody tr")];
  return rows.map((tr) => {
    const cells = [...tr.querySelectorAll("td")];
    return cells.map((td) => normalizeWhitespace(td.textContent ?? ""));
  });
}

function findHeadingByText(doc: Element, headingText: string): Element | null {
  const headings = [...doc.querySelectorAll("h2,h3")];
  const target = headingText.toLowerCase();
  for (const heading of headings) {
    const text = normalizeWhitespace(heading.textContent ?? "").toLowerCase();
    if (text === target) {
      return heading;
    }
  }
  return null;
}

function findNextTableAfterHeading(heading: Element | null): Element | null {
  if (!heading) {
    return null;
  }

  let cursor: Element | null = heading;
  while (cursor) {
    cursor = cursor.nextElementSibling;
    if (!cursor) {
      return null;
    }
    if (cursor.tagName.toLowerCase() === "table") {
      return cursor;
    }
    const nested = cursor.querySelector("table");
    if (nested) {
      return nested;
    }
  }

  return null;
}

function parseNameFromTitle(doc: Element): string {
  const h1 = doc.querySelector("h1");
  return normalizeWhitespace(h1?.textContent ?? "");
}

function parseAcceptsChildElements(root: Element): boolean {
  const paragraphs = [...root.querySelectorAll("p")];
  for (const p of paragraphs) {
    const strong = p.querySelector("strong");
    if (
      strong &&
      normalizeWhitespace(strong.textContent ?? "").toLowerCase().includes(
        "accepts child elements",
      )
    ) {
      return normalizeWhitespace(p.textContent ?? "").toLowerCase().includes(
        "yes",
      );
    }
  }
  return false;
}

export function parseElementPage(html: string): ParsedElement {
  const doc = new DOMParser().parseFromString(html, "text/html");
  if (!doc) {
    throw new Error("Could not parse element page HTML");
  }

  const name = parseNameFromTitle(doc as unknown as Element);
  const acceptsChildElements = parseAcceptsChildElements(doc as unknown as Element);
  const propertiesHeading = findHeadingByText(doc as unknown as Element, "Properties");
  const propertiesTable = findNextTableAfterHeading(propertiesHeading);
  const callbacksHeading = findHeadingByText(
    doc as unknown as Element,
    "Event Callbacks",
  );
  const callbacksTable = findNextTableAfterHeading(callbacksHeading);

  const properties = propertiesTable
    ? extractTableRows(propertiesTable)
      .map((row) => ({
        name: row[0] ?? "",
        type: cleanTypeText(row[1] ?? ""),
        description: row[2] ?? "",
      }))
      .filter((row) => row.name.length > 0)
    : [];

  const callbacks = callbacksTable
    ? extractTableRows(callbacksTable)
      .map((row) => ({
        name: row[0] ?? "",
        description: row[1] ?? "",
      }))
      .filter((row) => row.name.length > 0)
    : [];

  return { name, acceptsChildElements, properties, callbacks };
}

export function parseTypePage(html: string): ParsedType {
  const doc = new DOMParser().parseFromString(html, "text/html");
  if (!doc) {
    throw new Error("Could not parse type page HTML");
  }

  const root = doc as unknown as Element;
  const name = parseNameFromTitle(root);
  const firstTable = root.querySelector("table");

  if (!firstTable) {
    throw new Error(`Type page ${name} does not include a table`);
  }

  const headers = [...firstTable.querySelectorAll("thead th")].map((th) =>
    normalizeWhitespace(th.textContent ?? "").toLowerCase()
  );
  const rows = extractTableRows(firstTable);

  const hasTypeColumn = headers.includes("type");

  if (hasTypeColumn) {
    const fields: Record<string, string> = {};
    const descriptions: Record<string, string> = {};
    for (const row of rows) {
      const fieldName = row[0] ?? "";
      if (!fieldName) {
        continue;
      }
      fields[fieldName] = cleanTypeText(row[1] ?? "");
      descriptions[fieldName] = row[2] ?? "";
    }

    return {
      kind: "object",
      name,
      fields,
      descriptions,
    };
  }

  const values: string[] = [];
  const descriptions: Record<string, string> = {};
  for (const row of rows) {
    const valueName = row[0] ?? "";
    if (!valueName) {
      continue;
    }
    values.push(valueName);
    descriptions[valueName] = row[1] ?? "";
  }

  return {
    kind: "enum",
    name,
    values,
    descriptions,
  };
}

async function sleep(ms: number): Promise<void> {
  await new Promise((resolve) => setTimeout(resolve, ms));
}

async function fetchWithRetry(
  url: string,
  options: { maxRetries?: number; delayMs?: number; timeoutMs?: number } = {},
): Promise<string> {
  const maxRetries = options.maxRetries ?? 3;
  const delayMs = options.delayMs ?? 200;
  const timeoutMs = options.timeoutMs ?? 20_000;

  let lastError: Error | null = null;
  for (let attempt = 0; attempt < maxRetries; attempt++) {
    try {
      if (attempt > 0) {
        await sleep(delayMs * (attempt + 1));
      }

      const controller = new AbortController();
      const timeout = setTimeout(() => controller.abort(), timeoutMs);

      const response = await fetch(url, {
        signal: controller.signal,
        headers: {
          "User-Agent": "Vuetale-WikiExtractor/1.0",
        },
      });

      clearTimeout(timeout);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      await sleep(delayMs);
      return await response.text();
    } catch (error) {
      lastError = error instanceof Error ? error : new Error(String(error));
    }
  }

  throw lastError ?? new Error(`Failed to fetch ${url}`);
}

function toAbsoluteUrl(baseUrl: string, maybeRelativeUrl: string): string {
  return new URL(maybeRelativeUrl, baseUrl).toString();
}

function normalizeTypeDocumentationUrl(url: string): string {
  const normalized = new URL(url);
  normalized.hash = "";
  const path = normalized.pathname.replace(/\/+/g, "/");
  normalized.pathname = path
    .replace("/type-documentation/type-documentation/", "/type-documentation/")
    .replace("/lements/", "/elements/");
  return normalized.toString();
}

function extractLinksBySegment(root: Element, segment: string): string[] {
  const links = [...root.querySelectorAll(`a[href*="${segment}"]`)];
  const out = new Set<string>();

  for (const link of links) {
    const href = link.getAttribute("href");
    if (!href) {
      continue;
    }
    const absolute = toAbsoluteUrl(DEFAULT_INDEX_URL, href);
    const normalized = normalizeTypeDocumentationUrl(absolute);
    if (new URL(normalized).pathname.includes(segment)) {
      out.add(normalized);
    }
  }

  return [...out].sort((a, b) => a.localeCompare(b));
}

export function extractTypeCandidates(typeText: string): string[] {
  const clean = cleanTypeText(typeText);
  const pieces = clean.split(/\s*\|\s*|\s*\/\s*|\s*,\s*/g);
  const found = new Set<string>();

  for (const piece of pieces) {
    const matches = piece.match(/[A-Za-z][A-Za-z0-9`_]*/g) ?? [];
    for (const token of matches) {
      const lower = token.toLowerCase();
      if (GENERIC_CONTAINER_TYPES.has(lower) || isPrimitiveType(token)) {
        continue;
      }
      if (/`\d+$/.test(token)) {
        continue;
      }
      // Prefer type-like tokens that start uppercase.
      if (/^[A-Z]/.test(token)) {
        found.add(token);
      }
    }
  }

  return [...found].sort((a, b) => a.localeCompare(b));
}

export async function discoverLinks(indexUrl = DEFAULT_INDEX_URL): Promise<ExtractedLinks> {
  const html = await fetchWithRetry(indexUrl);
  const doc = new DOMParser().parseFromString(html, "text/html");
  if (!doc) {
    throw new Error("Could not parse type documentation index page");
  }

  const root = doc as unknown as Element;
  return {
    elements: extractLinksBySegment(root, "/elements/"),
    propertyTypes: extractLinksBySegment(root, "/property-types/"),
    enums: extractLinksBySegment(root, "/enums/"),
  };
}

export async function runExtraction(indexUrl = DEFAULT_INDEX_URL): Promise<ExtractionResult> {
  const links = await discoverLinks(indexUrl);

  const elementPages = new Map<string, ParsedElement>();
  const typePages = new Map<string, ParsedType>();
  const failures: FetchFailure[] = [];

  const allTypeUrls = [...links.propertyTypes, ...links.enums];
  const unresolved = new Set<string>();

  for (const url of links.elements) {
    try {
      const html = await fetchWithRetry(url);
      const parsed = parseElementPage(html);
      if (parsed.name) {
        elementPages.set(parsed.name, parsed);
      }
    } catch (error) {
      failures.push({
        url,
        reason: error instanceof Error ? error.message : String(error),
      });
    }
  }

  for (const url of allTypeUrls) {
    try {
      const html = await fetchWithRetry(url);
      const parsed = parseTypePage(html);
      if (parsed.name) {
        typePages.set(parsed.name, parsed);
      }
    } catch (error) {
      failures.push({
        url,
        reason: error instanceof Error ? error.message : String(error),
      });
    }
  }

  const types: Record<string, Record<string, string> | string[]> = {};
  const elements: Record<string, { properties: ElementProperty[]; acceptsChildElements: boolean }> = {};
  const eventCallbacks: Record<string, EventCallback[]> = {};

  for (const [name, parsedType] of [...typePages.entries()].sort((a, b) =>
    a[0].localeCompare(b[0])
  )) {
    if (parsedType.kind === "enum") {
      types[name] = [...parsedType.values].sort((a, b) => a.localeCompare(b));
    } else {
      types[name] = Object.fromEntries(
        Object.entries(parsedType.fields).sort((a, b) => a[0].localeCompare(b[0])),
      );
    }
  }

  const knownTypes = new Set(Object.keys(types));

  let callbackCount = 0;
  for (const [elementName, parsedElement] of [...elementPages.entries()].sort((a, b) =>
    a[0].localeCompare(b[0])
  )) {
    elements[elementName] = {
      acceptsChildElements: parsedElement.acceptsChildElements,
      properties: parsedElement.properties
        .map((property) => ({
          name: property.name,
          type: property.type,
          description: property.description,
        }))
        .sort((a, b) => a.name.localeCompare(b.name)),
    };

    eventCallbacks[elementName] = parsedElement.callbacks.sort((a, b) =>
      a.name.localeCompare(b.name)
    );
    callbackCount += eventCallbacks[elementName].length;

    for (const property of parsedElement.properties) {
      const candidates = extractTypeCandidates(property.type);
      for (const candidate of candidates) {
        if (!knownTypes.has(candidate)) {
          unresolved.add(candidate);
        }
      }
    }
  }

  for (const parsedType of typePages.values()) {
    if (parsedType.kind !== "object") {
      continue;
    }
    for (const fieldType of Object.values(parsedType.fields)) {
      const candidates = extractTypeCandidates(fieldType);
      for (const candidate of candidates) {
        if (!knownTypes.has(candidate)) {
          unresolved.add(candidate);
        }
      }
    }
  }

  const failedPages = failures.map((failure) => `${failure.url} (${failure.reason})`);

  return {
    types,
    elements,
    eventCallbacks,
    metadata: {
      generatedAt: new Date().toISOString(),
      sourceIndexUrl: indexUrl,
      counts: {
        elements: Object.keys(elements).length,
        types: Object.keys(types).length,
        callbacks: callbackCount,
        unresolvedTypeReferences: unresolved.size,
        failedPages: failures.length,
      },
      unresolvedTypeReferences: [...unresolved].sort((a, b) => a.localeCompare(b)),
      failedPages,
    },
  };
}

export async function writeExtractionToFile(
  outputPath = "./hytale-ui-types.json",
  indexUrl = DEFAULT_INDEX_URL,
): Promise<ExtractionResult> {
  const result = await runExtraction(indexUrl);
  await Deno.writeTextFile(outputPath, `${JSON.stringify(result, null, 2)}\n`);
  return result;
}

function parseCliArgs(args: string[]): { output: string; indexUrl: string } {
  let output = "./hytale-ui-types.json";
  let indexUrl = DEFAULT_INDEX_URL;

  for (let i = 0; i < args.length; i++) {
    const arg = args[i];
    if (arg === "--output" || arg === "-o") {
      output = args[i + 1] ?? output;
      i++;
      continue;
    }
    if (arg === "--index-url") {
      indexUrl = args[i + 1] ?? indexUrl;
      i++;
    }
  }

  return { output, indexUrl };
}

if (import.meta.main) {
  const { output, indexUrl } = parseCliArgs(Deno.args);
  const result = await writeExtractionToFile(output, indexUrl);
  console.log(
    [
      `Generated ${output}`,
      `elements=${result.metadata.counts.elements}`,
      `types=${result.metadata.counts.types}`,
      `callbacks=${result.metadata.counts.callbacks}`,
      `unresolved=${result.metadata.counts.unresolvedTypeReferences}`,
      `failedPages=${result.metadata.counts.failedPages}`,
    ].join(" | "),
  );
}
