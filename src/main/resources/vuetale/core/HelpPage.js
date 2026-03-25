import { defineComponent as o, openBlock as t, createElementBlock as n, createElementVNode as l, createVNode as r } from "vue";
import p from "./HelloWorld.js";
const u = /* @__PURE__ */ o({
  __name: "HelpPage",
  setup(m) {
    return (s, e) => (t(), n("div", null, [
      e[0] || (e[0] = l("h1", null, "Help", -1)),
      e[1] || (e[1] = l("p", null, "This is the help page.", -1)),
      r(p, { msg: "abc" })
    ]));
  }
});
export {
  u as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiSGVscFBhZ2UuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7In0=
