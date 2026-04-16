import { defineComponent as _, useAttrs as m, computed as h, ref as f, openBlock as v, createElementBlock as T, createElementVNode as x, createVNode as y, unref as u, mergeProps as B, nextTick as V } from "vue";
import { Common as g } from "../Common.js";
import { useForwardedBindings as S } from "../../composables/useForwardedBindings.js";
const E = { anchor: { Top: 40 } }, e = /* @__PURE__ */ _({
  inheritAttrs: !1,
  __name: "TextField",
  props: {
    anchor: {},
    autoFocus: { type: Boolean },
    autoScrollDown: { type: Boolean },
    autoSelectAll: { type: Boolean },
    background: {},
    contentHeight: {},
    contentWidth: {},
    decoration: {},
    flexWeight: {},
    hitTestVisible: { type: Boolean },
    isReadOnly: { type: Boolean },
    keepScrollPosition: { type: Boolean },
    maskTexturePath: {},
    maxLength: {},
    mouseWheelScrollBehaviour: {},
    outlineColor: {},
    outlineSize: {},
    overscroll: { type: Boolean },
    padding: {},
    passwordChar: {},
    placeholderStyle: {},
    placeholderText: {},
    elStyle: {},
    textTooltipShowDelay: {},
    textTooltipStyle: {},
    tooltipText: {},
    tooltipTextSpans: {},
    value: {},
    visible: { type: Boolean },
    modelValue: {}
  },
  emits: ["update:modelValue"],
  setup(l, { emit: i }) {
    const a = l, d = m(), s = i, n = h({
      get() {
        return a.modelValue;
      },
      set(o) {
        s("update:modelValue", o);
      }
    }), { forwardedBindings: p } = S(
      a,
      d,
      { exclude: ["modelValue", "value"] }
    ), t = f(!1);
    function c(o) {
      t.value = !0, n.value = o, V(() => {
        t.value = !1;
      });
    }
    return (o, r) => (v(), T("Group", E, [
      r[0] || (r[0] = x("Label", null, "TEST", -1)),
      y(u(g).TextField, B(u(p), {
        onValuechanged: c,
        value: n.value,
        "vt-skip-update": t.value
      }), null, 16, ["value", "vt-skip-update"])
    ]));
  }
});
e.__hmrId = "c161fde0";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGV4dEZpZWxkLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
