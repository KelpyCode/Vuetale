import { ComputedRef } from 'vue';
/**
 * Access a reactive data value pushed from the Kotlin/JVM side via
 * `playerUi.setData("key", value)` (or `app.setData("key", value)`).
 *
 * The returned `ComputedRef` updates automatically whenever Kotlin pushes a new
 * value for the same key. If no value has been set yet, `defaultValue` is returned.
 *
 * @example
 * ```vue
 * <script setup lang="ts">
 * import { useData } from '@core/composables/useData'
 *
 * const health = useData<number>('playerHealth', 0)
 * </script>
 *
 * <template>
 *   <label :text="`Health: ${health}`" />
 * </template>
 * ```
 */
export declare function useData<T>(key: string, defaultValue?: T): ComputedRef<T>;
