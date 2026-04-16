import { parseTopLevelDefinitions } from "./1_extract_ui.ts";

type UiDefinitionFile = {
  path: string;
  aliases: Record<string, string>;
  defs: Record<string, string>;
};

type ValueNode =
  | { kind: "string"; value: string }
  | { kind: "number"; value: number }
  | { kind: "boolean"; value: boolean }
  | { kind: "identifier"; value: string }
  | { kind: "color"; value: string }
  | { kind: "variable"; name: string }
  | { kind: "propRef"; name: string }
  | { kind: "refMember"; ref: string; name: string }
  | { kind: "type"; typeName: string | null; items: TypeItem[] }
  | { kind: "array"; items: ValueNode[] }
  | { kind: "math"; op: "+" | "-" | "*" | "/"; left: ValueNode; right: ValueNode }
  | { kind: "unary"; op: "-"; value: ValueNode }
  | { kind: "translation"; value: string };

type TypeItem =
  | { kind: "field"; name: string; value: ValueNode }
  | { kind: "spread"; value: ValueNode };

type ElementChildNode =
  | { kind: "localVar"; name: string; value: ValueNode }
  | { kind: "field"; name: string; value: ValueNode }
  | { kind: "selector"; selector: string; fields: Array<{ name: string; value: ValueNode }> }
  | { kind: "element"; element: ElementNode };

type ElementNode = {
  type: string;
  alias: string | null;
  id: string | null;
  children: ElementChildNode[];
};

type EvalContext = {
  localVars: Map<string, ValueNode>;
  filePath: string;
  missingVars: Set<string>;
};

type RenderNode = {
  type: string;
  id: string | null;
  props: Record<string, unknown>;
  children: RenderNode[];
};

const SLOT_ID_MAP: Record<string, string> = {
  Title: "title",
  Content: "content",
  CloseButton: "closeButton",
};

type PropDefinition = {
  varName: string;
  propName: string;
  tsType: string;
  defaultExpr: string;
};

class ValueParser {
  private i = 0;

  constructor(private readonly src: string) { }

  parseValue(): ValueNode {
    this.skipSpace();
    return this.parseMath();
  }

  private parseMath(): ValueNode {
    let left = this.parseUnary();
    this.skipSpace();

    while (true) {
      const op = this.peek();
      if (op !== "+" && op !== "-" && op !== "*" && op !== "/") {
        break;
      }
      this.i += 1;
      const right = this.parseUnary();
      left = { kind: "math", op, left, right };
      this.skipSpace();
    }

    return left;
  }

  private parseUnary(): ValueNode {
    this.skipSpace();
    if (this.peek() === "-") {
      this.i += 1;
      return { kind: "unary", op: "-", value: this.parseUnary() };
    }
    return this.parsePrimary();
  }

  private parsePrimary(): ValueNode {
    this.skipSpace();
    const ch = this.peek();

    if (ch === "\"") {
      return { kind: "string", value: this.parseString() };
    }

    if (ch === "#") {
      return { kind: "color", value: this.parseColor() };
    }

    if (ch === "@") {
      this.i += 1;
      return { kind: "variable", name: this.parseIdent() };
    }

    if (ch === "%") {
      this.i += 1;
      let out = "%";
      while (!this.eof() && /[A-Za-z0-9_.]/.test(this.peek())) {
        out += this.peek();
        this.i += 1;
      }
      return { kind: "translation", value: out };
    }

    if (ch === "$") {
      this.i += 1;
      const ref = this.parseIdent();
      this.expect(".");
      this.expect("@");
      const name = this.parseIdent();
      return { kind: "refMember", ref, name };
    }

    if (ch === "(") {
      return this.parseType(null);
    }

    if (ch === "[") {
      return this.parseArray();
    }

    if (this.isDigit(ch)) {
      return { kind: "number", value: this.parseNumber() };
    }

    const ident = this.parseIdent();
    if (ident === "true" || ident === "false") {
      return { kind: "boolean", value: ident === "true" };
    }

    this.skipSpace();
    if (this.peek() === "(") {
      return this.parseType(ident);
    }

    return { kind: "identifier", value: ident };
  }

