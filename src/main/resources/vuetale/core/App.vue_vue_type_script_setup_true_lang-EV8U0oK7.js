import { defineComponent as a, useAttrs as l, useSlots as C, h as B, openBlock as c, createElementBlock as p, createVNode as u, unref as T } from "vue";
import { _ as x } from "./Test.vue_vue_type_style_index_0_lang-D6-Mdsqp.js";
function o(e, t, n) {
  return a({
    name: t,
    // Let Vue treat incoming attrs as fallthrough while TypeScript keeps T for autocomplete.
    inheritAttrs: !0,
    setup(r) {
      const m = l(), i = C();
      return () => B(
        n,
        // or import Prefab and use the component directly
        {
          ...m,
          // spreads class, style, onClick, data-*, etc.
          ...r,
          // any explicitly passed props (if you declare them later)
          varFrom: e,
          varName: t
        },
        i
        // ← This forwards ALL slots perfectly (default + named)
      );
    }
  });
}
const S = {
  Panel: o("Common.ui", "Panel", "Group"),
  TitleLabel: o("Common.ui", "TitleLabel", "Label"),
  TextButton: o("Common.ui", "TextButton", "TextButton"),
  Button: o("Common.ui", "Button", "Button"),
  CancelTextButton: o("Common.ui", "CancelTextButton", "TextButton"),
  CancelButton: o("Common.ui", "CancelButton", "Button"),
  SmallSecondaryTextButton: o("Common.ui", "SmallSecondaryTextButton", "TextButton"),
  SmallTertiaryTextButton: o("Common.ui", "SmallTertiaryTextButton", "TextButton"),
  SecondaryTextButton: o("Common.ui", "SecondaryTextButton", "TextButton"),
  SecondaryButton: o("Common.ui", "SecondaryButton", "Button"),
  TertiaryTextButton: o("Common.ui", "TertiaryTextButton", "TextButton"),
  TertiaryButton: o("Common.ui", "TertiaryButton", "Button"),
  CloseButton: o("Common.ui", "CloseButton", "Button"),
  CheckBox: o("Common.ui", "CheckBox", "CheckBox"),
  CheckBoxWithLabel: o("Common.ui", "CheckBoxWithLabel", "Group"),
  TextField: o("Common.ui", "TextField", "TextField"),
  NumberField: o("Common.ui", "NumberField", "NumberField"),
  DropdownBox: o("Common.ui", "DropdownBox", "DropdownBox"),
  ContentSeparator: o("Common.ui", "ContentSeparator", "Group"),
  DefaultSpinner: o("Common.ui", "DefaultSpinner", "Sprite"),
  ActionButtonContainer: o("Common.ui", "ActionButtonContainer", "Group"),
  ActionButtonSeparator: o("Common.ui", "ActionButtonSeparator", "Group"),
  VerticalActionButtonSeparator: o("Common.ui", "VerticalActionButtonSeparator", "Group"),
  Subtitle: o("Common.ui", "Subtitle", "Label"),
  Title: o("Common.ui", "Title", "Label"),
  HeaderSearch: o("Common.ui", "HeaderSearch", "Group"),
  HeaderTextButton: o("Common.ui", "HeaderTextButton", "TextButton"),
  HeaderSeparator: o("Common.ui", "HeaderSeparator", "Group"),
  PanelTitle: o("Common.ui", "PanelTitle", "Group"),
  VerticalSeparator: o("Common.ui", "VerticalSeparator", "Group"),
  PanelSeparatorFancy: o("Common.ui", "PanelSeparatorFancy", "Group"),
  Container: o("Common.ui", "Container", "Group"),
  DecoratedContainer: o("Common.ui", "DecoratedContainer", "Group"),
  PageOverlay: o("Common.ui", "PageOverlay", "Group"),
  BackButton: o("Common.ui", "BackButton", "Group")
}, y = /* @__PURE__ */ a({
  __name: "App",
  setup(e) {
    console.log("WORKS!");
    function t() {
      console.log("CLICKED ME");
    }
    return (n, r) => (c(), p("div", null, [
      u(x, { onClick: t }),
      u(T(S).BackButton, { anchor: { Bottom: 123 } })
    ]));
  }
});
export {
  y as _
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQXBwLnZ1ZV92dWVfdHlwZV9zY3JpcHRfc2V0dXBfdHJ1ZV9sYW5nLUVWOFUwb0s3LmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
