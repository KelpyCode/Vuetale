package li.kelp.vuetale.app

import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.initializeElements


object AppManager {
    init {
        initializeElements()
    }

    val apps: MutableMap<String, App> = mutableMapOf()

    fun getAppId(owner: String, type: AppType): String {
        return "$owner-$type"
    }
    private fun getEngine() = JSEngine.instance

    fun createApp(id: String, type: AppType): App {
        val fullId = getAppId(id, type)

        if(apps.containsKey(fullId)) {
            throw IllegalArgumentException("App with id '$id' already exists")
        }

        return App(id, type)
    }

    fun getApp(id: String): App? {
        return apps[id]
    }

    fun addApp(app: App) {
        apps[app.getId()] = app
    }

    fun removeApp(id: String) {
        getApp(id)?.let {
            if (it.isMounted) {
                it.unmount()
            }
        }
        apps.remove(id)
    }

    fun removeApp(owner: String, type: AppType) {
        val id = getAppId(owner, type)
        removeApp(id)
    }

    fun removeOwnerApps(owner: String) {
        val idsToRemove = apps.keys.filter { it.startsWith("$owner-") }
        for (id in idsToRemove) {
            removeApp(id)
        }
    }


}