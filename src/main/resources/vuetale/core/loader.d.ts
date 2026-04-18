import { App } from 'vue';
interface VuetaleMeta {
    props: Record<string, unknown>;
}
export declare const USER_APPS: Map<string, App<any>>;
export declare const USER_APPS_REF: Map<string, unknown>;
export declare const USER_APPS_META: Map<string, VuetaleMeta>;
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
