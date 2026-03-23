package li.kelp.vuetale.javascript

import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.ElementContainer
import li.kelp.vuetale.tree.GroupElement
import li.kelp.vuetale.tree.getElementClassForTag
import org.graalvm.polyglot.Value
import java.util.logging.Logger

class VueBridge {
    val logger = Logger.getLogger("VueBridge")
    fun createElement(appId: String, tag: String): Element {
        logger.info("Creating element with tag '$tag' for app '$appId'")

        val elementClass = getElementClassForTag(tag)
        if(elementClass == null) {
            logger.warning("Unknown tag '$tag', defaulting to 'group'")
            return GroupElement()
        }

        return elementClass.constructors.first().call()
    }
    fun createText(appId: String, text: String) {

    }
    fun createComment(appId: String) {

    }
    fun setText(appId: String) {

    }
    fun setElementText(appId: String, el: Value, text: String) {
        logger.info("Setting element text for app '$appId': $text")

        val element = el.asHostObject<Element>()
        element.properties["Text"] = PropertyString("Text", text)
    }
    fun parentNode(appId: String) {

    }
    fun nextSibling(appId: String) {

    }
    fun insert(appId: String, child: Value, parent: Value) {
        logger.info("Inserting child into parent for app '$appId'")

        val childEl = child.asHostObject<Element>()
        val parentEl = parent.asHostObject<ElementContainer>()

        childEl.appendTo(parentEl)
    }
    fun remove(appId: String) {

    }
    fun patchProp(appId: String, el: Value, key: String, prevValue: Value, nextValue: Value) {
        logger.info("Patching property '$key' for app '$appId': $prevValue -> $nextValue")

        val element = el.asHostObject<Element>()

        if(key == "style") {
            nextValue.memberKeys.forEach {
                val styleKey = it.toString()
                val styleValue = nextValue.getMember(it).asString()
                element.properties["$styleKey"] = PropertyNumber(styleKey, styleValue.toInt())
            }
        }
    }
}