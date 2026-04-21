import { defineComponent as d, ref as c, onBeforeUnmount as m, onErrorCaptured as f, renderSlot as p, openBlock as _, createBlock as g, unref as a, withCtx as h, createElementVNode as l, createElementBlock as y, toDisplayString as E, createCommentVNode as v, createVNode as C } from "vue";
import { Common as u } from "./Common.js";
const I = {
  background: { Color: "#111111" },
  padding: { Full: 10 }
}, N = {
  key: 0,
  "el-style": {}
}, t = /* @__PURE__ */ d({
  __name: "ErrorBoundary",
  setup(s) {
    const e = c(null);
    let r = !1;
    m(() => {
      r = !0;
    });
    const i = () => {
      e.value = null;
    };
    return f((o) => (r || (e.value = o, console.error("Component error caught by boundary:", o)), !1)), (o, n) => e.value ? (_(), g(a(u).DecoratedContainer, {
      key: 1,
      anchor: { Width: 500, Height: 300 }
    }, {
      content: h(() => [
        n[0] || (n[0] = l("Label", null, "Something went wrong in this section.", -1)),
        l("Group", I, [
          e.value ? (_(), y("Label", N, E(e.value.message), 1)) : v("", !0)
        ]),
        C(a(u).TextButton, {
          onActivating: i,
          text: "Try again"
        })
      ]),
      _: 1
    })) : p(o.$slots, "default", { key: 0 });
  }
});
t.__hmrId = "2e92e1ba";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(t.__hmrId, t) || __VUE_HMR_RUNTIME__.reload(t.__hmrId, t));
export {
  t as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiRXJyb3JCb3VuZGFyeS52dWUuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
