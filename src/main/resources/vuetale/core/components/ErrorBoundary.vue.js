import { defineComponent as s, ref as m, onBeforeUnmount as i, onErrorCaptured as d, renderSlot as c, openBlock as _, createElementBlock as a, createElementVNode as f, toDisplayString as p, createCommentVNode as E, createVNode as v, unref as y } from "vue";
import { Common as g } from "./Common.js";
const I = { key: 1 }, N = { key: 0 }, t = /* @__PURE__ */ s({
  __name: "ErrorBoundary",
  setup(l) {
    const e = m(null);
    let r = !1;
    i(() => {
      r = !0;
    });
    const u = () => {
      e.value = null;
    };
    return d((o) => (r || (e.value = o, console.error("Component error caught by boundary:", o)), !1)), (o, n) => e.value ? (_(), a("Group", I, [
      n[0] || (n[0] = f("Label", null, "Something went wrong in this section.", -1)),
      e.value ? (_(), a("Label", N, p(e.value.message), 1)) : E("", !0),
      v(y(g).TextButton, {
        onActivating: u,
        text: "Try again"
      })
    ])) : c(o.$slots, "default", { key: 0 });
  }
});
t.__hmrId = "2e92e1ba";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(t.__hmrId, t) || __VUE_HMR_RUNTIME__.reload(t.__hmrId, t));
export {
  t as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiRXJyb3JCb3VuZGFyeS52dWUuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
