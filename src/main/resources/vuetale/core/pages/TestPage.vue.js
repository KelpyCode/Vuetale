import { defineComponent as B, ref as d, openBlock as m, createElementBlock as h, createVNode as l, unref as t, withCtx as v, createElementVNode as a, toDisplayString as c, createBlock as H, createCommentVNode as L, Fragment as b, renderList as G } from "vue";
import { Common as u } from "../components/Common.js";
import { Core as f } from "../components/core/index.js";
import { useData as i } from "../composables/useData.js";
const U = { anchor: { Full: 1, Left: 0, Right: 0 } }, D = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 },
  background: { Color: "#444444" }
}, R = { "layout-mode": "TopScrolling" }, I = /* @__PURE__ */ B({
  __name: "TestPage",
  setup(S) {
    console.log("WORKS!");
    const T = i("testFn", () => {
    }), x = i("testFn2", (e, n) => {
    });
    function E() {
      console.log("CLICKED ME"), s.value = !s.value, T.value(), console.log("Return of testFn2: ", x.value(123, "hello"));
    }
    const s = d(!1), p = d("nothing yet"), F = i("test"), _ = i("test2"), V = i("counter"), o = d([]);
    function y() {
      o.value.push({ name: `Entry ${o.value.length + 1}`, toggle: !1 });
    }
    function C(e) {
      o.value.splice(e, 1);
    }
    function A(e) {
      if (e > 0) {
        const n = o.value[e];
        o.value[e] = o.value[e - 1], o.value[e - 1] = n;
      }
    }
    return (e, n) => (m(), h("Group", U, [
      l(t(u).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: v(() => [
          l(t(u).Title, {
            class: "",
            text: s.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: v(() => [
          a("Group", D, [
            a("Group", null, [
              a("Label", null, c(p.value) + " | " + c(t(F)) + " " + c(t(_)?.a) + " " + c(t(V)), 1)
            ]),
            n[1] || (n[1] = a("TextField", null, null, -1)),
            l(t(u).TextButton, {
              text: "Example test",
              onActivating: E,
              anchor: { Height: 20, Top: 80 }
            }),
            s.value ? (m(), H(t(f).TextField, {
              key: 0,
              decoration: { Default: {} },
              modelValue: p.value,
              "onUpdate:modelValue": n[0] || (n[0] = (r) => p.value = r),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : L("", !0),
            a("Group", R, [
              (m(!0), h(b, null, G(o.value, (r, g) => (m(), h("Group", {
                key: g,
                anchor: { Height: 200 }
              }, [
                a("Label", null, c(r.name), 1),
                l(t(f).TextField, {
                  modelValue: r.name,
                  "onUpdate:modelValue": (k) => r.name = k
                }, null, 8, ["modelValue", "onUpdate:modelValue"]),
                l(t(u).TextButton, {
                  text: "Remove",
                  onActivating: () => C(g),
                  anchor: { Height: 20, Top: 5, Left: 100 }
                }, null, 8, ["onActivating"]),
                l(t(u).TextButton, {
                  text: "Up",
                  onActivating: () => A(g),
                  anchor: { Height: 20, Top: 5, Left: 160 }
                }, null, 8, ["onActivating"])
              ]))), 128)),
              l(t(u).TextButton, {
                text: "Add Entry",
                onActivating: y,
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
export {
  I as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
