type StyleTree = Record<string, unknown>;
declare global {
    interface Window {
        __vt_applyStyles__?: (styles: StyleTree, key?: string) => void;
        __vt_pendingStyles__?: Array<[string | undefined, StyleTree]>;
        ktBridge?: {
            registerStyles?: (key: string, styles: StyleTree) => void;
            applyStyles?: (key: string, styles: StyleTree) => void;
            registerCss?: (key: string, styles: StyleTree) => void;
        };
    }
}
export declare function applyStyles(styles: StyleTree, key?: string): void;
export declare function flushPendingStyles(): void;
export {};
