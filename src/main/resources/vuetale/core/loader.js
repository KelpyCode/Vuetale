import "vue";
import { hytaleRenderer as r } from "./renderer.js";
import { _ as o } from "./App.vue_vue_type_script_setup_true_lang-ZWKSyU9L.js";
import { flushPendingStyles as s, applyStyles as a } from "./styles.js";
function c(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), n = /* @__PURE__ */ new Map();
function f(e) {
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
function _(e, t) {
  n.set(e, t);
}
c("_vt", {
  applyStyles: a,
  createUserApp: u,
  getUserApp: i,
  getUserAppRef: l,
  removeUserApp: f,
  USER_APPS: p,
  USER_APPS_REF: n
});
export {
  p as USER_APPS,
  n as USER_APPS_REF,
  u as createUserApp,
  i as getUserApp,
  l as getUserAppRef,
  _ as registerUserAppRef,
  f as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
