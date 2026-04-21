import { ComputedRef } from 'vue';
type ForwardedOptions = {
    exclude?: string[];
};
export declare const useForwardedBindings: <T extends Record<string, unknown>>(props: T, attrs: Record<string, unknown>, options?: ForwardedOptions) => {
    forwardedProps: ComputedRef<Partial<T>>;
    forwardedBindings: ComputedRef<Record<string, unknown>>;
};
export {};
