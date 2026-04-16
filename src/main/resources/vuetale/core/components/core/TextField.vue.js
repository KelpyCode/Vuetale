import { defineComponent as s, useAttrs as _, computed as m, openBlock as c, createElementBlock as h, createElementVNode as T, createVNode as y, unref as a, mergeProps as v } from "vue";
import { Common as x } from "../Common.js";
const f = { anchor: { Top: 40 } }, e = /* @__PURE__ */ s({
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
  setup(t, { emit: r }) {
    const p = t, u = _(), i = r, l = m({
      get() {
        return p.modelValue;
      },
      set(n) {
        i("update:modelValue", n);
      }
    });
    return (n, o) => (c(), h("Group", f, [
      o[1] || (o[1] = T("Label", null, "TEST", -1)),
      y(a(x).TextField, v({ ...a(u) }, {
        onValuechanged: o[0] || (o[0] = (d) => l.value = d),
        value: l.value
      }), null, 16, ["value"])
    ]));
  }
});
e.__hmrId = "c161fde0";
typeof __VUE_HMR_RUNTIME__ < "u" && (__VUE_HMR_RUNTIME__.createRecord(e.__hmrId, e) || __VUE_HMR_RUNTIME__.reload(e.__hmrId, e));
export {
  e as default
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiVGV4dEZpZWxkLnZ1ZS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
