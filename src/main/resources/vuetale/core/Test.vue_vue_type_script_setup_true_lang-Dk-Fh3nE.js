import { defineComponent as t, openBlock as o, createElementBlock as n, createElementVNode as s } from "vue";
const p = { style: { width: "20", height: "20" } }, m = /* @__PURE__ */ t({
  __name: "Test",
  setup(r) {
    return console.log("Test component"), (c, e) => (o(), n("div", p, [...e[0] || (e[0] = [
      s("span", null, "Test comp", -1)
    ])]));
  }
});
export {
  m as _
};
