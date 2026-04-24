# Vuetale

> 🧙 Sponsord by [Hytale Modding Grant Program](https://opencollective.com/hytalemodding)

**Build Hytale mod UIs with Vue 3, TypeScript, and Kotlin/Java.**

Vuetale is a Hytale mod library that brings modern frontend development to Hytale mod interfaces. Write reactive,
component-based UIs in Vue SFCs, compile them through Vite, and drive them from your Kotlin/Java mod code — all with
full type safety.

[Vuetale Discord](https://discord.gg/affkepndn7)

---

## How It Works

Vuetale embeds a Node.js/V8 engine inside your mod's JVM process. Vue components are compiled by Vite at build time and
output as JavaScript bundles into your mod's `src/main/resources/vuetale/` folder. At runtime, Vue's render output is
translated through a custom bridge into native Hytale UI elements — no HTML, no CSS parsing, no browser involved.

```
Vue SFC  →  Vite build  →  resources/vuetale/<module>/  →  loaded by JVM mod  →  native Hytale UI
```

---

## Features

- **Vue 3 SFCs** — write `.vue` files with `<script setup>`, composables, and full reactivity
- **Kotlin/Java API** — open pages and push reactive data from server-side mod code
- **TypeScript types** — companion CLI extracts type definitions from JARs for IDE support
- **Hot reload** — iterate on UI without restarting the server
- **Tailwind CSS** — Tailwind v4 supported out of the box in the starter project
- **Per-player isolation** — each player gets their own Vue app instance

---

## Quick Start

> For an in-depth guide, see the [Getting Started Guide](https://kelpycode.github.io/vuetale-docs/docs/getting-started).

### 1. Get Vuetale

Download from either

* [GitHub Releases](https://github.com/KelpyCode/Vuetale/releases)
* [Modtale](https://modtale.net/mod/vuetale)
* [Curseforge](https://www.curseforge.com/hytale/mods/vuetale)

### 2. Clone the starter project

```bash
git clone git@github.com:KelpyCode/vuetale-starter.git --recurse-submodules my-mod-project
cd my-mod-project
rm -rf .git
rm -rf src/ui/.git
```

### 3. Place the JAR

Copy `Vuetale-*.jar` to `lib/Vuetale.jar`.

### 4. Validate the server (IntelliJ)

1. Sync Gradle dependencies.
2. Run `runServer`.
3. Copy `Vuetale.jar` into `run/mods/`.
4. Rerun the server and type `/vuetale` in chat — you should see the test UI.

### 5. Set up the UI project (VS Code → `src/ui`)

```bash
pnpm install
pnpm vt config server-mods <absolute-path-to-run/mods>
pnpm vt config resources <path-to-src/main/resources>
pnpm vt extract
pnpm vt dev true
pnpm watch
```

That's it. Vite watches your Vue files and outputs compiled bundles into `src/main/resources/vuetale/<module>/`, which
the server picks up automatically.

---

## Usage

### Register your module (Kotlin)

```kotlin
// In your plugin setup function
ModuleRegistry.registerModule("myMod", Plugin::class.java)
```

### Open a UI page for a player

```kotlin
val ui = PlayerUiManager.openPage(
    playerRef,
    ref as Ref<EntityStore>,
    store as Store<EntityStore>,
    "myMod",   // matches vuetale-plugin.json name
    "TestPage" // matches src/ui/lib/pages/TestPage.vue
)

// Push data to the Vue page
ui.setData("playerName", "Steve")
ui.setData("health", 20)
ui.setData("items", listOf("Sword", "Shield", "Potion"))
```

Any serializable value works — primitives, lists, maps, and data classes.

### Consume data in Vue

```html

<script setup lang="ts">
    import { useData } from 'vt:@core/composables/useData'
    import { Common } from 'vt:@core/components/Common'
    
    const playerName = useData<string>('playerName', 'Unknown')
    const items = useData<string[]>('items', [])
</script>

<template>
    <Common.DecoratedContainer :anchor="{ Width: 500, Height: 300 }">
        <template #title>
            <Common.Title :text="`${playerName}'s Inventory`"/>
        </template>
        <template #content>
            <Group layout-mode="TopScrolling" :anchor="{ Full: 1 }">
                <Common.TextButton
                        v-for="item in items"
                        :key="item"
                        :text="item"
                        @activating="() => console.log(item)"
                />
            </Group>
        </template>
    </Common.DecoratedContainer>
</template>
```

---

## Requirements

| Tool    | Version |
|---------|---------|
| Java    | 25      |
| Kotlin  | 2.x     |
| Node.js | 20+     |
| pnpm    | any     |
| Vue     | 3.5+    |
| Vite    | 7+      |

---

## Related Projects

| Project                                                             | Description                                            |
|---------------------------------------------------------------------|--------------------------------------------------------|
| [vuetale-starter](https://github.com/KelpyCode/vuetale-starter)     | Starter template with Gradle + Vite setup              |
| [vuetale-companion](https://github.com/KelpyCode/vuetale-companion) | CLI for project setup, type extraction, and hot reload |
| [vuetale-docs](https://github.com/KelpyCode/vuetale-docs)           | Full documentation site                                |

---

## Documentation

Full guides, API reference, and CLI docs at the documentation site.

- [Getting Started](https://kelpycode.github.io/vuetale-docs/docs/getting-started)
- [Kotlin API](https://kelpycode.github.io/vuetale-docs/docs/api/kotlin)
- [Vue API](https://kelpycode.github.io/vuetale-docs/docs/api/vue)
- [Companion CLI](https://kelpycode.github.io/vuetale-docs/docs/cli/companion-cli)
- [Troubleshooting](https://kelpycode.github.io/vuetale-docs/docs/troubleshooting)



