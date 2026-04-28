import { defineComponent as n, openBlock as s, createElementBlock as l, createElementVNode as e, toDisplayString as a, createTextVNode as t } from "vue";
const c = { class: "greetings" }, p = { class: "green" }, u = /* @__PURE__ */ n({
  __name: "HelloWorld",
  props: {
    msg: {}
  },
  setup(r) {
    return (i, o) => (s(), l("div", c, [
      e("h1", p, a(r.msg), 1),
      o[0] || (o[0] = e("h3", null, [
        t(" You’ve successfully created a project with "),
        e("a", {
          href: "https://vite.dev/",
          target: "_blank",
          rel: "noopener"
        }, "Vite"),
        t(" + "),
        e("a", {
          href: "https://vuejs.org/",
          target: "_blank",
          rel: "noopener"
        }, "Vue 3"),
        t(". ")
      ], -1))
    ]));
  }
});
export {
  u as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiSGVsbG9Xb3JsZC52dWUuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
