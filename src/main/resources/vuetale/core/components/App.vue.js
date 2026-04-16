import { defineComponent as s, ref as a, openBlock as p, createElementBlock as h, createVNode as t, unref as o, withCtx as i, createElementVNode as u, toDisplayString as f } from "vue";
import { Common as _ } from "./Common.js";
import { Core as x } from "./Core.js";
const g = { anchor: { Full: 1, Left: 0, Right: 0 } }, E = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 }
}, e = /* @__PURE__ */ s({
  __name: "App",
  setup(c) {
    console.log("WORKS!");
    function d() {
      console.log("CLICKED ME"), l.value = !l.value;
    }
    const l = a(!1), n = a("nothing yet");
    return (T, r) => (p(), h("Group", g, [
      t(o(_).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": ""
      }, {
        title: i(() => [
          t(o(_).Title, {
            text: l.value ? "Title example" : "Another title"
          }, null, 8, ["text"])
        ]),
        content: i(() => [
          u("Group", E, [
            u("Label", null, "You can also write inside tags " + f(n.value), 1),
            t(o(_).TextButton, {
              text: "Example test",
              onActivating: d,
              anchor: { Height: 20, Top: 80 }
            }),
            t(o(x).TextField, {
              modelValue: n.value,
              "onUpdate:modelValue": r[0] || (r[0] = (m) => n.value = m),
              anchor: { Height: 30, Top: 50 }
            }, null, 8, ["modelValue"])
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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
