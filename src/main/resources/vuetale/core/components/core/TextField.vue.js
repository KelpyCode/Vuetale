import { defineComponent as _, useAttrs as m, computed as h, ref as y, openBlock as f, createElementBlock as v, createElementVNode as g, createVNode as T, unref as r, mergeProps as V, nextTick as x } from "vue";
import { Common as B } from "../Common.js";
import { useForwardedBindings as F } from "../../composables/useForwardedBindings.js";
const S = { anchor: { Top: 40 } }, e = /* @__PURE__ */ _({
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
    const l = n, s = m(), p = u, a = h({
      get() {
        return l.modelValue;
      },
      set(o) {
        p("update:modelValue", o);
      }
    }), { forwardedBindings: d } = F(
      l,
      s,
      { exclude: ["modelValue", "value"] }
    ), t = y(!1);
    function c(o) {
      t.value = !0, a.value = o, x(() => {
        t.value = !1;
      });
    }
    return (o, i) => (f(), v("Group", S, [
      i[0] || (i[0] = g("Label", null, "TEST", -1)),
      T(r(B).TextField, V(r(d), {
        onValuechanged: c,
        value: a.value,
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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGV4dEZpZWxkLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