  private parseType(typeName: string | null): ValueNode {
    this.expect("(");
    this.skipSpace();

    const items: TypeItem[] = [];
    while (!this.eof() && this.peek() !== ")") {
      if (this.src.slice(this.i, this.i + 3) === "...") {
        this.i += 3;
        const value = this.parseValue();
        items.push({ kind: "spread", value });
      } else {
        const start = this.i;
        const name = this.parseIdent();
        this.skipSpace();
        if (this.peek() !== ":") {
          this.i = start;
          const value = this.parseValue();
          items.push({ kind: "field", name: "value", value });
        } else {
          this.expect(":");
          const value = this.parseValue();
          items.push({ kind: "field", name, value });
        }
      }

      this.skipSpace();
      if (this.peek() === ",") {
        this.i += 1;
        this.skipSpace();
      }
    }

    this.expect(")");
    return { kind: "type", typeName, items };
  }

  private parseArray(): ValueNode {
    this.expect("[");
    this.skipSpace();
    const items: ValueNode[] = [];

    while (!this.eof() && this.peek() !== "]") {
      items.push(this.parseValue());
      this.skipSpace();
      if (this.peek() === ",") {
        this.i += 1;
        this.skipSpace();
      }
    }

    this.expect("]");
    return { kind: "array", items };
  }

  private parseColor(): string {
    const slice = this.src.slice(this.i);
    const match = slice.match(/^#[A-Za-z0-9]+(?:\([0-9.]+\))?/);
    if (!match) {
      throw new Error(`Invalid color near: ${slice.slice(0, 20)}`);
    }
    this.i += match[0].length;
    return match[0];
  }

  private parseString(): string {
    this.expect("\"");
    let out = "";
    let escaped = false;
    while (!this.eof()) {
      const ch = this.peek();
      this.i += 1;
      if (escaped) {
        out += ch;
        escaped = false;
        continue;
      }
      if (ch === "\\") {
        escaped = true;
        continue;
      }
      if (ch === "\"") {
        break;
      }
      out += ch;
    }
    return out;
  }

  private parseNumber(): number {
    const start = this.i;
    while (!this.eof() && /[0-9.]/.test(this.peek())) {
      this.i += 1;
    }
    return Number(this.src.slice(start, this.i));
  }

  private parseIdent(): string {
    this.skipSpace();
    const start = this.i;
    if (!/[A-Za-z_]/.test(this.peek())) {
      throw new Error(`Expected identifier near: ${this.src.slice(this.i, this.i + 20)}`);
    }
    this.i += 1;
    while (!this.eof() && /[A-Za-z0-9_]/.test(this.peek())) {
      this.i += 1;
    }
    return this.src.slice(start, this.i);
  }

  private skipSpace(): void {
    while (!this.eof()) {
      const ch = this.peek();
      const next = this.src[this.i + 1] ?? "";
      if (/\s/.test(ch)) {
        this.i += 1;
        continue;
      }
      if (ch === "/" && next === "/") {
        this.i += 2;
        while (!this.eof() && this.peek() !== "\n") {
          this.i += 1;
        }
        continue;
      }
      if (ch === "/" && next === "*") {
        this.i += 2;
        while (!this.eof() && !(this.peek() === "*" && this.src[this.i + 1] === "/")) {
          this.i += 1;
        }
        if (!this.eof()) {
          this.i += 2;
        }
        continue;
      }
      break;
    }
  }

  private expect(ch: string): void {
    this.skipSpace();
    if (this.peek() !== ch) {
      throw new Error(`Expected '${ch}' near: ${this.src.slice(this.i, this.i + 20)}`);
    }
    this.i += 1;
  }

  private peek(): string {
    return this.src[this.i] ?? "";
  }

  private eof(): boolean {
    return this.i >= this.src.length;
  }

  private isDigit(ch: string): boolean {
    return /[0-9]/.test(ch);
  }
}

function parseAliases(source: string): Record<string, string> {
  const aliases: Record<string, string> = {};
  const re = /\$([A-Za-z_][A-Za-z0-9_]*)\s*=\s*"([^"]+)"\s*;/g;
  for (const match of source.matchAll(re)) {
    aliases[match[1]] = match[2];
  }
  return aliases;
}

function findTopLevelBraceBlock(input: string, braceStart: number): { body: string; end: number } {
  let i = braceStart;
  let depth = 0;
  let inString = false;
  let escaped = false;
  let inLineComment = false;
  let inBlockComment = false;

  while (i < input.length) {
    const ch = input[i];
    const next = input[i + 1] ?? "";

    if (inLineComment) {
      if (ch === "\n") {
        inLineComment = false;
      }
      i += 1;
      continue;
    }

    if (inBlockComment) {
      if (ch === "*" && next === "/") {
        inBlockComment = false;
        i += 2;
        continue;
      }
      i += 1;
      continue;
    }

    if (inString) {
      if (escaped) {
        escaped = false;
      } else if (ch === "\\") {
        escaped = true;
      } else if (ch === "\"") {
        inString = false;
      }
      i += 1;
      continue;
    }

    if (ch === "/" && next === "/") {
      inLineComment = true;
      i += 2;
      continue;
    }
    if (ch === "/" && next === "*") {
      inBlockComment = true;
      i += 2;
      continue;
    }
    if (ch === "\"") {
      inString = true;
      i += 1;
      continue;
    }

    if (ch === "{") {
      depth += 1;
      i += 1;
      continue;
    }
    if (ch === "}") {
      depth -= 1;
      i += 1;
      if (depth === 0) {
        return { body: input.slice(braceStart + 1, i - 1), end: i };
      }
      continue;
    }

    i += 1;
  }

  throw new Error("Unclosed brace block");
}

