const i = /* @__PURE__ */ new Set();
function l(t, n) {
  return n || JSON.stringify(t);
}
function o(t, n) {
  const e = globalThis.ktBridge;
  if (e && typeof e.registerStyles == "function") {
    e.registerStyles(t, n);
    return;
  }
}
function s(t, n) {
  const e = l(t, n);
  console.log("Applying styles with key:", e, "and styles:", t), !i.has(e) && (i.add(e), o(e, t));
}
function r() {
  const t = globalThis.__vt_pendingStyles__;
  if (!(!t || t.length === 0)) {
    for (const [n, e] of t)
      s(e, n);
    globalThis.__vt_pendingStyles__ = [];
  }
}
globalThis.__vt_applyStyles__ = s;
export {
  s as applyStyles,
  r as flushPendingStyles
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic3R5bGVzLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
