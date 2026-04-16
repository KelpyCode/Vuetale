import { createRenderer as s } from "vue";
const k = (t) => ({ _kt: t }), u = () => ({ _vtText: !0 }), i = (t) => t?._vtText === !0, r = (t) => t?._kt ?? t, c = (t) => s({
  createElement: (e) => k(ktBridge.createElement(t, e)),
  // Text / comment nodes have no Kotlin equivalent.
  // Return a sentinel so Vue has a non-null handle; all bridge functions
  // that receive a node check isTextNode and bail out early.
  createText: (e) => u(),
  createComment: (e) => u(),
  setText: (e, n) => {
    if (!i(e))
      return ktBridge.setText(t, r(e), n);
  },
  setElementText: (e, n) => {
    if (!i(e))
      return ktBridge.setElementText(t, r(e), n);
  },
  parentNode: (e) => i(e) ? null : ktBridge.parentNode(t, r(e)),
  nextSibling: (e) => i(e) ? null : ktBridge.nextSibling(t, r(e)),
  insert: (e, n) => {
    i(e) || ktBridge.insert(t, r(e), r(n));
  },
  remove: (e) => {
    i(e) || ktBridge.remove(t, r(e));
  },
  patchProp: (e, n, x, o) => {
    ktBridge.patchProp(t, r(e), n, x, o);
  }
});
export {
  c as hytaleRenderer
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVuZGVyZXIuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