function splitBodyStatements(body: string): string[] {
  const out: string[] = [];
  let i = 0;
  let start = 0;
  let depthParen = 0;
  let depthBrace = 0;
  let depthBracket = 0;
  let inString = false;
  let escaped = false;
  let inLineComment = false;
  let inBlockComment = false;

  while (i < body.length) {
    const ch = body[i];
    const next = body[i + 1] ?? "";

    if (inLineComment) {
      if (ch === "\n") {
        inLineComment = false;
      }
      i += 1;
      continue;
    }
    if (inBlockComment) {
      if (ch === "*" && next === "/") {
        inBlockComment = false;
        i += 2;
        continue;
      }
      i += 1;
      continue;
    }
    if (inString) {
      if (escaped) {
        escaped = false;
      } else if (ch === "\\") {
        escaped = true;
      } else if (ch === "\"") {
        inString = false;
      }
      i += 1;
      continue;
    }

    if (ch === "/" && next === "/") {
      inLineComment = true;
      i += 2;
      continue;
    }
    if (ch === "/" && next === "*") {
      inBlockComment = true;
      i += 2;
      continue;
    }
    if (ch === "\"") {
      inString = true;
      i += 1;
      continue;
    }

    if (ch === "(") depthParen += 1;
    if (ch === ")") depthParen = Math.max(0, depthParen - 1);
    if (ch === "{") depthBrace += 1;
    if (ch === "}") depthBrace = Math.max(0, depthBrace - 1);
    if (ch === "[") depthBracket += 1;
    if (ch === "]") depthBracket = Math.max(0, depthBracket - 1);

    if ((ch === ";" || ch === "\n") && depthParen === 0 && depthBrace === 0 && depthBracket === 0) {
      const piece = body.slice(start, i).trim();
      if (piece) {
        out.push(piece);
      }
      start = i + 1;
    }

    i += 1;
  }

  const tail = body.slice(start).trim();
  if (tail) {
    out.push(tail);
  }

  return out;
}

