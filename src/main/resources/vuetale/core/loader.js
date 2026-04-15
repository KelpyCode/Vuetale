import "vue";
import { hytaleRenderer as r } from "./renderer.js";
import s from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as u, applyStyles as a } from "./styles.js";
function c(e, t) {
  globalThis[e] = t;
}
const n = /* @__PURE__ */ new Map(), o = /* @__PURE__ */ new Map();
function l(e) {
  const t = n.get(e);
  t && (t.unmount(), n.delete(e), o.delete(e));
}
function f(e) {
  console.log("Creating user app", e), u();
  const t = r(e).createApp(s);
  return t.provide("appId", e), n.set(e, t), t;
}
function i(e) {
  return n.get(e);
}
function _(e) {
  return o.get(e);
}
function m(e, t) {
  const p = {
    _vnode: null,
    __vue_app__: null,
    _vtContainerId: e,
    getRoot: () => t.root
  };
  o.set(e, p);
}
c("_vt", {
  applyStyles: a,
  createUserApp: f,
  getUserApp: i,
  getUserAppRef: _,
  registerUserAppRef: m,
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
  m as registerUserAppRef,
  l as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
