import { defineComponent as p, ref as i, openBlock as u, createElementBlock as h, createVNode as n, unref as o, withCtx as c, createElementVNode as r, toDisplayString as f, createBlock as x, createCommentVNode as g } from "vue";
import { Common as _ } from "./Common.js";
import { Core as v } from "./Core.js";
const E = { anchor: { Full: 1, Left: 0, Right: 0 } }, T = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 }
}, e = /* @__PURE__ */ p({
  __name: "App",
  setup(d) {
    console.log("WORKS!");
    function m() {
      console.log("CLICKED ME"), t.value = !t.value;
    }
    const t = i(!1), l = i("nothing yet");
    return (C, a) => (u(), h("Group", E, [
      n(o(_).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": ""
      }, {
        title: c(() => [
          n(o(_).Title, {
            text: t.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: c(() => [
          r("Group", T, [
            r("Group", null, [
              r("Label", null, "You can also write inside tags " + f(l.value), 1)
            ]),
            n(o(_).TextButton, {
              text: "Example test",
              onActivating: m,
              anchor: { Height: 20, Top: 80 }
            }),
            t.value ? (u(), x(o(v).TextField, {
              key: 0,
              modelValue: l.value,
              "onUpdate:modelValue": a[0] || (a[0] = (s) => l.value = s),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : g("", !0)
          ])
        ]),
        _: 1
      })
    ]));
  }
});
e.__hmrId = "5ddfd01a";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
