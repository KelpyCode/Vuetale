import { defineComponent as d, inject as r, shallowRef as i, watch as p, openBlock as a, createBlock as c, withCtx as f, resolveDynamicComponent as s, createCommentVNode as u } from "vue";
import v from "./ErrorBoundary.vue.js";
const o = /* @__PURE__ */ d({
  __name: "App",
  setup(m) {
    r("appId");
    const n = r("componentPathRef"), t = i(null);
    function l(e) {
      const _ = globalThis._vt?.getRegisteredComponent?.(e);
      _ ? (console.log(`[Vuetale] Loading component "${e}" from registry`), t.value = _) : console.error(`[Vuetale] Component "${e}" was not pre-loaded. Call JSEngine.preloadComponent() before navigating.`);
    }
    return n && (n.value && l(n.value), p(n, (e) => {
      e ? l(e) : t.value = null;
    })), (e, _) => (a(), c(v, null, {
      default: f(() => [
        t.value ? (a(), c(s(t.value), { key: 0 })) : u("", !0)
      ]),
      _: 1
    }));
  }
});
o.__hmrId = "5ddfd01a";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(o.__hmrId, o) || __VUE_HMR_RUNTIME__.reload(o.__hmrId, o));
export {
  o as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
