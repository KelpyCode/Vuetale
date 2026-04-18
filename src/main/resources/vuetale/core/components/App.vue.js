import { defineComponent as c, inject as r, shallowRef as d, watch as i, openBlock as a, createElementBlock as p, Fragment as u, createElementVNode as f, createVNode as s, withCtx as v, createBlock as C, resolveDynamicComponent as R, createCommentVNode as g } from "vue";
import E from "./ErrorBoundary.vue.js";
const t = /* @__PURE__ */ c({
  __name: "App",
  setup(m) {
    r("appId");
    const n = r("componentPathRef"), l = d(null);
    function _(e) {
      const o = globalThis._vt?.getRegisteredComponent?.(e);
      o ? (console.log(`[Vuetale] Loading component "${e}" from registry`), l.value = o) : console.error(`[Vuetale] Component "${e}" was not pre-loaded. Call JSEngine.preloadComponent() before navigating.`);
    }
    return n && (n.value && _(n.value), i(n, (e) => {
      e ? _(e) : l.value = null;
    })), (e, o) => (a(), p(u, null, [
      o[0] || (o[0] = f("Label", null, "TESTING", -1)),
      s(E, null, {
        default: v(() => [
          l.value ? (a(), C(R(l.value), { key: 0 })) : g("", !0)
        ]),
        _: 1
      })
    ], 64));
  }
});
t.__hmrId = "5ddfd01a";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(t.__hmrId, t) || __VUE_HMR_RUNTIME__.reload(t.__hmrId, t));
export {
  t as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
