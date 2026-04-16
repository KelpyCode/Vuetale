package li.kelp.vuetale.javascript

import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueString
import com.caoccao.javet.values.reference.V8ValueFunction
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.app.App
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.events.VueEventMapper
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
import li.kelp.vuetale.validator.*
import java.util.logging.Logger

class VueBridge(
    private val v8Runtime: V8Runtime,
    private val converter: JavetProxyConverter
) {
    val logger: Logger = Logger.getLogger("VueBridge")

    // ── Styles ─────────────────────────────────────────────────────────────

    fun registerStyles(key: String, value: V8Value) {
        StyleRegistry.registerStyle(key, value as V8ValueObject)
    }

    // ── Renderer host API ──────────────────────────────────────────────────

    fun createElement(appId: String, tag: String): Element {
        val elementClass = Element.findElementClassByTag(tag)
        if (elementClass == null) {
            logger.warning("Unknown tag '$tag', defaulting to 'group'")
            return GroupElement()
        }
        val el = ReflectUtil.createInstance<Element>(elementClass)
        el.app = AppManager.getApp(appId)
        return el
    }

    fun createText(appId: String, text: String) {}

    fun createComment(appId: String) {}

    fun setText(appId: String) {}

    fun setElementText(appId: String, el: Element, text: String) {
        val existing = el.properties["Text"] as? PropertyString
        if (existing?.value == text) return  // no change – skip dirty tracking
        el.properties["Text"] = PropertyString("Text", text)
        val app = AppManager.getApp(appId)
        app?.dirtyElementIds?.add(el.id)  // store generated id; selector built at emit time
        app?.markDirty()
    }

    fun parentNode(appId: String) {}

    fun nextSibling(appId: String) {}

    fun insert(appId: String, child: Element?, parent: Any?) {
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
            actualParent.appendChild(child)
            val app = AppManager.getApp(appId)
            if (app != null) {
                val parentSelector = when {
                    parent is Map<*, *> && parent.containsKey("_vtContainerId") -> "#App"
                    actualParent is RootElement -> "#App"
                    else -> actualParent.buildUniqueSelector()
                }
                app.insertedElements.add(App.InsertedElement(child, parentSelector))
                app.markDirty()
            }
        } else {
            logger.warning("insert: could not resolve parent for child ${child.id}")
        }
    }

    fun remove(appId: String, element: Element) {
        val app = AppManager.getApp(appId)
        if (element.varFrom != null) {
            app?.removeDependency(element.varFrom!!)
        }
        // Clean up event bindings for this element before removing it
        app?.eventRegistry?.unregisterEvents(element.customId ?: element.id)
        app?.removedElementSelectors?.add(element.buildUniqueSelector())
        element.remove()
        app?.markDirty()
    }

    fun patchProp(appId: String, el: Element, key: String, prevValue: V8Value, nextValue: V8Value) {
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
                            val prop: li.kelp.vuetale.property.Property = when (sv) {
                                is com.caoccao.javet.values.primitive.V8ValueBoolean ->
                                    PropertyBoolean(keyCapitalized, sv.value)

                                is com.caoccao.javet.values.primitive.V8ValueString ->
                                    PropertyString(keyCapitalized, sv.value)

                                else ->
                                    PropertyNumber(keyCapitalized, sv.asKtInt())
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
                    el.customId = nextValue.asKtString()
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
                    // ── Regular Hytale element property ─────────────────────────
                    var keyCapitalized = key.capitalize()

                    if (PropertyNameMap.containsKey(keyCapitalized)) {
                        keyCapitalized = PropertyNameMap[keyCapitalized]!!
                    }

                    if (nextValue.isNullOrUndefined) {
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
                                el.app?.dirtyElementIds?.add(el.id)  // store generated id; selector built at emit time
                                el.app?.markDirty()
                            }
                        }
                    } else {
                        logger.warning("Unknown property '$keyCapitalized', ignoring")
                    }
                }
            }
        }


    }
}