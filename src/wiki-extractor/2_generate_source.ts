import uiTypes from "./hytale-ui-types.json" with { type: "json" }
import commonDefs from "./output/common-ui-definitions.json" with { type: "json" }

function toLowerCamelCase(str: string): string {
    return str.charAt(0).toLowerCase() + str.slice(1);
}

function generateTypeScriptTypes() {

    const typeMap = {
        "String": "string",
        "Char": "string",
        "Integer": "number",
        "Boolean": "boolean",
        "Number": "number",
        "Decimal": "number",
        "Double": "number",
        "Float": "number",
        "ClientItemMetadata": "Record<string, any>",
        "List": "Array",
        "List<String>": "Array<string>",
        "List<PatchStyle>": "Array<PatchStyle>",
        "List<LabelSpan>": "Array<LabelSpan>",
        "ColorOption": "unknown",
        "List<ColorOption>": "Array<unknown>",
        "Map": "Record",
        "PatchStyle / String": "PatchStyle | string",
        "UI Path (String)": "string",
        "Dictionary`2": "Record<string, any>",
        "IReadOnlyList`1": "Array<any>",
        "Color": "string",
        "Font Name (String)": "string",
    }

    let src: string[] = []

    src.push(`/* eslint-disable @typescript-eslint/no-explicit-any */
import type { DefineComponent } from 'vue'

// eslint-disable-next-line @typescript-eslint/no-empty-object-type
type C<T> = DefineComponent<T, {}, {}, {}, {}, {}, {}>`)

    for (const key of Object.keys(uiTypes.types)) {
        const type = uiTypes.types[key as keyof typeof uiTypes.types]

        if (Array.isArray(type)) {
            src.push(`export type ${key} = ${type.map(t => typeMap[t as keyof typeof typeMap] ?? t).map(s => `"${s}"`).join(" | ")};`)
        } else {
            let code = ""
            code += `export interface ${key} {\n`
            for (const field of Object.keys(type)) {
                const fieldType = type[field as keyof typeof type]
                code += `  ${field}?: ${typeMap[fieldType as keyof typeof typeMap] ?? fieldType};\n`
            }
            code += `}`
            src.push(code)
        }
    }


    src.push("export type NATIVE = {")

    for (const key of Object.keys(uiTypes.elements)) {
        const type = uiTypes.elements[key as keyof typeof uiTypes.elements]
        let code = ""
        code += `  ${key}: {`

        for (const prop of Object.values(type.properties)) {
            const propType = typeMap[prop.type as keyof typeof typeMap] ?? prop.type

            if (prop.description) {
                code += `\n    /** ${prop.description} */`
            }

            code += `\n    ${toLowerCamelCase(prop.name)}?: ${propType};`
        }

        code += `\n  };`
        src.push(code)

    }

    src.push("}\n")

    src.push(`declare module 'vue' {
    export interface GlobalComponents {`)

    for (const key of Object.keys(uiTypes.elements)) {
        const type = uiTypes.elements[key as keyof typeof uiTypes.elements]
        let code = ""
        code += `      ${key}: C<NATIVE["${key}"]>;`
        src.push(code)

    }

    src.push(`   }
}`)
    Deno.writeTextFileSync("output/hytale-ui-types.d.ts", src.join("\n"))
}

function generateTypeScriptCommonPrefabs() {
    const src: string[] = []

    src.push(`import { createPrefabComponent } from "@/helpers/prefab";

export const Common = {`)
    Object.entries(commonDefs.elements).forEach(([key, element]) => {
        src.push(`  ${key}: createPrefabComponent("Common.ui", "${key}", "${element}"),`)
    })


    src.push(`}\n`)

    Deno.writeTextFileSync("output/common.ts", src.join("\n"))
}

function generateKotlin() {


    const aliases = {
        group: ["div"],
        label: ["span", "p"]
    }

    let s = `package li.kelp.vuetale.tree

import li.kelp.vuetale.validator.PropertyValidator

val elementTagMap = mutableMapOf<String, Class<out Element>>()

fun initializeElements() {
`

    for (const key of Object.keys(uiTypes.elements)) {
        const type = uiTypes.elements[key as keyof typeof uiTypes.elements]

        const props = Object.values(type.properties).map(p => `"${p.name}"`).join(", ")

        const declarations = [key.toLowerCase(), ...aliases[key.toLowerCase() as keyof typeof aliases] ?? []].map(s => `  elementTagMap["${s}"] = ${key}Element::class.java`).join("\n")

        s += `  // ${key} element
${declarations}
  PropertyValidator.registerProperties(${key}Element::class.java, listOf(${props}))

`


    }

    s += "}\n"

    /*
    
class LabelElement() : Element() {
    override val elementType = "Label"

    companion object {
        init {
            PropertyValidator.registerProperties(LabelElement::class.java, listOf())

            val x: PatchStyle
        }
    }
} */

    for (const key of Object.keys(uiTypes.elements)) {
        const type = uiTypes.elements[key as keyof typeof uiTypes.elements]

        const props = Object.values(type.properties).map(p => `"${p.name}"`).join(", ")

        const declarations = [key.toLowerCase(), ...aliases[key.toLowerCase() as keyof typeof aliases] ?? []].map(s => `elementTagMap["${s}"] = ${key}Element::class.java`).join("\n            ")

        s += `
class ${key}Element() : Element${type.acceptsChildElements ? 'Container' : ''}("${key}") {}
        `


    }

    Deno.writeTextFileSync("output/Elements.kt", s)
}

generateTypeScriptTypes()
generateTypeScriptCommonPrefabs()
generateKotlin()