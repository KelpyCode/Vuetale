# Vuetale – AI Agent Guide

## Architecture Overview

Vuetale is a **Hytale mod** that embeds Vue.js inside a JVM via **Javet** (V8/Node.js). It has three sub-projects:

| Sub-project | Path | Purpose |
|---|---|---|
| Kotlin mod | `src/main/kotlin/li/kelp/vuetale/` | Hytale plugin, V8 host, element tree |
| Vue library | `src/vuetale/` | Vite + Vue 3 SFC project, custom renderer |
| Wiki extractor | `src/wiki-extractor/` | Deno scripts to regenerate typings from Hytale wiki |

**Data flow:** Vue component renders → custom renderer (`lib/renderer.ts`) calls `ktBridge.*` → `VueBridge.kt` manipulates Kotlin `Element` tree → tree rendered to Hytale UI.

## Build Commands

```bash
# Kotlin mod (fat JAR → build/libs/ and run/mods/)
./gradlew shadowJar

# Run standalone (no Hytale server, for quick JS testing)
./gradlew runMain

# Vue library – watch mode (outputs → src/main/resources/vuetale/core/)
cd src/vuetale && pnpm run watch

# Vue library – production build + type-check
cd src/vuetale && pnpm run build

# Wiki extractor (Deno, not Node)
cd src/wiki-extractor && deno run -A 2_generate_source.ts
```

The Vite output directory is controlled by `src/vuetale/lib/vuetale-plugin.json` (`"name": "core"`), which maps to `src/main/resources/vuetale/core/`. Always rebuild the Vue project after changing `.vue` or `.ts` files in `src/vuetale/lib/`.

## Hot Reload (Dev Mode)

During development you can edit Vue/TS files and have the server automatically reload V8 + remount all apps — no server restart needed.

### Setup
1. Copy `run/vuetale-dev.properties` and fill in your absolute path:
   ```properties
   vuetale.devResourcesPath=C:/Users/yourname/Projects/Hytale/Vuetale/src/main/resources
   ```
2. Terminal 1 – start Vite in watch mode:
   ```bash
   cd src/vuetale && pnpm run watch
   ```
3. Terminal 2 – start the server:
   ```bash
   ./gradlew runMain
   ```

Every time Vite finishes writing to `src/main/resources/vuetale/core/`, `HotReloadManager` detects the change, debounces 400 ms, then:
- Calls `forceReset()` on all `App` objects (clears element tree + event bindings without touching V8).
- Closes the old `JSEngine` (tears down V8 entirely).
- Creates a fresh `JSEngine` (boots new V8 + Vue IIFE + `loader.js` + `debug.js`).
- Calls `reinitializeInEngine()` on every `App` to re-register it in the new V8.
- Re-mounts all apps that were previously mounted.
- Sets `isDirty = true` on each, which fires `onDirty` → `sendUpdate()` on the next tick.

### Dev-only files
| File | Purpose |
|---|---|
| `src/main/kotlin/.../javascript/DevConfig.kt` | Reads `vuetale.devResourcesPath` from system property or `run/vuetale-dev.properties` |
| `src/main/kotlin/.../javascript/HotReloadManager.kt` | WatchService daemon + debounce + restart orchestration |
| `src/vuetale/lib/debug.ts` | Dev-only Vue module – enables performance tracking, exposes `globalThis.__vt_debug__` |

`debug.ts` is excluded from production Vite builds (`pnpm run build`) and is only loaded by `JSEngine` when `DevConfig.isDevMode` is true.

### Optional: Vue dev build
For full Vue runtime warnings, place the dev IIFE (`vue.esm-browser.js` renamed to `vue.dev.js`) next to `src/main/resources/vue.js`.  `JSEngine` checks for `vue.dev.js` first in dev mode.

## Key Architectural Rules

### V8 Thread Confinement
`JSEngine` owns a **single** `V8Runtime` confined to the `vuetale-v8` daemon thread. **Never call V8 APIs directly from other threads.** Always use:
- `JSEngine.instance.evalScript("...")` for fire-and-forget JS
- `JSEngine.instance.runOnV8Thread { ... }` when you need a return value

The Node.js event loop is auto-pumped at ~20 Hz by an internal scheduler.

### Adding a New Hytale UI Element
1. Create `src/main/kotlin/li/kelp/vuetale/tree/YourElement.kt` extending `Element` or `ElementContainer`.
2. Register the tag and valid properties in `Elements.kt` (`initializeElements()`):
   ```kotlin
   elementTagMap["yourelement"] = YourElement::class.java
   PropertyValidator.registerProperties(YourElement::class.java, listOf("Anchor", "ContentWidth", ...))
   ```
3. Add the corresponding TypeScript component type to `src/vuetale/lib/types/global.d.ts` (usually regenerated via the wiki extractor).

### Property Naming Convention
- Vue props use **lowerCamelCase** (e.g., `:contentWidth="100"`).
- Kotlin `Element.properties` keys use **PascalCase** (`"ContentWidth"`).
- `VueBridge.patchProp` calls `.capitalize()` to bridge the two. Do not store raw lowercase keys in `Element.properties`.

### Special Vue Props
These are intercepted by `VueBridge.patchProp` before property dispatch:
- `style` → parsed directly into `PropertyOrigin.Style` properties
- `class` → looked up in `StyleRegistry`; only `.className` selectors work (no descendant selectors)
- `id` → sets `Element.customId`
- `varFrom` / `varName` → cross-app dependency tracking (not a Hytale UI property)

### CSS/Styling
CSS is **pre-compiled** by `CssBuildPlugin` at Vite build time. Only class selectors (`.foo`) are supported at runtime. Deep selectors (`.foo .bar`) will throw `UnsupportedSelectorException`.

## App Lifecycle

```kotlin
val app = AppManager.createApp("playerName", AppType.Page) // or AppType.Hud
// ... JSEngine is lazily initialized on first access
app.mount()   // calls _vt.getUserApp(id).mount(ref)
app.unmount() // call before server shutdown
```

App IDs follow the pattern `"$owner-$type"` (e.g., `"test-Page"`).

## JS ↔ Kotlin Global Objects

| JS global | Kotlin source | Purpose |
|---|---|---|
| `ktBridge` | `VueBridge.kt` | Renderer host: createElement, patchProp, etc. |
| `_vt` | `lib/loader.ts` | createUserApp, getUserApp, registerUserAppRef |

## Module Resolution
JS `import` statements are resolved from the **classpath** (JAR resources), not the filesystem. Absolute imports (e.g., `import 'vue'`) resolve to `vuetale/core/vue.js`; relative imports resolve relative to the referrer path within `vuetale/core/`.

## Key Files

- `src/main/kotlin/li/kelp/vuetale/javascript/JSEngine.kt` – V8 lifecycle, module resolver, thread model
- `src/main/kotlin/li/kelp/vuetale/javascript/VueBridge.kt` – renderer host API, prop dispatch
- `src/main/kotlin/li/kelp/vuetale/tree/Elements.kt` – all registered element tags + property lists
- `src/vuetale/lib/renderer.ts` – custom Vue renderer (calls `ktBridge`)
- `src/vuetale/lib/loader.ts` – JS entry point, exposes `_vt` global
- `src/vuetale/vite.config.ts` – Vite build config (entry discovery, output path)

