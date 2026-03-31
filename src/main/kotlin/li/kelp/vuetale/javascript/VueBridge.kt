package li.kelp.vuetale.javascript

import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyOrigin
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.style.StyleRegistry
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.ElementContainer
import li.kelp.vuetale.tree.GroupElement
import li.kelp.vuetale.util.ReflectUtil
import li.kelp.vuetale.validator.PropertyValidator.canHaveProperty
import org.graalvm.polyglot.Value
import java.util.logging.Logger

class VueBridge {
    val logger = Logger.getLogger("VueBridge")

    // Styles

    fun registerStyles(key: String, value: Value) {
        StyleRegistry.registerStyle(key, value)
    }

    // Renderer

    fun createElement(appId: String, tag: String): Element {
        // logger.info("Creating element with tag '$tag' for app '$appId'")

        val elementClass = Element.findElementClassByTag(tag)
        if(elementClass == null) {
            logger.warning("Unknown tag '$tag', defaulting to 'group'")
            return GroupElement()
        }

        return ReflectUtil.createInstance<Element>(elementClass)
    }
    fun createText(appId: String, text: String) {

    }
    fun createComment(appId: String) {

    }
    fun setText(appId: String) {

    }
    fun setElementText(appId: String, el: Value, text: String) {
        // logger.info("Setting element text for app '$appId': $text")

        val element = el.asHostObject<Element>()
        element.properties["Text"] = PropertyString("Text", text)
    }
    fun parentNode(appId: String) {

    }
    fun nextSibling(appId: String) {

    }
    fun insert(appId: String, child: Value, parent: Value) {
        // logger.info("Inserting child into parent for app '$appId'")

        val childEl = child.asHostObject<Element>()
        val parentEl = parent.asHostObject<Element>()

        if(parentEl is ElementContainer) {
            parentEl.appendChild(childEl)
        } else {
            logger.warning("Parent element ${parentEl.id} is not a container, cannot append child ${childEl.id}")
        }
    }
    fun remove(appId: String) {

    }
    fun patchProp(appId: String, el: Value, key: String, prevValue: Value, nextValue: Value) {
        // logger.info("Patching property '$key' for app '$appId': $prevValue -> $nextValue")

        val element = el.asHostObject<Element>()

        fun clearPropertiesWithOrigin(origin: PropertyOrigin) {
            element.properties = element.properties.filter { it.value.origin != origin }.toMutableMap()
        }

        when(key) {
            "style" -> {
                // Clear existing style properties
                clearPropertiesWithOrigin(PropertyOrigin.Style)

                nextValue.memberKeys.forEach {
                    val styleKey = it.toString()
                    val styleValue = nextValue.getMember(it).asString()
                    element.properties["$styleKey"] = PropertyNumber(styleKey, styleValue.toInt()).apply { origin =
                        PropertyOrigin.Style
                    }
                }
            }
            "class" -> {
                // Clear existing style properties
                clearPropertiesWithOrigin(PropertyOrigin.Class)

                val classes = nextValue.asString()?.split(" ") ?: emptyList()
                classes.forEach { c ->
                    StyleRegistry.getPropertiesForClass(c).forEach {
                        try {
                            element.setPropertySafe(it.name, it)
                        } catch (e: Exception) {
                            logger.warning("Failed to set property '${it.name}' from class '$c': ${e.message}")
                        }
                    }
                }
            }
            "id" -> {
                element.customId = nextValue.asString()
            }
            else -> {
                if(nextValue.isNull) {
                    element.properties.remove(key)
                    return
                }
                else if(element.canHaveProperty(key)) {
                    element.properties[key] = PropertyString(key, nextValue.asString()).apply { origin = PropertyOrigin.Attribute }
                } else {
                    logger.warning("Unknown property '$key', ignoring")
                }
            }
        }


    }
}