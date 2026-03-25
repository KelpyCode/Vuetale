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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiSGVsbG9Xb3JsZC5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9

export const styles = {
  "h1[data-v-606fcf89]": {
    "fontWeight": 500,
    "fontSize": "2.6rem",
    "position": "relative",
    "top": "-10px"
  },
  "h3[data-v-606fcf89]": {
    "fontSize": "1.2rem"
  },
  ".greetings h1[data-v-606fcf89],.greetings h3[data-v-606fcf89]": {
    "textAlign": "center"
  },
  "@media (min-width:1024px)": {
    ".greetings h1[data-v-606fcf89],.greetings h3[data-v-606fcf89]": {
      "textAlign": "left"
    }
  }
};

;(function(){var __s={
  "h1[data-v-606fcf89]": {
    "fontWeight": 500,
    "fontSize": "2.6rem",
    "position": "relative",
    "top": "-10px"
  },
  "h3[data-v-606fcf89]": {
    "fontSize": "1.2rem"
  },
  ".greetings h1[data-v-606fcf89],.greetings h3[data-v-606fcf89]": {
    "textAlign": "center"
  },
  "@media (min-width:1024px)": {
    ".greetings h1[data-v-606fcf89],.greetings h3[data-v-606fcf89]": {
      "textAlign": "left"
    }
  }
};var __fn=globalThis.__vt_applyStyles__;if(typeof __fn==="function"){__fn(__s,"HelloWorld.css");}else{(globalThis.__vt_pendingStyles__=globalThis.__vt_pendingStyles__||[]).push(["HelloWorld.css",__s]);}})();
