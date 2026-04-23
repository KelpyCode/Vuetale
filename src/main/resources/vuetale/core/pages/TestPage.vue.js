import { defineComponent as I, ref as p, openBlock as m, createElementBlock as h, createVNode as n, unref as t, withCtx as g, createElementVNode as a, toDisplayString as i, createBlock as A, createCommentVNode as N, Fragment as k, renderList as B } from "vue";
import { Common as u } from "../components/Common.js";
import { Core as f } from "../components/core/index.js";
import { useData as v } from "../composables/useData.js";
const L = { anchor: { Full: 1, Left: 0, Right: 0 } }, M = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 },
  background: { Color: "#444444" }
}, F = { "layout-mode": "TopScrolling" }, c = /* @__PURE__ */ I({
  __name: "TestPage",
  setup(T) {
    console.log("WORKS!");
    function E() {
      console.log("CLICKED ME"), s.value = !s.value;
    }
    const s = p(!1), _ = p("nothing yet"), x = v("test"), V = v("test2"), H = v("counter"), e = p([]);
    function U() {
      e.value.push({ name: `Entry ${e.value.length + 1}`, toggle: !1 });
    }
    function y(o) {
      e.value.splice(o, 1);
    }
    function C(o) {
      if (o > 0) {
        const l = e.value[o];
        e.value[o] = e.value[o - 1], e.value[o - 1] = l;
      }
    }
    return (o, l) => (m(), h("Group", L, [
      n(t(u).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: g(() => [
          n(t(u).Title, {
            class: "",
            text: s.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: g(() => [
          a("Group", M, [
            a("Group", null, [
              a("Label", null, i(_.value) + " | " + i(t(x)) + " " + i(t(V)?.a) + " " + i(t(H)), 1)
            ]),
            l[1] || (l[1] = a("TextField", null, null, -1)),
            n(t(u).TextButton, {
              text: "Example test",
              onActivating: E,
              anchor: { Height: 20, Top: 80 }
            }),
            s.value ? (m(), A(t(f).TextField, {
              key: 0,
              decoration: { Default: {} },
              modelValue: _.value,
              "onUpdate:modelValue": l[0] || (l[0] = (r) => _.value = r),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : N("", !0),
            a("Group", F, [
              (m(!0), h(k, null, B(e.value, (r, d) => (m(), h("Group", {
                key: d,
                anchor: { Height: 200 }
              }, [
                a("Label", null, i(r.name), 1),
                n(t(f).TextField, {
                  modelValue: r.name,
                  "onUpdate:modelValue": (R) => r.name = R
                }, null, 8, ["modelValue", "onUpdate:modelValue"]),
                n(t(u).TextButton, {
                  text: "Remove",
                  onActivating: () => y(d),
                  anchor: { Height: 20, Top: 5, Left: 100 }
                }, null, 8, ["onActivating"]),
                n(t(u).TextButton, {
                  text: "Up",
                  onActivating: () => C(d),
                  anchor: { Height: 20, Top: 5, Left: 160 }
                }, null, 8, ["onActivating"])
              ]))), 128)),
              n(t(u).TextButton, {
                text: "Add Entry",
                onActivating: U,
                anchor: { Height: 20, Top: 10 }
              })
            ])
          ])
        ]),
        _: 1
      })
    ]));
  }
});
c.__hmrId = "265dd60d";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(c.__hmrId, c) || __VUE_HMR_RUNTIME__.reload(c.__hmrId, c));
export {
  c as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
