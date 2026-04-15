import { App } from 'vue';
export declare const USER_APPS: Map<string, App<any>>;
export declare const USER_APPS_REF: Map<string, unknown>;
export declare function removeUserApp(id: string): void;
export declare function createUserApp(id: string): App<any>;
export declare function getUserApp(id: string): App<any> | undefined;
export declare function getUserAppRef(id: string): unknown;
export declare function registerUserAppRef(id: string, ref: unknown): void;
