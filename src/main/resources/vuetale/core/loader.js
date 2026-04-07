import "vue";
import { hytaleRenderer as r } from "./renderer.js";
import o from "./components/App.vue.js";
import { flushPendingStyles as s, applyStyles as c } from "./styles.js";
function f(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), n = /* @__PURE__ */ new Map();
function a(e) {
  const t = p.get(e);
  t && (t.unmount(), p.delete(e), n.delete(e));
}
function u(e) {
  console.log("Creating user app", e), s();
  const t = r(e).createApp(o);
  return t.provide("appId", e), p.set(e, t), t;
}
function i(e) {
  return p.get(e);
}
function l(e) {
  return n.get(e);
}
function R(e, t) {
  const container = {
    _vnode: null,
    __vue_app__: null,
    _vtContainerId: e,
    getRoot: function() { return t.root; }
  };
  n.set(e, container);
}
f("_vt", {
  applyStyles: c,
  createUserApp: u,
  getUserApp: i,
  getUserAppRef: l,
  registerUserAppRef: R,
  removeUserApp: a,
  USER_APPS: p,
  USER_APPS_REF: n
});
export {
  p as USER_APPS,
  n as USER_APPS_REF,
  u as createUserApp,
  i as getUserApp,
  l as getUserAppRef,
  R as registerUserAppRef,
  a as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
