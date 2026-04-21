import { defineComponent as g, ref as s, openBlock as c, createElementBlock as v, createVNode as u, unref as e, withCtx as d, createElementVNode as n, toDisplayString as r, createBlock as E, createCommentVNode as C } from "vue";
import { Common as _ } from "../components/Common.js";
import { Core as I } from "../components/core/index.js";
import { useData as i } from "../composables/useData.js";
const R = { anchor: { Full: 1, Left: 0, Right: 0 } }, V = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 },
  background: { Color: "#444444" }
}, t = /* @__PURE__ */ g({
  __name: "TestPage",
  setup(m) {
    console.log("WORKS!");
    function p() {
      console.log("CLICKED ME"), o.value = !o.value;
    }
    const o = s(!1), a = s("nothing yet"), f = i("test"), h = i("test2"), x = i("counter");
    return (N, l) => (c(), v("Group", R, [
      u(e(_).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: d(() => [
          u(e(_).Title, {
            class: "",
            text: o.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: d(() => [
          n("Group", V, [
            n("Group", null, [
              n("Label", null, "dd " + r(a.value) + " | " + r(e(f)) + " " + r(e(h)?.a) + " " + r(e(x)), 1)
            ]),
            l[1] || (l[1] = n("TextField", null, null, -1)),
            u(e(_).TextButton, {
              text: "Example test",
              onActivating: p,
              anchor: { Height: 20, Top: 80 }
            }),
            o.value ? (c(), E(e(I).TextField, {
              key: 0,
              decoration: { Default: {} },
              modelValue: a.value,
              "onUpdate:modelValue": l[0] || (l[0] = (T) => a.value = T),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : C("", !0)
          ])
        ]),
        _: 1
      })
    ]));
  }
});
t.__hmrId = "265dd60d";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(t.__hmrId, t) || __VUE_HMR_RUNTIME__.reload(t.__hmrId, t));
export {
  t as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
