import "vue";
import { hytaleRenderer as r } from "./renderer.js";
import s from "./components/App.vue.js";
import { flushPendingStyles as a, applyStyles as c } from "./styles.js";
function u(e, t) {
  globalThis[e] = t;
}
const n = /* @__PURE__ */ new Map(), o = /* @__PURE__ */ new Map();
function l(e) {
  const t = n.get(e);
  t && (t.unmount(), n.delete(e), o.delete(e));
}
function f(e) {
  console.log("Creating user app", e), a();
  const t = r(e).createApp(s);
  return t.provide("appId", e), n.set(e, t), t;
}
function i(e) {
  return n.get(e);
}
function _(e) {
  return o.get(e);
}
function g(e, t) {
  const p = {
    _vnode: null,
    __vue_app__: null,
    _vtContainerId: e,
    getRoot: () => t.root
  };
  o.set(e, p);
}
u("_vt", {
  applyStyles: c,
  createUserApp: f,
  getUserApp: i,
  getUserAppRef: _,
  registerUserAppRef: g,
  removeUserApp: l,
  USER_APPS: n,
  USER_APPS_REF: o
});
export {
  n as USER_APPS,
  o as USER_APPS_REF,
  f as createUserApp,
  i as getUserApp,
  _ as getUserAppRef,
  g as registerUserAppRef,
  l as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
