package li.kelp.vuetale.javascript

import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueString
import com.caoccao.javet.values.reference.V8ValueFunction
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.events.VueEventMapper
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyOrigin
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.style.StyleRegistry
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.ElementContainer
import li.kelp.vuetale.tree.GroupElement
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
        el.properties["Text"] = PropertyString("Text", text)
    }

    fun parentNode(appId: String) {}

    fun nextSibling(appId: String) {}

    fun insert(appId: String, child: Element, parent: Any?) {
        // `parent` is either a proxied Kotlin ElementContainer or the plain JS
        // container wrapper ({_vtContainerId, ...}) that Vue mounts on.
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
            AppManager.getApp(appId)?.markDirty()
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
        val rawId = element.customId ?: element.id
        app?.eventRegistry?.unregisterEvents(rawId)
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
                            el.properties[styleKey] = PropertyNumber(styleKey, sv.asKtInt()).apply {
                                origin = PropertyOrigin.Style
                            }
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
                            app.eventRegistry.unregisterEvents(el.customId ?: el.id)
                        } else {
                            val fn = nextValue as? V8ValueFunction
                            if (fn != null) {
                                app.eventRegistry.registerEvent(el, bindingType, fn)
                            } else {
                                logger.warning("patchProp: expected V8ValueFunction for '$key', got ${nextValue.javaClass.simpleName}")
                            }
                        }
                    }
                } else {
                    // ── Regular Hytale element property ─────────────────────────
                    val keyCapitalized = key.capitalize()
                    if (nextValue.isNullOrUndefined) {
                        el.properties.remove(keyCapitalized)
                    } else if (el.canHaveProperty(keyCapitalized)) {
                        val prop = el.executeProperty(keyCapitalized, nextValue)
                        if (prop != null) {
                            el.properties[keyCapitalized] = prop
                        }
                    } else {
                        logger.warning("Unknown property '$keyCapitalized', ignoring")
                    }
                }
                // Mark dirty for every prop mutation so incremental updates fire correctly
                el.app?.markDirty()
            }
        }


    }
}