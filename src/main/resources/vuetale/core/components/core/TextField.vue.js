import { defineComponent as d, useAttrs as _, computed as m, ref as h, openBlock as y, createBlock as f, unref as i, mergeProps as v, nextTick as g } from "vue";
import { Common as x } from "../Common.js";
import { useForwardedBindings as B } from "../../composables/useForwardedBindings.js";
const e = /* @__PURE__ */ d({
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
    onDismissing: { type: Function },
    onFocusGained: { type: Function },
    onFocusLost: { type: Function },
    onRightClicking: { type: Function },
    onValidating: { type: Function },
    onValueChanged: { type: Function },
    modelValue: {}
  },
  emits: ["update:modelValue"],
  setup(n, { emit: u }) {
    const l = n, r = _(), s = u, a = m({
      get() {
        return l.modelValue;
      },
      set(t) {
        s("update:modelValue", t);
      }
    }), { forwardedBindings: p } = B(
      l,
      r,
      { exclude: ["modelValue", "value"] }
    ), o = h(!1);
    function c(t) {
      o.value = !0, a.value = t, g(() => {
        o.value = !1;
      });
    }
    return (t, T) => (y(), f(i(x).TextField, v(i(p), {
      onValueChanged: c,
      value: a.value,
      "vt-skip-update": o.value
    }), null, 16, ["value", "vt-skip-update"]));
  }
});
e.__hmrId = "c161fde0";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGV4dEZpZWxkLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
