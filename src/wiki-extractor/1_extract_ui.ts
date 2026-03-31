export type UiExtractionResult = {
    elements: Record<string, string>;
    vars: string[];
};

type TopLevelDefinition = {
    name: string;
    expression: string;
};

function isIdentChar(char: string): boolean {
    return /[A-Za-z0-9_]/.test(char);
}

function isSpace(char: string): boolean {
    return /\s/.test(char);
}

export function parseTopLevelDefinitions(source: string): TopLevelDefinition[] {
    const definitions: TopLevelDefinition[] = [];

    let depthParen = 0;
    let depthBrace = 0;
    let depthBracket = 0;

    let inString = false;
    let escaped = false;
    let inLineComment = false;

    let i = 0;
    while (i < source.length) {
        const char = source[i];
        const next = source[i + 1] ?? "";

        if (inLineComment) {
            if (char === "\n") {
                inLineComment = false;
            }
            i += 1;
            continue;
        }

        if (inString) {
            if (escaped) {
                escaped = false;
            } else if (char === "\\") {
                escaped = true;
            } else if (char === '"') {
                inString = false;
            }
            i += 1;
            continue;
        }

        if (char === "/" && next === "/") {
            inLineComment = true;
            i += 2;
            continue;
        }

        if (char === '"') {
            inString = true;
            i += 1;
            continue;
        }

        if (char === "(") {
            depthParen += 1;
            i += 1;
            continue;
        }
        if (char === ")") {
            depthParen = Math.max(0, depthParen - 1);
            i += 1;
            continue;
        }
        if (char === "{") {
            depthBrace += 1;
            i += 1;
            continue;
        }
        if (char === "}") {
            depthBrace = Math.max(0, depthBrace - 1);
            i += 1;
            continue;
        }
        if (char === "[") {
            depthBracket += 1;
            i += 1;
            continue;
        }
        if (char === "]") {
            depthBracket = Math.max(0, depthBracket - 1);
            i += 1;
            continue;
        }

        const isTopLevel = depthParen === 0 && depthBrace === 0 && depthBracket === 0;
        if (!isTopLevel || char !== "@") {
            i += 1;
            continue;
        }

        let cursor = i + 1;
        if (!/[A-Za-z_]/.test(source[cursor] ?? "")) {
            i += 1;
            continue;
        }

        while (cursor < source.length && isIdentChar(source[cursor])) {
            cursor += 1;
        }
        const name = source.slice(i + 1, cursor);

        while (cursor < source.length && isSpace(source[cursor])) {
            cursor += 1;
        }

        if (source[cursor] !== "=") {
            i += 1;
            continue;
        }

        cursor += 1;
        while (cursor < source.length && isSpace(source[cursor])) {
            cursor += 1;
        }

        const expressionStart = cursor;

        let localParen = 0;
        let localBrace = 0;
        let localBracket = 0;
        let localInString = false;
        let localEscaped = false;
        let localInLineComment = false;

        while (cursor < source.length) {
            const current = source[cursor];
            const localNext = source[cursor + 1] ?? "";

            if (localInLineComment) {
                if (current === "\n") {
                    localInLineComment = false;
                }
                cursor += 1;
                continue;
            }

            if (localInString) {
                if (localEscaped) {
                    localEscaped = false;
                } else if (current === "\\") {
                    localEscaped = true;
                } else if (current === '"') {
                    localInString = false;
                }
                cursor += 1;
                continue;
            }

            if (current === "/" && localNext === "/") {
                localInLineComment = true;
                cursor += 2;
                continue;
            }

            if (current === '"') {
                localInString = true;
                cursor += 1;
                continue;
            }

            if (current === "(") {
                localParen += 1;
                cursor += 1;
                continue;
            }
            if (current === ")") {
                localParen = Math.max(0, localParen - 1);
                cursor += 1;
                continue;
            }
            if (current === "{") {
                localBrace += 1;
                cursor += 1;
                continue;
            }
            if (current === "}") {
                localBrace = Math.max(0, localBrace - 1);
                cursor += 1;
                continue;
            }
            if (current === "[") {
                localBracket += 1;
                cursor += 1;
                continue;
            }
            if (current === "]") {
                localBracket = Math.max(0, localBracket - 1);
                cursor += 1;
                continue;
            }

            if (
                current === ";" &&
                localParen === 0 &&
                localBrace === 0 &&
                localBracket === 0
            ) {
                const expression = source.slice(expressionStart, cursor).trim();
                definitions.push({ name, expression });
                cursor += 1;
                break;
            }

            cursor += 1;
        }

        i = cursor;
    }

    return definitions;
}

function extractElementType(expression: string): string | null {
    const match = expression.match(/^([A-Za-z_][A-Za-z0-9_]*)\s*\{/s);
    if (!match) {
        return null;
    }
    return match[1];
}

export function buildUiExtractionResult(source: string): UiExtractionResult {
    const definitions = parseTopLevelDefinitions(source);
    const elements: Record<string, string> = {};
    const vars: string[] = [];

    for (const definition of definitions) {
        const elementType = extractElementType(definition.expression);
        if (elementType) {
            elements[definition.name] = elementType;
            continue;
        }

        vars.push(definition.name);
    }

    return { elements, vars };
}

function parseCliArgs(args: string[]): { input: string; output: string } {
    let input = "./input/Common.ui";
    let output = "./output/common-ui-definitions.json";

    for (let i = 0; i < args.length; i += 1) {
        const arg = args[i];

        if (arg === "--input" || arg === "-i") {
            input = args[i + 1] ?? input;
            i += 1;
            continue;
        }

        if (arg === "--output" || arg === "-o") {
            output = args[i + 1] ?? output;
            i += 1;
        }
    }

    return { input, output };
}

export async function extractUiDefinitionsToFile(
    inputPath = "./input/Common.ui",
    outputPath = "./output/common-ui-definitions.json",
): Promise<UiExtractionResult> {
    const source = await Deno.readTextFile(inputPath);
    const result = buildUiExtractionResult(source);
    await Deno.writeTextFile(outputPath, `${JSON.stringify(result, null, 2)}\n`);
    return result;
}

if (import.meta.main) {
    const { input, output } = parseCliArgs(Deno.args);
    const result = await extractUiDefinitionsToFile(input, output);
    console.log(
        `Generated ${output} with ${Object.keys(result.elements).length} elements and ${result.vars.length} vars`,
    );
}
