package li.kelp.vuetale.util

object ReflectUtil {
    fun <T> createInstance(javaClass: Class<*>): T {
        return try {
            // Preferred way - handles private constructors too if you call setAccessible(true)
            val constructor = javaClass.getDeclaredConstructor()
            constructor.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            constructor.newInstance() as T
        } catch (e: Exception) {
            throw RuntimeException("Failed to instantiate ${javaClass.name}", e)
        }
    }
}