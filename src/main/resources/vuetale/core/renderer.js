import { createRenderer as g } from "vue";
const o = (t) => g({
  createElement: (e) => ktBridge.createElement(t, e),
  createText: (e) => ktBridge.createText(t, e),
  createComment: (e) => ktBridge.createComment(t, e),
  setText: (e, r) => ktBridge.setText(t, e, r),
  setElementText: (e, r) => ktBridge.setElementText(t, e, r),
  parentNode: (e) => ktBridge.parentNode(t, e),
  nextSibling: (e) => ktBridge.nextSibling(t, e),
  insert: (e, r) => {
    ktBridge.insert(t, e, r);
  },
  remove: (e) => {
    ktBridge.remove(t, e);
  },
  patchProp: (e, r, n, i) => {
    ktBridge.patchProp(t, e, r, n, i);
  }
});
export {
  o as hytaleRenderer
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVuZGVyZXIuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
