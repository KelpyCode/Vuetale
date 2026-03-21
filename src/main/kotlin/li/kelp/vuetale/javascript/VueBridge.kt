package li.kelp.vuetale.javascript

import java.util.logging.Logger

class VueBridge {
    val logger = Logger.getLogger("VueBridge")
    fun createView(type: String): VueBridge {
        // Your logic to instantiate a native Android/iOS/Desktop view
        return VueBridge()
    }

    fun setProperty(view: VueBridge, key: String, value: Any) {
        // view.applyProperty(key, value)
    }
}