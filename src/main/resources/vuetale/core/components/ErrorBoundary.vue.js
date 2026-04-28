import { defineComponent as i, ref as c, onBeforeUnmount as m, onErrorCaptured as d, renderSlot as f, openBlock as r, createBlock as p, unref as a, withCtx as g, createElementVNode as l, createElementBlock as _, toDisplayString as y, createCommentVNode as h, createVNode as C } from "vue";
import { Common as u } from "./Common.js";
const k = {
  background: { Color: "#111111" },
  padding: { Full: 10 }
}, v = {
  key: 0,
  "el-style": {}
}, E = /* @__PURE__ */ i({
  __name: "ErrorBoundary",
  setup(B) {
    const e = c(null);
    let o = !1;
    m(() => {
      o = !0;
    });
    const s = () => {
      e.value = null;
    };
    return d((t) => (o || (e.value = t, console.error("Component error caught by boundary:", t)), !1)), (t, n) => e.value ? (r(), p(a(u).DecoratedContainer, {
      key: 1,
      anchor: { Width: 500, Height: 300 }
    }, {
      content: g(() => [
        n[0] || (n[0] = l("Label", null, "Something went wrong in this section.", -1)),
        l("Group", k, [
          e.value ? (r(), _("Label", v, y(e.value.message), 1)) : h("", !0)
        ]),
        C(a(u).TextButton, {
          onActivating: s,
          text: "Try again"
        })
      ]),
      _: 1
    })) : f(t.$slots, "default", { key: 0 });
  }
});
export {
  E as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiRXJyb3JCb3VuZGFyeS52dWUuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
