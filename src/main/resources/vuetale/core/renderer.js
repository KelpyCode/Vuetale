import { createRenderer as o } from "vue";
const x = (r) => ({ _kt: r }), v = () => ({ _vtText: !0 }), s = (r) => r?._vtText === !0, u = (r) => r?._kt ?? r, f = (r, e, t) => {
  Object.defineProperty(r, e, { value: t, writable: !0, enumerable: !1, configurable: !0 });
}, C = (r) => o({
  createElement: (e) => x(ktBridge.createElement(r, e)),
  // Text / comment nodes have no Kotlin equivalent.
  // Return a sentinel so Vue has a non-null handle; all bridge functions
  // that receive a node check isTextNode and bail out early.
  createText: (e) => v(),
  createComment: (e) => v(),
  setText: (e, t) => {
    if (!s(e))
      return ktBridge.setText(r, u(e), t);
  },
  setElementText: (e, t) => {
    if (!s(e))
      return ktBridge.setElementText(r, u(e), t);
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
    t._vtChildren || f(t, "_vtChildren", []), f(e, "_vtParent", t);
    const l = t._vtChildren.indexOf(e);
    if (l >= 0 && t._vtChildren.splice(l, 1), n == null)
      t._vtChildren.push(e);
    else {
      const _ = t._vtChildren.indexOf(n);
      _ >= 0 ? t._vtChildren.splice(_, 0, e) : t._vtChildren.push(e);
    }
    if (s(e)) return;
    let i = n;
    for (; i != null && s(i); )
      i = i._vtParent?._vtChildren[i._vtParent._vtChildren.indexOf(i) + 1] ?? null;
    ktBridge.insert(r, u(e), u(t), i ? u(i) : null);
  },
  remove: (e) => {
    if (e == null) return;
    const t = e._vtParent;
    if (t?._vtChildren) {
      const l = t._vtChildren.indexOf(e);
      l >= 0 && t._vtChildren.splice(l, 1);
    }
    if (s(e)) return;
    const n = u(e);
    n != null && ktBridge.remove(r, n);
  },
  patchProp: (e, t, n, l) => {
    ktBridge.patchProp(r, u(e), t, n, l);
  }
});
export {
  C as hytaleRenderer
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVuZGVyZXIuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
