import { defineComponent as s, ref as i, openBlock as p, createElementBlock as h, createVNode as t, unref as o, withCtx as u, createElementVNode as _, toDisplayString as f } from "vue";
import { Common as r } from "./Common.js";
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
    const l = i(!1), n = i("nothing yet");
    return (T, a) => (p(), h("Group", g, [
      t(o(r).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": ""
      }, {
        title: u(() => [
          t(o(r).Title, {
            text: l.value ? "Title example" : "Another title"
          }, null, 8, ["text"])
        ]),
        content: u(() => [
          _("Group", E, [
            _("Group", null, [
              _("Label", null, "You can also write inside tags " + f(n.value), 1)
            ]),
            t(o(r).TextButton, {
              text: "Example test",
              onActivating: d,
              anchor: { Height: 20, Top: 80 }
            }),
            t(o(x).TextField, {
              modelValue: n.value,
              "onUpdate:modelValue": a[0] || (a[0] = (m) => n.value = m),
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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
