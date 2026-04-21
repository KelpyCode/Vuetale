import uiTypes from "./output/hytale-ui-types.json" with { type: "json" }
import commonDefs from "./output/common-ui-definitions.json" with { type: "json" }
import { generateVueRenderComponents } from "./3_generate_vue_components.ts"

function toLowerCamelCase(str: string): string {
    return str.charAt(0).toLowerCase() + str.slice(1);
}

function toKebabCase(str: string): string {
    return str.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
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


    const propNameMaps = {
        Style: "ElStyle"
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

            if (prop.name in propNameMaps) {
                code += `\n    ${toLowerCamelCase(propNameMaps[prop.name as keyof typeof propNameMaps])}?: ${propType}; // Original name: ${prop.name}`
                continue
            }

            code += `\n    ${toLowerCamelCase(prop.name)}?: ${propType};`
        }

        const callbacks = uiTypes.eventCallbacks[key as keyof typeof uiTypes.eventCallbacks] ?? []
        for (const cb of callbacks) {
            if (cb.description) {
                code += `\n    /** ${cb.description} */`
            }
            code += `\n    on${cb.name}?: (...args: any[]) => void;`
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

function generateTypeScriptTags() {

    Deno.writeTextFileSync("output/tags.json", JSON.stringify(Object.keys(uiTypes.elements)))

}

function generateVscodeCssData() {
    const data = {
        "$schema": "https://unpkg.com/vscode-css-languageservice@6.2.10/docs/customData.schema.json",
        "version": 1.1,
        "properties": [] as any[]
    }

    const addedProperties = new Set<string>()

    function addProperty(name: string, description: string, values?: Array<{ name: string }>) {
        if (addedProperties.has(name)) {
            return
        }
        addedProperties.add(name)

        const property: any = {
            name,
            description
        }
        if (values) {
            property.values = values
        }

        data.properties.push(property)
    }

    function normalizeType(type: string): string {
        return type.trim()
    }

    function isStringUnion(type: string): boolean {
        return type.split("/").map(normalizeType).includes("String")
    }

    function resolveType(type: string) {
        return uiTypes.types[type as keyof typeof uiTypes.types]
    }

    function collectCssProperties(propName: string, typeName: string, description: string, prefixParts: string[] = []) {
        const normalizedType = normalizeType(typeName)

        if (isStringUnion(normalizedType)) {
            const fullName = `h-${[...prefixParts, toKebabCase(propName)].join("-")}`
            addProperty(fullName, description)

            for (const unionPart of normalizedType.split("/").map(normalizeType)) {
                if (unionPart === "String") {
                    continue
                }
                collectCssProperties(propName, unionPart, description, prefixParts)
            }
            return
        }

        const listMatch = normalizedType.match(/^List<(.+)>$/)
        if (listMatch) {
            collectCssProperties(propName, listMatch[1], description, prefixParts)
            return
        }

        const typeDef = resolveType(normalizedType)
        if (Array.isArray(typeDef)) {
            const fullName = `h-${[...prefixParts, toKebabCase(propName)].join("-")}`
            addProperty(fullName, description, typeDef.map(v => ({ name: v })))
            return
        }

        if (typeDef) {
            for (const fieldName of Object.keys(typeDef)) {
                collectCssProperties(fieldName, typeDef[fieldName as keyof typeof typeDef], description, [...prefixParts, toKebabCase(propName)])
            }
            return
        }

        const fullName = `h-${[...prefixParts, toKebabCase(propName)].join("-")}`
        addProperty(fullName, description)
    }

    for (const key of Object.keys(uiTypes.elements)) {
        const element = uiTypes.elements[key as keyof typeof uiTypes.elements]
        for (const prop of Object.values(element.properties)) {
            collectCssProperties(prop.name, prop.type, prop.description ?? "")
        }
    }

    Deno.writeTextFileSync("output/vscode-css-data.json", JSON.stringify(data, null, 2))
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

function generateKotlinSchemas() {

    const typeMap = {
        "String": ["String"],
        "Char": ["String"],
        "Integer": ["Number"],
        "Boolean": ["Boolean"],
        "Number": ["Number"],
        "Decimal": ["Number"],
        "Double": ["Number"],
        "Float": ["Number"],
        "Tab[]": ["RefArray", "Tab"],
        "ClientItemStack[]": ["RefArray", "ClientItemStack"],
        "ItemGridSlot[]": ["RefArray", "ItemGridSlot"],
        "ClientItemMetadata": ["Record"],
        "List<String>": ["Array", "String"],
        "List<PatchStyle>": ["RefArray", "PatchStyle"],
        "List<LabelSpan>": ["RefArray", "LabelSpan"],
        "ColorOption": ["String"],
        "List<ColorOption>": ["Array", "String"],
        "Map": ["Record"],
        "PatchStyle / String": ["RefOrString", "PatchStyle"],
        "UI Path (String)": ["String"],
        "Dictionary`2": ["Record"],
        "IReadOnlyList`1": ["Record"],
        "Color": ["ColorString"],
        "Font Name (String)": ["String"],
    }

    function parseListType(type: string): string | null {
        const match = type.match(/^List<(.+)>$/)
        if (!match) {
            return null
        } return match[1]
    }

    let s = [`package li.kelp.vuetale.validator\n`]

    s.push(`var propertySchema: Schema? = null`)
    s.push(``)

    s.push(`fun initializeSchemas() {`)

    s.push(`propertySchema = buildSchema {`)
    for (const key of Object.keys(uiTypes.types)) {
        const type = uiTypes.types[key as keyof typeof uiTypes.types]
        const indent = "    "
        if (Array.isArray(type)) {
            s.push(`${indent}enum("${key}", listOf(${type.map(v => `"${v}"`).join(", ")}))`)
        } else {
            s.push(`${indent}type("${key}", listOf(`)
            for (const field of Object.keys(type)) {
                const fieldType = type[field as keyof typeof type]

                let resolvedType = typeMap[fieldType as keyof typeof typeMap]

                let ref = resolvedType?.[0] ?? key

                // Find ref in types
                const found = uiTypes.types[ref as keyof typeof uiTypes.types]
                if (found) {
                    ref = `PropertyType.Ref, "${fieldType}"`
                } else {
                    ref = resolvedType?.[0] ?? "Unknown"
                    ref = "PropertyType." + ref

                    if (resolvedType?.[1]) {
                        ref += `, "${resolvedType[1]}"`
                    }
                }

                s.push(`${indent}${indent}SchemaField("${field}", ${ref}),`)
            }
            s.push(`${indent}))`)
        }
    }

    for (const key of Object.keys(uiTypes.elements)) {
        const element = uiTypes.elements[key as keyof typeof uiTypes.elements]
        const indent = "    "
        s.push(`${indent}element("${key}", listOf(`)
        for (const prop of Object.values(element.properties)) {
            const field = prop.name
            const fieldType = prop.type

            let resolvedType = typeMap[fieldType as keyof typeof typeMap]

            let ref = resolvedType?.[0] ?? key

            // Find ref in types
            const found = uiTypes.types[fieldType as keyof typeof uiTypes.types]
            if (found) {
                ref = `PropertyType.Ref, "${fieldType}"`
            } else {
                ref = resolvedType?.[0] ?? "Unknown"
                ref = "PropertyType." + ref

                if (resolvedType?.[1]) {
                    ref += `, "${resolvedType[1]}"`
                }
            }

            s.push(`${indent}${indent}SchemaField("${field}", ${ref}),`)
        }
        s.push(`${indent}))`)
    }
    s.push(`}`)

    s.push(`}`)

    Deno.writeTextFileSync("output/PropertySchemas.kt", s.join("\n"))
}

generateTypeScriptTypes()
generateTypeScriptCommonPrefabs()
generateTypeScriptTags()
generateKotlin()
generateKotlinSchemas()
generateVscodeCssData()
await generateVueRenderComponents()