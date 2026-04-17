import { createRenderer as v } from "vue";
const o = (r) => ({ _kt: r }), f = () => ({ _vtText: !0 }), u = (r) => r?._vtText === !0, l = (r) => r?._kt ?? r, _ = (r, e, t) => {
  Object.defineProperty(r, e, { value: t, writable: !0, enumerable: !1, configurable: !0 });
}, c = (r) => v({
  createElement: (e) => o(ktBridge.createElement(r, e)),
  // Text / comment nodes have no Kotlin equivalent.
  // Return a sentinel so Vue has a non-null handle; all bridge functions
  // that receive a node check isTextNode and bail out early.
  createText: (e) => f(),
  createComment: (e) => f(),
  setText: (e, t) => {
    if (!u(e))
      return ktBridge.setText(r, l(e), t);
  },
  setElementText: (e, t) => {
    if (!u(e))
      return ktBridge.setElementText(r, l(e), t);
  },
  parentNode: (e) => e == null ? null : e._vtParent ?? null,
  nextSibling: (e) => {
    if (e == null) return null;
    const t = e._vtParent;
    if (!t?._vtChildren) return null;
    const n = t._vtChildren.indexOf(e);
    return n < 0 ? null : t._vtChildren[n + 1] ?? null;
  },
  insert: (e, t, n) => {
    t._vtChildren || _(t, "_vtChildren", []), _(e, "_vtParent", t);
    const i = t._vtChildren.indexOf(e);
    if (i >= 0 && t._vtChildren.splice(i, 1), n == null)
      t._vtChildren.push(e);
    else {
      const s = t._vtChildren.indexOf(n);
      s >= 0 ? t._vtChildren.splice(s, 0, e) : t._vtChildren.push(e);
    }
    u(e) || ktBridge.insert(r, l(e), l(t));
  },
  remove: (e) => {
    if (e == null) return;
    const t = e._vtParent;
    if (t?._vtChildren) {
      const i = t._vtChildren.indexOf(e);
      i >= 0 && t._vtChildren.splice(i, 1);
    }
    if (u(e)) return;
    const n = l(e);
    n != null && ktBridge.remove(r, n);
  },
  patchProp: (e, t, n, i) => {
    ktBridge.patchProp(r, l(e), t, n, i);
  }
});
export {
  c as hytaleRenderer
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVuZGVyZXIuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
