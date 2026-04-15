import { defineComponent as n, openBlock as l, createElementBlock as m, createVNode as _, createElementVNode as c, unref as d } from "vue";
import p from "./Test.vue.js";
/* empty css          */
import { Common as i } from "./Common.js";
const f = { anchor: { Full: 1, Left: 0, Right: 0 } }, e = /* @__PURE__ */ n({
  __name: "App",
  setup(t) {
    console.log("WORKS!");
    function r() {
      console.log("CLICKED ME");
    }
    return (s, o) => (l(), m("Group", f, [
      _(p, { onClick: r }),
      o[0] || (o[0] = c("TextField", { "placeholder-style": {} }, null, -1)),
      _(d(i).PageOverlay)
    ]));
  }
});
e.__hmrId = "5ddfd01a";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
