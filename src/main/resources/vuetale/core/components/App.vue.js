import { defineComponent as i, inject as a, shallowRef as m, watch as p, openBlock as c, createBlock as r, withCtx as s, resolveDynamicComponent as u, createCommentVNode as f } from "vue";
import d from "./ErrorBoundary.vue.js";
const C = /* @__PURE__ */ i({
  __name: "App",
  setup(_) {
    a("appId");
    const o = a("componentPathRef"), n = m(null);
    function l(e) {
      const t = globalThis._vt?.getRegisteredComponent?.(e);
      t ? (console.log(`[Vuetale] Loading component "${e}" from registry`), n.value = t) : console.error(`[Vuetale] Component "${e}" was not pre-loaded. Call JSEngine.preloadComponent() before navigating.`);
    }
    return o && (o.value && l(o.value), p(o, (e) => {
      e ? l(e) : n.value = null;
    })), (e, t) => (c(), r(d, null, {
      default: s(() => [
        n.value ? (c(), r(u(n.value), { key: 0 })) : f("", !0)
      ]),
      _: 1
    }));
  }
});
export {
  C as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
