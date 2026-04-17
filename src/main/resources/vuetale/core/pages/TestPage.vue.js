import { defineComponent as p, ref as u, openBlock as c, createElementBlock as h, createVNode as n, unref as o, withCtx as i, createElementVNode as r, toDisplayString as f, createBlock as x, createCommentVNode as T } from "vue";
import { Common as _ } from "../components/Common.js";
import { Core as g } from "../components/Core.js";
const v = { anchor: { Full: 1, Left: 0, Right: 0 } }, E = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 }
}, e = /* @__PURE__ */ p({
  __name: "TestPage",
  setup(d) {
    console.log("WORKS!");
    function m() {
      console.log("CLICKED ME"), t.value = !t.value;
    }
    const t = u(!1), l = u("nothing yet");
    return (C, a) => (c(), h("Group", v, [
      n(o(_).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: i(() => [
          n(o(_).Title, {
            class: "",
            text: t.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: i(() => [
          r("Group", E, [
            r("Group", null, [
              r("Label", null, "abcd " + f(l.value), 1)
            ]),
            n(o(_).TextButton, {
              text: "Example test",
              onActivating: m,
              anchor: { Height: 20, Top: 80 }
            }),
            t.value ? (c(), x(o(g).TextField, {
              key: 0,
              modelValue: l.value,
              "onUpdate:modelValue": a[0] || (a[0] = (s) => l.value = s),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : T("", !0)
          ])
        ]),
        _: 1
      })
    ]));
  }
});
e.__hmrId = "265dd60d";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