function parseElementExpression(expression: string): ElementNode {
  const headMatch = expression.match(/^([A-Za-z_][A-Za-z0-9_]*)\s*\{/s);
  if (!headMatch) {
    throw new Error(`Invalid element expression: ${expression.slice(0, 50)}`);
  }

  const type = headMatch[1];
  const braceStart = expression.indexOf("{");
  const { body } = findTopLevelBraceBlock(expression, braceStart);
  return {
    type,
    alias: null,
    id: null,
    children: parseElementBody(body),
  };
}

function parseElementBody(body: string): ElementChildNode[] {
  const entries = splitBodyStatements(body);
  const nodes: ElementChildNode[] = [];

  for (const raw of entries) {
    const line = raw.trim();
    if (!line) {
      continue;
    }

    if (line.startsWith("#")) {
      const selectorStart = line.indexOf("{");
      if (selectorStart === -1) {
        continue;
      }
      const selector = line.slice(1, selectorStart).trim();
      const { body: selectorBody } = findTopLevelBraceBlock(line, selectorStart);
      const fields: Array<{ name: string; value: ValueNode }> = [];
      for (const fieldLine of splitBodyStatements(selectorBody)) {
        const ix = fieldLine.indexOf(":");
        if (ix === -1) {
          continue;
        }
        const name = fieldLine.slice(0, ix).trim();
        const valueSource = fieldLine.slice(ix + 1).trim().replace(/,$/, "");
        fields.push({ name, value: new ValueParser(valueSource).parseValue() });
      }
      nodes.push({ kind: "selector", selector, fields });
      continue;
    }

    const localVarMatch = line.match(/^@([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.+)$/s);
    if (localVarMatch) {
      try {
        nodes.push({
          kind: "localVar",
          name: localVarMatch[1],
          value: new ValueParser(localVarMatch[2]).parseValue(),
        });
      } catch (error) {
        throw new Error(`Failed local var parse: ${line}\n${String(error)}`);
      }
      continue;
    }

    const fieldIx = line.indexOf(":");
    if (fieldIx !== -1 && !line.includes("{")) {
      const name = line.slice(0, fieldIx).trim();
      const valueSource = line.slice(fieldIx + 1).trim().replace(/,$/, "");
      try {
        nodes.push({ kind: "field", name, value: new ValueParser(valueSource).parseValue() });
      } catch (error) {
        throw new Error(`Failed field parse: ${line}\n${String(error)}`);
      }
      continue;
    }

    const child = parseChildElement(line);
    if (child) {
      nodes.push({ kind: "element", element: child });
    }
  }

  return nodes;
}

function parseChildElement(text: string): ElementNode | null {
  const braceIndex = text.indexOf("{");
  if (braceIndex === -1) {
    return null;
  }
  const head = text.slice(0, braceIndex).trim();
  const { body } = findTopLevelBraceBlock(text, braceIndex);
  const m = head.match(/^(@[A-Za-z_][A-Za-z0-9_]*|[A-Za-z_][A-Za-z0-9_]*)(?:\s*#([A-Za-z_][A-Za-z0-9_]*))?$/);
  if (!m) {
    return null;
  }

  const name = m[1];
  return {
    type: name.replace(/^@/, ""),
    alias: name.startsWith("@") ? name.slice(1) : null,
    id: m[2] ?? null,
    children: parseElementBody(body),
  };
}

function toLowerCamelCase(str: string): string {
  return str.charAt(0).toLowerCase() + str.slice(1);
}

function toGeneratedPropKey(name: string): string {
  if (name === "Style") {
    return "elStyle";
  }
  return toLowerCamelCase(name);
}

function resolveFilePath(currentPath: string, relativeImport: string): string {
  const currentDir = currentPath.replace(/[\\/][^\\/]+$/, "");
  const joined = `${currentDir}/${relativeImport}`.replace(/\\/g, "/");
  const parts = joined.split("/");
  const out: string[] = [];
  for (const part of parts) {
    if (!part || part === ".") {
      continue;
    }
    if (part === "..") {
      out.pop();
      continue;
    }
    out.push(part);
  }
  return out.join("/");
}

class DefinitionGraph {
  private readonly files = new Map<string, UiDefinitionFile>();
  private readonly valueCache = new Map<string, ValueNode>();

  constructor(private readonly root: string) { }

  async loadFile(filePath: string): Promise<UiDefinitionFile> {
    const normalized = filePath.replace(/\\/g, "/");
    const existing = this.files.get(normalized);
    if (existing) {
      return existing;
    }

    const source = await Deno.readTextFile(normalized);
    const defs = parseTopLevelDefinitions(source);
    const byName: Record<string, string> = {};
    for (const def of defs) {
      byName[def.name] = def.expression;
    }

    const file: UiDefinitionFile = {
      path: normalized,
      aliases: parseAliases(source),
      defs: byName,
    };

    this.files.set(normalized, file);
    return file;
  }

  async resolveVar(filePath: string, varName: string, stack: string[] = []): Promise<ValueNode> {
    const normalized = filePath.replace(/\\/g, "/");
    const cacheKey = `${normalized}::${varName}`;
    if (this.valueCache.has(cacheKey)) {
      return this.valueCache.get(cacheKey)!;
    }

    if (stack.includes(cacheKey)) {
      throw new Error(`Variable cycle: ${[...stack, cacheKey].join(" -> ")}`);
    }

    const file = await this.loadFile(normalized);
    const expression = file.defs[varName];
    if (!expression) {
      throw new Error(`Unknown variable @${varName} in ${normalized}`);
    }

    const parsed = new ValueParser(expression).parseValue();
    const resolved = await this.resolveNode(parsed, { localVars: new Map(), filePath: normalized, missingVars: new Set() }, [...stack, cacheKey]);
    this.valueCache.set(cacheKey, resolved);
    return resolved;
  }

  async resolveNode(node: ValueNode, context: EvalContext, stack: string[] = []): Promise<ValueNode> {
    if (node.kind === "propRef") {
      return node; // already a prop reference — do not resolve further
    }

    if (node.kind === "variable") {
      const local = context.localVars.get(node.name);
      if (local) {
        return this.resolveNode(local, context, stack);
      }

      try {
        return await this.resolveVar(context.filePath, node.name, stack);
      } catch {
        context.missingVars.add(node.name);
        return node;
      }
    }

    if (node.kind === "refMember") {
      const file = await this.loadFile(context.filePath);
      const relative = file.aliases[node.ref];
      if (!relative) {
        context.missingVars.add(`${node.ref}.@${node.name}`);
        return node;
      }
      const target = resolveFilePath(context.filePath, relative);
      return this.resolveVar(target, node.name, stack);
    }

    if (node.kind === "array") {
      return {
        kind: "array",
        items: await Promise.all(node.items.map((it) => this.resolveNode(it, context, stack))),
      };
    }

    if (node.kind === "type") {
      const items: TypeItem[] = [];
      for (const item of node.items) {
        if (item.kind === "field") {
          items.push({ kind: "field", name: item.name, value: await this.resolveNode(item.value, context, stack) });
        } else {
          items.push({ kind: "spread", value: await this.resolveNode(item.value, context, stack) });
        }
      }
      return { kind: "type", typeName: node.typeName, items };
    }

    if (node.kind === "math") {
      const left = await this.resolveNode(node.left, context, stack);
      const right = await this.resolveNode(node.right, context, stack);
      return { kind: "math", op: node.op, left, right };
    }

    if (node.kind === "unary") {
      return { kind: "unary", op: "-", value: await this.resolveNode(node.value, context, stack) };
    }

    return node;
  }

  getRootPath(): string {
    return this.root;
  }
}

function evaluateValue(node: ValueNode, propsMap: Record<string, string>): unknown {
  switch (node.kind) {
    case "string":
      return node.value;
    case "number":
      return node.value;
    case "boolean":
      return node.value;
    case "identifier":
      return node.value;
    case "color":
      return node.value;
    case "translation":
      return node.value;
    case "variable": {
      const propName = toLowerCamelCase(node.name);
      propsMap[node.name] = propName;
      return { __propRef: propName };
    }
    case "propRef": {
      const propName = toLowerCamelCase(node.name);
      propsMap[node.name] = propName;
      return { __propRef: propName };
    }
    case "refMember":
      return `$${node.ref}.@${node.name}`;
    case "array":
      return node.items.map((item) => evaluateValue(item, propsMap));
    case "unary": {
      const value = evaluateValue(node.value, propsMap);
      return typeof value === "number" ? -value : value;
    }
    case "math": {
      const left = evaluateValue(node.left, propsMap);
      const right = evaluateValue(node.right, propsMap);
      if (typeof left === "number" && typeof right === "number") {
        if (node.op === "+") return left + right;
        if (node.op === "-") return left - right;
        if (node.op === "*") return left * right;
        if (node.op === "/") return left / right;
      }
      return `${formatJsValue(left)} ${node.op} ${formatJsValue(right)}`;
    }
    case "type": {
      const out: Record<string, unknown> = {};
      for (const item of node.items) {
        if (item.kind === "spread") {
          const spread = evaluateValue(item.value, propsMap);
          if (spread && typeof spread === "object" && !Array.isArray(spread)) {
            Object.assign(out, spread);
          }
          continue;
        }
        out[item.name] = evaluateValue(item.value, propsMap);
      }
      return out;
    }
  }
}

async function resolveElementNode(
  graph: DefinitionGraph,
  element: ElementNode,
  filePath: string,
  inheritedLocals?: Map<string, ValueNode>,
  propVarNames?: Set<string>,
): Promise<{ node: RenderNode; propsMap: Record<string, string> }> {
  const ctx: EvalContext = {
    localVars: new Map(inheritedLocals ?? []),
    filePath,
    missingVars: new Set(),
  };
  const propsMap: Record<string, string> = {};

  const props: Record<string, unknown> = {};
  const children: RenderNode[] = [];

  for (const item of element.children) {
    if (item.kind === "localVar") {
      if (propVarNames?.has(item.name)) {
        // Store a sentinel so references to this var become prop refs
        // (prevents the resolver from falling back to a same-named top-level definition)
        ctx.localVars.set(item.name, { kind: "propRef", name: item.name });
        continue;
      }
      ctx.localVars.set(item.name, await graph.resolveNode(item.value, ctx));
      continue;
    }

    if (item.kind === "field") {
      const resolved = await graph.resolveNode(item.value, ctx);
      props[toGeneratedPropKey(item.name)] = evaluateValue(resolved, propsMap);
      continue;
    }

    if (item.kind === "selector") {
      const selectorObj: Record<string, unknown> = {};
      for (const field of item.fields) {
        const resolved = await graph.resolveNode(field.value, ctx);
        selectorObj[toGeneratedPropKey(field.name)] = evaluateValue(resolved, propsMap);
      }
      const key = toLowerCamelCase(item.selector);
      const current = (props.selectors as Record<string, unknown> | undefined) ?? {};
      current[key] = selectorObj;
      props.selectors = current;
      continue;
    }

    const childDefName = item.element.alias;
    let childElement = item.element;

    if (childDefName) {
      const file = await graph.loadFile(filePath);
      const expr = file.defs[childDefName];
      if (expr) {
        const baseElement = parseElementExpression(expr);
        childElement = {
          ...baseElement,
          id: item.element.id,
          children: [...baseElement.children, ...item.element.children],
        };
      }
    }

    const resolvedChild = await resolveElementNode(graph, childElement, filePath, ctx.localVars);
    Object.assign(propsMap, resolvedChild.propsMap);
    children.push(resolvedChild.node);
  }

  return {
    node: {
      type: element.type,
      id: element.id,
      props,
      children,
    },
    propsMap,
  };
}

function formatJsValue(value: unknown): string {
  if (value && typeof value === "object" && !Array.isArray(value) && "__propRef" in (value as Record<string, unknown>)) {
    return `props.${String((value as Record<string, unknown>).__propRef)}`;
  }

  if (Array.isArray(value)) {
    return `[${value.map((it) => formatJsValue(it)).join(", ")}]`;
  }

  if (value && typeof value === "object") {
    const obj = value as Record<string, unknown>;
    const entries = Object.entries(obj).map(([k, v]) => `${JSON.stringify(k)}: ${formatJsValue(v)}`);
    return `{ ${entries.join(", ")} }`;
  }

  return JSON.stringify(value);
}

function renderNodeToTs(node: RenderNode, indent: string, isRoot = false): string {
  const propsObj: Record<string, unknown> = { ...node.props };
  if (node.id) {
    propsObj.id = node.id;
  }

  const rawPropsCode = formatJsValue(propsObj);
  const propsCode = isRoot ? `{ ...nativeProps, ...${rawPropsCode} }` : rawPropsCode;
  const childrenExpr: string[] = [];

  for (const child of node.children) {
    const slot = child.id ? SLOT_ID_MAP[child.id] : undefined;
    if (slot) {
      const wrapperPropsObj: Record<string, unknown> = { ...child.props };
      if (child.id) wrapperPropsObj.id = child.id;
      const wrapperPropsCode = formatJsValue(wrapperPropsObj);
      const originalChildren = child.children.map(c => renderNodeToTs(c, `${indent}    `, false));
      const fallbackExpr = originalChildren.length > 0 ? `[${originalChildren.join(`, `)}]` : `[]`;
      childrenExpr.push(`h(${JSON.stringify(child.type)}, ${wrapperPropsCode}, slots.${slot} ? slots.${slot}() : ${fallbackExpr})`);
    } else {
      childrenExpr.push(renderNodeToTs(child, `${indent}  `, false));
    }
  }

  const componentExpr = JSON.stringify(node.type);

  if (childrenExpr.length === 0) {
    return `h(${componentExpr}, ${propsCode})`;
  }

  return `h(${componentExpr}, ${propsCode}, [\n${indent}  ${childrenExpr.join(`,\n${indent}  `)}\n${indent}])`;
}

function collectSlotNames(node: RenderNode, out = new Set<string>()): Set<string> {
  for (const child of node.children) {
    const slot = child.id ? SLOT_ID_MAP[child.id] : undefined;
    if (slot) {
      out.add(slot);
    }
    collectSlotNames(child, out);
  }
  return out;
}

function toPascalSafe(name: string): string {
  return name.replace(/[^A-Za-z0-9_]/g, "");
}

function primitiveUiTypeToTs(typeName: string): string | null {
  const map: Record<string, string> = {
    "Integer": "number",
    "Float": "number",
    "Double": "number",
    "Boolean": "boolean",
    "String": "string",
    "Color": "string",
    "UI Path (String)": "string",
  };
  return map[typeName] ?? null;
}

function uiTypeToTs(
  typeName: string,
  uiTypes: Record<string, Record<string, string> | string[]>,
  depth = 0,
): string {
  if (depth > 1) return "unknown";
  const prim = primitiveUiTypeToTs(typeName);
  if (prim) return prim;
  const def = uiTypes[typeName];
  if (!def) return "unknown";
  if (Array.isArray(def)) {
    return def.map((v) => JSON.stringify(v)).join(" | ");
  }
  const fields = Object.entries(def as Record<string, string>)
    .map(([k, v]) => {
      const ft = primitiveUiTypeToTs(v) ?? uiTypeToTs(v, uiTypes, depth + 1);
      return `${k}?: ${ft}`;
    })
    .join("; ");
  return `{ ${fields} }`;
}

function valueNodeToTsType(
  node: ValueNode,
  uiTypes: Record<string, Record<string, string> | string[]>,
): string {
  switch (node.kind) {
    case "boolean": return "boolean";
    case "number": return "number";
    case "string": return "string";
    case "color": return "string";
    case "identifier": return "string";
    case "translation": return "string";
    case "type": {
      if (node.typeName) {
        return uiTypeToTs(node.typeName, uiTypes);
      }
      const fields = node.items
        .filter((it): it is Extract<TypeItem, { kind: "field" }> => it.kind === "field")
        .map((it) => `${it.name}?: ${valueNodeToTsType(it.value, uiTypes)}`)
        .join("; ");
      return `{ ${fields} }`;
    }
    case "array": return "unknown[]";
    case "propRef": return "unknown";
    default: return "unknown";
  }
}

function valueNodeToLiteral(node: ValueNode): string {
  switch (node.kind) {
    case "string": return JSON.stringify(node.value);
    case "number": return String(node.value);
    case "boolean": return String(node.value);
    case "color": return JSON.stringify(node.value);
    case "identifier": return JSON.stringify(node.value);
    case "translation": return JSON.stringify(node.value);
    case "variable": return "undefined";
    case "propRef": return "undefined";
    case "refMember": return "undefined";
    case "unary": {
      const v = valueNodeToLiteral(node.value);
      const n = Number(v);
      if (!isNaN(n)) return String(-n);
      return `-(${v})`;
    }
    case "math": {
      const l = valueNodeToLiteral(node.left);
      const r = valueNodeToLiteral(node.right);
      const ln = Number(l);
      const rn = Number(r);
      if (!isNaN(ln) && !isNaN(rn)) {
        if (node.op === "+") return String(ln + rn);
        if (node.op === "-") return String(ln - rn);
        if (node.op === "*") return String(ln * rn);
        if (node.op === "/") return String(ln / rn);
      }
      return `(${l} ${node.op} ${r})`;
    }
    case "type": {
      const fields: string[] = [];
      for (const item of node.items) {
        if (item.kind === "field") {
          fields.push(`${JSON.stringify(item.name)}: ${valueNodeToLiteral(item.value)}`);
        }
      }
      return `{ ${fields.join(", ")} }`;
    }
    case "array":
      return `[${node.items.map(valueNodeToLiteral).join(", ")}]`;
  }

  throw new Error(`Unsupported value node kind: ${String((node as { kind?: string }).kind)}`);
}

export async function generateVueRenderComponents(options?: {
  inputPath?: string;
  outputDir?: string;
  uiTypesPath?: string;
}): Promise<void> {
  const inputPath = (options?.inputPath ?? "./input/Common.ui").replace(/\\/g, "/");
  const outputDir = (options?.outputDir ?? "./output/components").replace(/\\/g, "/");
  const uiTypesPath = (options?.uiTypesPath ?? "./output/hytale-ui-types.json").replace(/\\/g, "/");

  const source = await Deno.readTextFile(inputPath);
  const defs = parseTopLevelDefinitions(source);
  const graph = new DefinitionGraph(inputPath);
  await graph.loadFile(inputPath);

  let uiTypes: Record<string, Record<string, string> | string[]> = {};
  try {
    const uiTypesJson = JSON.parse(await Deno.readTextFile(uiTypesPath));
    uiTypes = uiTypesJson.types ?? {};
  } catch {
    // uiTypes unavailable; prop types will fall back to unknown
  }

  await Deno.mkdir(outputDir, { recursive: true });
  const componentNames: string[] = [];
  const componentModules: string[] = [];
  let needsPropTypeImport = false;

  for (const def of defs) {
    const elementType = def.expression.match(/^([A-Za-z_][A-Za-z0-9_]*)\s*\{/s);
    if (!elementType) {
      continue;
    }

    const componentName = toPascalSafe(def.name);
    const element = parseElementExpression(def.expression);

    // Collect top-level local vars — these become typed props with defaults
    const localVarNodes = element.children.filter(
      (c): c is Extract<ElementChildNode, { kind: "localVar" }> => c.kind === "localVar",
    );
    const propVarNames = new Set(localVarNodes.map((lv) => lv.name));

    // Resolve each local var's default expression against file-level globals
    const defaultCtx: EvalContext = { localVars: new Map(), filePath: inputPath, missingVars: new Set() };
    const localVarPropDefs = new Map<string, PropDefinition>();
    for (const lv of localVarNodes) {
      const resolvedDefault = await graph.resolveNode(lv.value, defaultCtx);
      localVarPropDefs.set(lv.name, {
        varName: lv.name,
        propName: toLowerCamelCase(lv.name),
        tsType: valueNodeToTsType(resolvedDefault, uiTypes),
        defaultExpr: valueNodeToLiteral(resolvedDefault),
      });
    }

    // Resolve element with local vars excluded from scope (they are now props)
    const resolved = await resolveElementNode(graph, element, inputPath, undefined, propVarNames);

    // Build per-prop runtime declarations + TS type fields
    let needsPropType = false;
    const propDeclParts: string[] = [];
    const customPropTypeFields: string[] = [];
    const customPropNames: string[] = [];

    for (const [varName, propName] of Object.entries(resolved.propsMap)) {
      const pd = localVarPropDefs.get(varName);
      customPropNames.push(propName);
      if (pd) {
        const { tsType, defaultExpr } = pd;
        customPropTypeFields.push(`  ${propName}?: ${tsType};`);
        if (tsType === "boolean") {
          propDeclParts.push(`    ${propName}: {\n      type: Boolean,\n      default: ${defaultExpr},\n    }`);
        } else if (tsType === "number") {
          propDeclParts.push(`    ${propName}: {\n      type: Number,\n      default: ${defaultExpr},\n    }`);
        } else if (tsType === "string") {
          propDeclParts.push(`    ${propName}: {\n      type: String,\n      default: ${defaultExpr},\n    }`);
        } else {
          needsPropType = true;
          propDeclParts.push(`    ${propName}: {\n      type: Object as PropType<${tsType}>,\n      default: () => (${defaultExpr}),\n    }`);
        }
      } else {
        customPropTypeFields.push(`  ${propName}?: unknown;`);
        propDeclParts.push(`    ${propName}: { type: null, required: false }`);
      }
    }

    if (needsPropType) {
      needsPropTypeImport = true;
    }

    const propDecl = propDeclParts.join(",\n");
    const renderBody = renderNodeToTs(resolved.node, "      ", true);
    const baseElementName = resolved.node.type;

    const customPropsTypeName = `${componentName}CustomProps`;
    const propsTypeName = `${componentName}Props`;
    const slotsTypeName = `${componentName}Slots`;

    const customPropsTypeBody = customPropTypeFields.length > 0
      ? customPropTypeFields.join("\n")
      : "";

    const slotNames = Array.from(collectSlotNames(resolved.node)).sort();
    const slotsTypeBody = slotNames.length > 0
      ? slotNames.map((slotName) => `  ${slotName}?: () => VNode[];`).join("\n")
      : "";

    const nativePropsInit = customPropNames.length > 0
      ? `const { ${customPropNames.join(", ")}, ...nativeProps } = props as ${propsTypeName} & Record<string, unknown>;`
      : `const nativeProps = props as ${propsTypeName} & Record<string, unknown>;`;

    const componentCode = `type ${customPropsTypeName} = {\n${customPropsTypeBody}\n};
type ${propsTypeName} = ${customPropsTypeName} & Partial<NATIVE[${JSON.stringify(baseElementName)}]>;
type ${slotsTypeName} = {\n${slotsTypeBody}\n};

const ${componentName} = defineComponent({
  name: ${JSON.stringify(componentName)},
  slots: Object as SlotsType<${slotsTypeName}>,
  props: {\n${propDecl}\n  },
  setup(props, { slots }) {
    ${nativePropsInit}
    return () => ${renderBody};
  },
}) as C<${propsTypeName}, ${slotsTypeName}>;
`;

    componentNames.push(componentName);
    componentModules.push(componentCode);
  }

  const commonEntries = componentNames.join(",\n  ");
  const moduleCode = `/* eslint-disable vue/no-reserved-component-names */\n/* eslint-disable @typescript-eslint/no-empty-object-type */\n/* eslint-disable @typescript-eslint/no-unused-vars */\n/* eslint-disable vue/multi-word-component-names */\nimport type { NATIVE } from "@/types/global";\nimport { defineComponent, h, resolveComponent, type PropType } from "vue";\nimport type { DefineComponent, PublicProps, SlotsType, VNode } from "vue";\n\ntype C<P, S extends Record<string, (...args: any[]) => VNode[]> = Record<never, never>> = DefineComponent<P, {}, {}, {}, {}, {}, {}, {}, string, PublicProps, Readonly<P>, {}, SlotsType<S>>;\n\n${componentModules.join("\n")}\nexport const Common = {\n  ${commonEntries}\n};\n`;

  await Deno.writeTextFile(`${outputDir}/Common.ts`, moduleCode);
}
