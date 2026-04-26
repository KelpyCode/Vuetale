package li.kelp.vuetale.javascript

import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueBoolean
import com.caoccao.javet.values.primitive.V8ValueString
import com.caoccao.javet.values.reference.V8ValueFunction
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.app.App
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.events.VueEventMapper
import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyBoolean
import li.kelp.vuetale.property.PropertyNameMap
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyOrigin
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.style.StyleRegistry
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.ElementContainer
import li.kelp.vuetale.tree.GroupElement
import li.kelp.vuetale.tree.RootElement
import li.kelp.vuetale.util.ReflectUtil
import li.kelp.vuetale.util.StringUtil.capitalize
import li.kelp.vuetale.util.StringUtil.fromKebabCaseToPascalCase
import li.kelp.vuetale.validator.*
import java.lang.reflect.InvocationTargetException
import java.util.logging.Logger
import li.kelp.vuetale.javascript.DebugConfig
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import java.util.function.Consumer

class VueBridge(
    private val v8Runtime: V8Runtime,
    private val converter: JavetProxyConverter
) {
    val logger: Logger = Logger.getLogger("VueBridge")

    /** Expose the current debug flag to callers (JS can call ktBridge.isDebugEnabled()). */
    fun isDebugEnabled(): Boolean = DebugConfig.enabled

    // Host callback registry: maps hostId -> original JVM callback object.
    // Callbacks are registered when Kotlin code calls App.setData with a function
    // and are invoked synchronously from JS via ktBridge.invokeHostCallback.
    private val hostCallbacks: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    // ── Styles ─────────────────────────────────────────────────────────────

    fun registerStyles(key: String, value: V8Value) {
        StyleRegistry.registerStyle(key, value as V8ValueObject)
    }

    /**
     * Register a JVM callback for the given app and return a stable hostId.
     * The returned hostId is passed to JS as a marker; JS creates a wrapper that
     * calls ktBridge.invokeHostCallback(hostId, ...args) to invoke the JVM callback.
     */
    fun registerHostCallback(appId: String, callback: Any): String {
        val hostId = "${appId}-cb-${UUID.randomUUID()}"
        hostCallbacks[hostId] = callback
        if (DebugConfig.enabled) logger.info("[vuetaledebug] registerHostCallback: registered $hostId for app $appId")
        return hostId
    }

    /**
     * Invoke a previously-registered host callback. Called from JS synchronously.
     * Attempts to call a sensible method on the underlying JVM object (invoke, apply,
     * accept, run, call, etc.) and returns its result back to the caller.
     */
    fun invokeHostCallback(hostId: String, vararg args: Any?): Any? {
        val cb = hostCallbacks[hostId]
            ?: throw IllegalStateException("Host callback not found: $hostId")
        return callCallback(cb, args)
    }

    /**
     * Unregister all host callbacks for a given app id. Used during app cleanup/hot-reload.
     */
    fun unregisterHostCallbacksForApp(appId: String) {
        val prefix = "${appId}-cb-"
        hostCallbacks.keys.filter { it.startsWith(prefix) }.forEach { hostCallbacks.remove(it) }
        if (DebugConfig.enabled) logger.info("[vuetaledebug] unregisterHostCallbacksForApp: cleared callbacks for $appId")
    }

    /**
     * dont question it, it works
     */
    private fun callCallback(callback: Any, args: Array<out Any?>): Any? {
        try {
            // Direct support for Kotlin function interfaces
            when (callback) {
                is Function0<*> -> return callback.invoke()
                is Function1<*, *> -> return (callback as Function1<Any?, Any?>).invoke(args.getOrNull(0))
                is Function2<*, *, *> -> return (callback as Function2<Any?, Any?, Any?>).invoke(
                    args.getOrNull(0),
                    args.getOrNull(1)
                )

                is Function3<*, *, *, *> -> return (callback as Function3<Any?, Any?, Any?, Any?>).invoke(
                    args.getOrNull(
                        0
                    ), args.getOrNull(1), args.getOrNull(2)
                )

                is Function4<*, *, *, *, *> -> return (callback as Function4<Any?, Any?, Any?, Any?, Any?>).invoke(
                    args.getOrNull(
                        0
                    ), args.getOrNull(1), args.getOrNull(2), args.getOrNull(3)
                )

                is Function5<*, *, *, *, *, *> -> return (callback as Function5<Any?, Any?, Any?, Any?, Any?, Any?>).invoke(
                    args.getOrNull(0),
                    args.getOrNull(1),
                    args.getOrNull(2),
                    args.getOrNull(3),
                    args.getOrNull(4)
                )

                is Function6<*, *, *, *, *, *, *> -> return (callback as Function6<Any?, Any?, Any?, Any?, Any?, Any?, Any?>).invoke(
                    args.getOrNull(0),
                    args.getOrNull(1),
                    args.getOrNull(2),
                    args.getOrNull(3),
                    args.getOrNull(4),
                    args.getOrNull(5)
                )

                is Runnable -> {
                    callback.run(); return null
                }

                is Callable<*> -> return (callback as Callable<Any?>).call()
                is Consumer<*> -> {
                    (callback as Consumer<Any?>).accept(args.getOrNull(0)); return null
                }

                is java.util.function.Function<*, *> -> return (callback as java.util.function.Function<Any?, Any?>).apply(
                    args.getOrNull(0)
                )

                is BiFunction<*, *, *> -> return (callback as BiFunction<Any?, Any?, Any?>).apply(
                    args.getOrNull(0),
                    args.getOrNull(1)
                )
            }

            // Fallback: reflection. Make target method accessible to handle lambda classes with restricted visibility.
            val cls = callback.javaClass
            val candidates = listOf("invoke", "apply", "accept", "call", "run", "execute")
            for (name in candidates) {
                val m = cls.methods.firstOrNull { it.name == name && it.parameterCount == args.size }
                if (m != null) {
                    m.isAccessible = true
                    return try {
                        m.invoke(callback, *args)
                    } catch (e: Exception) {
                        throw e.cause ?: e
                    }
                }
            }

            val fallback =
                cls.methods.firstOrNull { it.declaringClass != Any::class.java && it.parameterCount == args.size }
            if (fallback != null) {
                fallback.isAccessible = true
                return try {
                    fallback.invoke(callback, *args)
                } catch (e: Exception) {
                    throw e.cause ?: e
                }
            }

            throw IllegalArgumentException("No callable method found on host callback of type ${cls.name}")
        } catch (e: InvocationTargetException) {
            throw e.cause ?: e
        }
    }

    // ── Renderer host API ──────────────────────────────────────────────────

    fun createElement(appId: String, tag: String): Element {
        val elementClass = Element.findElementClassByTag(tag)
        if (elementClass == null) {
            if (DebugConfig.enabled) logger.warning("[vuetaledebug] Unknown tag '$tag', defaulting to 'group'")
            return GroupElement()
        }
        val el = ReflectUtil.createInstance<Element>(elementClass)
        el.app = AppManager.getApp(appId)
        if (DebugConfig.enabled) logger.info("[vuetaledebug] createElement app=$appId tag=$tag -> ${elementClass.simpleName}")
        return el
    }

    fun createText(appId: String, text: String) {}

    fun createComment(appId: String) {}

    fun setText(appId: String) {}

    fun setElementText(appId: String, el: Element, text: String) {
        val existing = el.properties["Text"] as? PropertyString
        if (existing?.value == text) return  // no change – skip dirty tracking
        el.properties["Text"] = PropertyString("Text", text)
        if (el.isVtSkipUpdate()) return  // Vuetale flag suppresses dirty flush
        val app = AppManager.getApp(appId)
        if (DebugConfig.enabled) logger.info("[vuetaledebug] setElementText app=$appId el=${el.getId()} textPreview=${if (text.length>100) text.substring(0,100)+"..." else text}")
        app?.dirtyElementIds?.add(el.id)
        app?.markDirty()
    }

    fun parentNode(appId: String, element: Element): ElementContainer? {
        return element.parent
    }

    fun nextSibling(appId: String, element: Element): Element? {
        val parent = element.parent ?: return null
        val idx = parent.children.indexOf(element)
        if (idx < 0) return null
        return parent.children.getOrNull(idx + 1)
    }

    fun insert(appId: String, child: Element?, parent: Any?, anchor: Element? = null) {
        if (child == null) return
        val actualParent: ElementContainer? = when {
            parent is ElementContainer -> parent
            parent is Map<*, *> && parent.containsKey("_vtContainerId") ->
                AppManager.getApp(appId)?.root

            else -> {
                logger.warning("insert: unknown parent type ${parent?.javaClass}, ignoring")
                null
            }
        }
        if (actualParent != null) {
            if (DebugConfig.enabled) logger.info("[vuetaledebug] insert app=$appId child=${child.id} parent=${actualParent.buildUniqueSelector()} anchor=${anchor?.id}")
            val existingIdx = actualParent.children.indexOf(child)
            val alreadyCorrect: Boolean = if (existingIdx < 0) {
                false  // not in parent at all — must insert
            } else if (anchor != null) {
                // Correct position = child is immediately before anchor
                val anchorIdx = actualParent.children.indexOf(anchor)
                anchorIdx >= 0 && existingIdx == anchorIdx - 1
            } else {
                // Correct position = child is already last
                existingIdx == actualParent.children.size - 1
            }

            if (alreadyCorrect) return  // No structural change, nothing to do

            if (anchor != null) {
                val anchorIdx = actualParent.children.indexOf(anchor)
                if (anchorIdx >= 0) {
                    actualParent.children.remove(child)
                    val insertIdx = actualParent.children.indexOf(anchor) // re-query after removal
                    actualParent.insertChild(child, insertIdx)
                } else {
                    actualParent.appendChild(child)
                }
            } else {
                actualParent.appendChild(child)
            }
            val app = AppManager.getApp(appId)
            if (app != null) {
                val parentSelector = when {
                    parent is Map<*, *> && parent.containsKey("_vtContainerId") -> "#App"
                    actualParent is RootElement -> "#App"
                    else -> actualParent.buildUniqueSelector()
                }
                app.insertedElements.add(App.InsertedElement(child, parentSelector))
                // A child being re-inserted at a different position is a structural change
                // (order matters for Hytale's UI tree).
                app.hasStructuralChanges = true
                app.markDirty()
            }
        } else {
            if (DebugConfig.enabled) logger.warning("[vuetaledebug] insert: could not resolve parent for child ${child.id}")
        }

    }

    fun remove(appId: String, element: Element?) {
        if (element == null) return
        val app = AppManager.getApp(appId)
        if (element.varFrom != null) {
            app?.removeDependency(element.varFrom!!)
        }
        app?.removedElementSelectors?.add(element.buildUniqueSelector())
        // Clean up this element and every descendant: unregister event bindings and
        // remove from idElementMap.  Only calling remove() on the root would leave
        // child elements as orphans in EventRegistry, causing "event binding target
        // not found" errors when those stale bindings are re-sent on the next update.
        cleanupElementTree(app, element)
        if (DebugConfig.enabled) logger.info("[vuetaledebug] remove app=$appId element=${element.buildUniqueSelector()}")
        app?.markDirty()
    }

    /**
     * Recursively unregisters event bindings and removes every element in the subtree
     * rooted at [element] from [Element.idElementMap].
     * Children are processed before the parent so that [Element.remove] (which calls
     * [Element.detachFromParent]) operates on a consistent list.
     */
    private fun cleanupElementTree(app: App?, element: Element) {
        // Recurse into children first (copy to avoid CME from detachFromParent)
        if (element is ElementContainer) {
            element.children.toList().forEach { cleanupElementTree(app, it) }
        }
        app?.eventRegistry?.unregisterEvents(element.getId())
        element.remove()  // detachFromParent + idElementMap.remove
    }

    fun patchProp(appId: String, el: Element, key: String, prevValue: V8Value, nextValue: V8Value) {
        if (DebugConfig.enabled) {
            val pv = try { if (prevValue.isNullOrUndefined) "<null>" else prevValue.javaClass.simpleName } catch (e: Exception) { "<err>" }
            val nv = try { if (nextValue.isNullOrUndefined) "<null>" else nextValue.javaClass.simpleName } catch (e: Exception) { "<err>" }
            logger.fine("[vuetaledebug] patchProp app=$appId el=${el.getId()} key=$key prev=$pv next=$nv")
        }
        fun clearPropertiesWithOrigin(origin: PropertyOrigin) {
            el.properties = el.properties.filter { it.value.origin != origin }.toMutableMap()
        }

        when (key) {
            "style" -> {
                clearPropertiesWithOrigin(PropertyOrigin.Style)
                if (!nextValue.isNullOrUndefined) {
                    val nextObj = nextValue as V8ValueObject
                    nextObj.memberKeys().forEach { styleKey ->
                        nextObj.get<V8Value>(styleKey).use { sv ->
                            val keyCapitalized = styleKey.capitalize()
                            val prop: Property = when (sv) {
                                is V8ValueBoolean ->
                                    PropertyBoolean(keyCapitalized, sv.value)

                                is V8ValueString ->
                                    PropertyString(keyCapitalized, sv.value)

                                else ->
                                    PropertyNumber(keyCapitalized, sv.asKtDouble())
                            }
                            prop.origin = PropertyOrigin.Style
                            el.properties[keyCapitalized] = prop
                        }
                    }
                }
            }

            "class" -> {
                clearPropertiesWithOrigin(PropertyOrigin.Class)
                if (!nextValue.isNullOrUndefined) {
                    val classes = (nextValue as V8ValueString).value.split(" ")
                    classes.forEach { c ->
                        StyleRegistry.getPropertiesForClass(c).forEach { prop ->
                            try {
                                el.setPropertySafe(prop.name, prop)
                            } catch (e: Exception) {
                                logger.warning("Failed to set property '${prop.name}' from class '$c': ${e.message}")
                            }
                        }
                    }
                }
            }

            "id" -> {
                if (!nextValue.isNullOrUndefined) {
                    el.customId = nextValue.asKtString().replace(".", "")
                }
            }

            "varFrom" -> {
                val prev = if (prevValue.isNullOrUndefined) null else prevValue.asKtString()
                if (prev != null) {
                    el.app?.removeDependency(prev)
                }
                el.varFrom = if (nextValue.isNullOrUndefined) null else nextValue.asKtString()
                if (el.varFrom != null) {
                    el.app?.addDependency(el.varFrom!!)
                }
            }

            "varName" -> {
                el.varName = if (nextValue.isNullOrUndefined) null else nextValue.asKtString()
            }

            else -> {
                // ── Vue event handlers (on*) ─────────────────────────────────────
                val bindingType = VueEventMapper.map(key)
                if (bindingType != null) {
                    val app = el.app
                    if (app != null) {
                        if (nextValue.isNullOrUndefined) {
                            app.eventRegistry.unregisterEvents(el.getId())
                        } else {
                            val fn = nextValue as? V8ValueFunction
                            if (fn != null) {
                                // Clone the function to take ownership of the V8 reference.
                                // JavetProxyConverter passes arguments as *borrowed* handles that
                                // Javet closes once patchProp returns.  Without cloning, the stored
                                // callback would be a dead reference and callVoid would throw
                                // "Runtime is already closed".
                                val ownedFn = fn.toClone<V8ValueFunction>()
                                app.eventRegistry.registerEvent(el, bindingType, ownedFn)
                            } else {
                                logger.warning("patchProp: expected V8ValueFunction for '$key', got ${nextValue.javaClass.simpleName}")
                            }
                        }
                    }
                    // Event-binding changes don't mutate visual properties – no markDirty needed.
                } else {
                    // ── Vuetale-internal property (Vt*) ─────────────────────────
                    // Vue passes prop names in kebab-case; convert to PascalCase for Hytale/Vuetale lookup.
                    // e.g. vt-skip-update → VtSkipUpdate, layout-mode → LayoutMode
                    var keyCapitalized = key.fromKebabCaseToPascalCase()

                    if (PropertyNameMap.containsKey(keyCapitalized)) {
                        keyCapitalized = PropertyNameMap[keyCapitalized]!!
                    }

                    if (vtPropertySchema.isVuetaleProperty(keyCapitalized)) {
                        // Vuetale props are stored on the element but never rendered
                        // and never trigger dirty/re-render.
                        el.vuetaleProperties[keyCapitalized] = vtPropertySchema.parse(keyCapitalized, nextValue)
                        if (DebugConfig.enabled) logger.info("[vuetaledebug] patchProp vuetaleProp app=$appId el=${el.getId()} prop=$keyCapitalized")
                    } else if (nextValue.isNullOrUndefined) {
                        el.properties.remove(keyCapitalized)
                        // Removal can't be expressed as a targeted set command – need full re-render
                        el.app?.hasStructuralChanges = true
                        el.app?.markDirty()
                    } else if (el.canHaveProperty(keyCapitalized)) {
                        val prop = el.executeProperty(keyCapitalized, nextValue)
                        if (prop != null) {
                            val existing = el.properties[keyCapitalized]
                            // Only mark dirty when the value actually changed.
                            // Vue re-patches every prop on each component re-render, so without
                            // this guard static props (Title, Content, CloseButton…) would be
                            // spuriously added to dirtyElementIds on every keystroke.
                            if (existing?.render() != prop.render()) {
                                el.properties[keyCapitalized] = prop
                                if (!el.isVtSkipUpdate()) {
                                    el.app?.dirtyElementIds?.add(el.id)
                                    el.app?.markDirty()
                                }
                            }
                        }
                    } else {
                        logger.warning("Unknown property '$keyCapitalized', ignoring")
                            if (DebugConfig.enabled) logger.info("[vuetaledebug] patchProp unknownProperty app=$appId el=${el.getId()} prop=$keyCapitalized")
                    }
                }
            }
        }


    }
}