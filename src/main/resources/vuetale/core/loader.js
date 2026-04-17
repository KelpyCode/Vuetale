import "vue";
import { hytaleRenderer as p } from "./renderer.js";
import u from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as a, applyStyles as l } from "./styles.js";
function s(e, t) {
  globalThis[e] = t;
}
const r = /* @__PURE__ */ new Map(), n = /* @__PURE__ */ new Map();
function c(e) {
  const t = r.get(e);
  t && (t.unmount(), r.delete(e), n.delete(e));
}
function f(e) {
  console.log("Creating user app", e), a();
  const t = p(e).createApp(u);
  return t.provide("appId", e), r.set(e, t), t;
}
function i(e) {
  return r.get(e);
}
function m(e) {
  return n.get(e);
}
function g(e, t) {
  const o = {
    _vtContainerId: e,
    getRoot: () => t.root
  };
  Object.defineProperty(o, "_vnode", { value: null, writable: !0, enumerable: !1, configurable: !0 }), Object.defineProperty(o, "__vue_app__", { value: null, writable: !0, enumerable: !1, configurable: !0 }), n.set(e, o);
}
s("_vt", {
  applyStyles: l,
  createUserApp: f,
  getUserApp: i,
  getUserAppRef: m,
  registerUserAppRef: g,
  removeUserApp: c,
  USER_APPS: r,
  USER_APPS_REF: n
});
export {
  r as USER_APPS,
  n as USER_APPS_REF,
  f as createUserApp,
  i as getUserApp,
  m as getUserAppRef,
  g as registerUserAppRef,
  c as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
