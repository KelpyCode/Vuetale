import { App } from 'vue';
interface VuetaleMeta {
    props: Record<string, unknown>;
}
export declare const USER_APPS: Map<string, App<any>>;
export declare const USER_APPS_REF: Map<string, unknown>;
export declare const USER_APPS_META: Map<string, VuetaleMeta>;
/**
 * Per-app reactive data store.
 * Each entry is a shallowReactive plain object so Vue's computed/watch can
 * track individual key additions and mutations directly.
 */
export declare const USER_APPS_DATA: Map<string, Record<string, unknown>>;
/**
 * Set (or create) a reactive data value for an app.
 * Called from the Kotlin side via JSEngine / loaderCtx.invoke.
 */
export declare function setAppData(id: string, key: string, value: unknown): void;
export declare function registerComponent(path: string, component: unknown): void;
export declare function getRegisteredComponent(path: string): unknown | undefined;
export declare function removeUserApp(id: string): void;
export declare function createUserApp(id: string, componentPath?: string): App<any>;
export declare function navigateTo(id: string, componentPath: string): void;
export declare function getUserApp(id: string): App<any> | undefined;
export declare function getUserAppRef(id: string): unknown;
export declare function getUserAppMeta(id: string): VuetaleMeta | undefined;
export declare function registerUserAppRef(id: string, ref: unknown): void;
export {};
