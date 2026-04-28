import { defineComponent as c, useAttrs as d, computed as m, ref as y, openBlock as h, createBlock as g, unref as l, mergeProps as v, nextTick as f } from "vue";
import { Common as x } from "../Common.js";
import { useForwardedBindings as B } from "../../composables/useForwardedBindings.js";
const V = /* @__PURE__ */ c({
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
  setup(a, { emit: i }) {
    const o = a, u = d(), r = i, n = m({
      get() {
        return o.modelValue;
      },
      set(e) {
        r("update:modelValue", e);
      }
    }), { forwardedBindings: s } = B(
      o,
      u,
      { exclude: ["modelValue", "value"] }
    ), t = y(!1);
    function p(e) {
      t.value = !0, n.value = e, f(() => {
        t.value = !1;
      });
    }
    return (e, F) => (h(), g(l(x).TextField, v(l(s), {
      onValueChanged: p,
      value: n.value,
      "vt-skip-update": t.value
    }), null, 16, ["value", "vt-skip-update"]));
  }
});
export {
  V as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGV4dEZpZWxkLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
