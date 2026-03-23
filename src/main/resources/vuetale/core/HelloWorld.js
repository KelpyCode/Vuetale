import { defineComponent as c, openBlock as a, createElementBlock as p, createElementVNode as o, toDisplayString as _, createTextVNode as r } from "vue";
const d = { class: "greetings" }, i = { class: "green" }, f = /* @__PURE__ */ c({
  __name: "HelloWorld",
  props: {
    msg: {}
  },
  setup(t) {
    return (s, e) => (a(), p("div", d, [
      o("h1", i, _(t.msg), 1),
      e[0] || (e[0] = o("h3", null, [
        r(" You’ve successfully created a project with "),
        o("a", {
          href: "https://vite.dev/",
          target: "_blank",
          rel: "noopener"
        }, "Vite"),
        r(" + "),
        o("a", {
          href: "https://vuejs.org/",
          target: "_blank",
          rel: "noopener"
        }, "Vue 3"),
        r(". ")
      ], -1))
    ]));
  }
}), u = (t, s) => {
  const e = t.__vccOpts || t;
  for (const [n, l] of s)
    e[n] = l;
  return e;
}, m = /* @__PURE__ */ u(f, [["__scopeId", "data-v-606fcf89"]]);
export {
  m as default
};
