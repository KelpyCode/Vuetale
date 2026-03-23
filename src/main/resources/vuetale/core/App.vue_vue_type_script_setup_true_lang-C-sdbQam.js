import { defineComponent as e, openBlock as n, createElementBlock as c, createVNode as t } from "vue";
import { _ as r } from "./Test.vue_vue_type_script_setup_true_lang-Dk-Fh3nE.js";
const m = /* @__PURE__ */ e({
  __name: "App",
  setup(_) {
    console.log("WORKS!");
    function o() {
      console.log("CLICKED ME");
    }
    return (l, p) => (n(), c("div", null, [
      t(r, { onClick: o })
    ]));
  }
});
export {
  m as _
};
