import { defineComponent as n, openBlock as s, createElementBlock as a, createElementVNode as t, toDisplayString as l, createTextVNode as _ } from "vue";
const d = { class: "greetings" }, c = { class: "green" }, e = /* @__PURE__ */ n({
  __name: "HelloWorld",
  props: {
    msg: {}
  },
  setup(r) {
    return (p, o) => (s(), a("div", d, [
      t("h1", c, l(r.msg), 1),
      o[0] || (o[0] = t("h3", null, [
        _(" You’ve successfully created a project with "),
        t("a", {
          href: "https://vite.dev/",
          target: "_blank",
          rel: "noopener"
        }, "Vite"),
        _(" + "),
        t("a", {
          href: "https://vuejs.org/",
          target: "_blank",
          rel: "noopener"
        }, "Vue 3"),
        _(". ")
      ], -1))
    ]));
  }
});
e.__hmrId = "eab776a7";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiSGVsbG9Xb3JsZC52dWUuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
