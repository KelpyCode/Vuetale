import { defineComponent as A, ref as h, openBlock as _, createElementBlock as v, createVNode as l, unref as t, withCtx as g, createElementVNode as a, toDisplayString as c, createBlock as N, createCommentVNode as k, Fragment as B, renderList as L } from "vue";
import { Common as u } from "../components/Common.js";
import { Core as f } from "../components/core/index.js";
import { useData as i } from "../composables/useData.js";
const M = { anchor: { Full: 1, Left: 0, Right: 0 } }, b = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 },
  background: { Color: "#444444" }
}, G = { "layout-mode": "TopScrolling" }, s = /* @__PURE__ */ A({
  __name: "TestPage",
  setup(T) {
    console.log("WORKS!");
    const E = i("testFn", () => {
    }), x = i("testFn2", (e, n) => {
    });
    function V() {
      console.log("CLICKED ME"), m.value = !m.value, E.value(), console.log("Return of testFn2: ", x.value(123, "hello"));
    }
    const m = h(!1), d = h("nothing yet"), F = i("test"), H = i("test2"), R = i("counter"), o = h([]);
    function U() {
      o.value.push({ name: `Entry ${o.value.length + 1}`, toggle: !1 });
    }
    function y(e) {
      o.value.splice(e, 1);
    }
    function C(e) {
      if (e > 0) {
        const n = o.value[e];
        o.value[e] = o.value[e - 1], o.value[e - 1] = n;
      }
    }
    return (e, n) => (_(), v("Group", M, [
      l(t(u).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: g(() => [
          l(t(u).Title, {
            class: "",
            text: m.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: g(() => [
          a("Group", b, [
            a("Group", null, [
              a("Label", null, c(d.value) + " | " + c(t(F)) + " " + c(t(H)?.a) + " " + c(t(R)), 1)
            ]),
            n[1] || (n[1] = a("TextField", null, null, -1)),
            l(t(u).TextButton, {
              text: "Example test",
              onActivating: V,
              anchor: { Height: 20, Top: 80 }
            }),
            m.value ? (_(), N(t(f).TextField, {
              key: 0,
              decoration: { Default: {} },
              modelValue: d.value,
              "onUpdate:modelValue": n[0] || (n[0] = (r) => d.value = r),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : k("", !0),
            a("Group", G, [
              (_(!0), v(B, null, L(o.value, (r, p) => (_(), v("Group", {
                key: p,
                anchor: { Height: 200 }
              }, [
                a("Label", null, c(r.name), 1),
                l(t(f).TextField, {
                  modelValue: r.name,
                  "onUpdate:modelValue": (I) => r.name = I
                }, null, 8, ["modelValue", "onUpdate:modelValue"]),
                l(t(u).TextButton, {
                  text: "Remove",
                  onActivating: () => y(p),
                  anchor: { Height: 20, Top: 5, Left: 100 }
                }, null, 8, ["onActivating"]),
                l(t(u).TextButton, {
                  text: "Up",
                  onActivating: () => C(p),
                  anchor: { Height: 20, Top: 5, Left: 160 }
                }, null, 8, ["onActivating"])
              ]))), 128)),
              l(t(u).TextButton, {
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
s.__hmrId = "265dd60d";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(s.__hmrId, s) || __VUE_HMR_RUNTIME__.reload(s.__hmrId, s));
export {
  s as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
