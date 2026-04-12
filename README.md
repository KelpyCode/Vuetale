<p align="center">
  <img src="./logo.png" style="max-height: 300px;" />
</p>

# Vuetale

**Modern Vue.js + TypeScript UI development for Hytale mods.**

Bring the powerful developer experience of Vue.js and TypeScript to Hytale. Write reactive, component-based UIs with
full autocompletion, excellent tooling, and a clean separation of concerns - all while staying fully compatible with
Hytale's UI system.

https://opencollective.com/vuetale
https://discord.gg/affkepndn7

---

## Why Vuetale?

Web developers love Vue for its **intuitive reactivity**, **single-file components**, and **fantastic DX**.  
Vuetale brings exactly that to Hytale by using a custom Vue renderer powered by **Javet** and a Kotlin
bridge.

### Key Benefits

- **Reactive UI** - Changes in your data automatically update the interface.
- **Full TypeScript support** - 100% typed Hytale UI elements and properties (autocompletion included).
- **Component-based architecture** - Build reusable components with logic, template, and style in one `.vue` file.
- **Excellent scaling** - Vue scales naturally; reuse components easily with props.
- **Clean separation** - UI logic runs on its own thread (not the world thread).
- **Zero runtime HTML/CSS parsing** - Everything is pre-compiled during build.

### Performance & Memory

- Very performant for a modern framework approach (not quite as fast as 100% native, but more than acceptable).
- Single shared Javet engine per server - loads Vue and all components only once.
- Two lightweight Vue apps per player (`HUD` + `Page`) with negligible memory overhead.
- All heavy lifting happens at build time.

---

## How It Works

1. You get a **pre-configured Vite project** inside your mod/plugin for the UI part.
2. Write your UI using standard `.vue` Single-File Components with TypeScript.
3. The Vite build step:
    - Compiles `.vue` files to JavaScript
    - Extracts and converts CSS to optimized JSON (via PostCSS)
4. At runtime, Vuetale’s custom Vue renderer bridges everything to Hytale’s UI system via Javet and Kotlin.

No runtime parsing. Maximum performance. Maximum developer happiness.

---

## Getting Started

> **Note**: Detailed installation and setup instructions coming soon.  
> For now, please join the [Vuetale Discord](https://discord.gg/affkepndn7) for the latest guides and examples.

### Recommended Setup

- Use **VS Code** + official **Vue** extension for the best experience with the Vite project, alongside IntelliJ IDEA
  for your plugin.
- Keep your JVM mod project and the Vite UI project open side-by-side.

---

## Current Progress

### ✅ Finished

- Javet integration + Vue.js loading
- Vite build pipeline (SFC → JS + CSS → JSON)
- Custom Vue renderer with full Kotlin bridge
- Kotlin-side element tree with Vue support
- Basic style and class support
- **100% TypeScript typings** for all current Hytale UI elements and `Common.ui`
- App separation (`createApp` per player) within a single JS context
- **Property validation** (with helpful CLI warnings)

### 🚧 In Progress / Todo

- Actual rendering in Hytale (Page & HUD handling)
- Javet custom filesystem support for loading from mod JARs
- Vite plugin to extract `.d.ts` from other mods (for cross-mod autocompletion)
- Better Java interop support
- Passing JVM values into Vue apps + reactivity helpers
- `setTimeout` / `setInterval` polyfills
- Documentation site

### Long-term Goals

- Tailwind-like utility classes
- Official Vuetale component library mod (date pickers, dialogs, advanced components, etc.)
- More quality-of-life features based on community feedback

---

## Property Validation

Vuetale includes robust, code-generated property validation to prevent Hytale client crashes from invalid UI
properties.  
It warns you during development if you use unknown properties or invalid values, while still attempting to render
safely.

See the validator here:  
[
`PropertySchemaData.kt`](https://github.com/KelpyCode/Vuetale/blob/master/src/main/kotlin/li/kelp/vuetale/validator/PropertySchemaData.kt)

## Contributing

Contributions are welcome! Feel free to open issues or PRs.

If you have ideas for the official component library or other features, let us know in Discord.

---

**Made with ❤️ for the Hytale modding community.**

*Bringing modern web development workflows to block game UI since 2026.*