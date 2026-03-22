package li.kelp.vuetale.javascript

import org.graalvm.polyglot.Value
import java.util.logging.Logger

class VueBridge {
    val logger = Logger.getLogger("VueBridge")
    fun createElement(appId: String, tag: String) {
        logger.info("Creating element with tag '$tag' for app '$appId'")
    }
    fun createText(appId: String, text: String) {

    }
    fun createComment(appId: String) {

    }
    fun setText(appId: String) {

    }
    fun setElementText(appId: String, el: Value, text: String) {
        logger.info("Setting element text for app '$appId': $text")
    }
    fun parentNode(appId: String) {

    }
    fun nextSibling(appId: String) {

    }
    fun insert(appId: String, child: Value, parent: Value) {
        logger.info("Inserting child into parent for app '$appId'")
    }
    fun remove(appId: String) {

    }
    fun patchProp(appId: String, el: Value, key: String, prevValue: Value, nextValue: Value) {
        logger.info("Patching property '$key' for app '$appId': $prevValue -> $nextValue")
    }
}