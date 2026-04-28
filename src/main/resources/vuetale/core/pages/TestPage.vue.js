import { defineComponent as L, ref as s, onBeforeUnmount as M, openBlock as v, createElementBlock as h, createVNode as l, unref as t, withCtx as T, createElementVNode as u, toDisplayString as a, createBlock as b, createCommentVNode as G, Fragment as w, renderList as D } from "vue";
import { Common as r } from "../components/Common.js";
import { Core as E } from "../components/core/index.js";
import { useData as i } from "../composables/useData.js";
const S = { anchor: { Full: 1, Left: 0, Right: 0 } }, W = {
  "layout-mode": "Top",
  "flex-weight": 1,
  anchor: { Full: 1 },
  background: { Color: "#444444" }
}, K = { "layout-mode": "TopScrolling" }, m = /* @__PURE__ */ L({
  __name: "TestPage",
  setup(x) {
    console.log("WORKS!");
    const I = i("testFn", () => {
    }), V = i("testFn2", (e, n) => {
    });
    function C() {
      console.log("CLICKED ME"), _.value = !_.value, I.value(), console.log("Return of testFn2: ", V.value(123, "hello"));
    }
    const _ = s(!1), d = s("nothing yet"), U = i("test"), F = i("test2"), H = i("counter"), o = s([]);
    function R() {
      o.value.push({ name: `Entry ${o.value.length + 1}`, toggle: !1 });
    }
    function y(e) {
      o.value.splice(e, 1);
    }
    function A(e) {
      if (e > 0) {
        const n = o.value[e];
        o.value[e] = o.value[e - 1], o.value[e - 1] = n;
      }
    }
    const g = s(0), f = s(0), B = setInterval(() => {
      g.value++;
    }, 1e3), N = setInterval(() => {
      f.value++;
    }, 1500);
    return M(() => {
      clearInterval(B), clearInterval(N);
    }), (e, n) => (v(), h("Group", S, [
      l(t(r).Container, {
        anchor: { Height: 800, Width: 600 },
        "close-button": !0
      }, {
        title: T(() => [
          l(t(r).Title, {
            class: "",
            text: _.value ? "Title example" : "Anotherx title"
          }, null, 8, ["text"])
        ]),
        content: T(() => [
          u("Group", W, [
            u("Group", null, [
              u("Label", null, a(d.value) + " | " + a(t(U)) + " " + a(t(F)?.a) + " " + a(t(H)) + " | Counter1: " + a(g.value) + " | Counter2: " + a(f.value), 1)
            ]),
            n[1] || (n[1] = u("TextField", null, null, -1)),
            l(t(r).TextButton, {
              text: "Example test",
              onActivating: C,
              anchor: { Height: 20, Top: 80 }
            }),
            _.value ? (v(), b(t(E).TextField, {
              key: 0,
              decoration: { Default: {} },
              modelValue: d.value,
              "onUpdate:modelValue": n[0] || (n[0] = (c) => d.value = c),
              anchor: { Height: 120, Top: 10, Width: 200 }
            }, null, 8, ["modelValue"])) : G("", !0),
            u("Group", K, [
              (v(!0), h(w, null, D(o.value, (c, p) => (v(), h("Group", {
                key: p,
                anchor: { Height: 200 }
              }, [
                u("Label", null, a(c.name), 1),
                l(t(E).TextField, {
                  modelValue: c.name,
                  "onUpdate:modelValue": (k) => c.name = k
                }, null, 8, ["modelValue", "onUpdate:modelValue"]),
                l(t(r).TextButton, {
                  text: "Remove",
                  onActivating: () => y(p),
                  anchor: { Height: 20, Top: 5, Left: 100 }
                }, null, 8, ["onActivating"]),
                l(t(r).TextButton, {
                  text: "Up",
                  onActivating: () => A(p),
                  anchor: { Height: 20, Top: 5, Left: 160 }
                }, null, 8, ["onActivating"])
              ]))), 128)),
              l(t(r).TextButton, {
                text: "Add Entry",
                onActivating: R,
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
m.__hmrId = "265dd60d";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(m.__hmrId, m) || __VUE_HMR_RUNTIME__.reload(m.__hmrId, m));
export {
  m as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGVzdFBhZ2UudnVlLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
