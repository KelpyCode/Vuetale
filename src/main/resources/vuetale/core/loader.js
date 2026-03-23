import "vue";
import { hytaleRenderer as r } from "./renderer.js";
import { _ as o } from "./App.vue_vue_type_script_setup_true_lang-C-sdbQam.js";
function s(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), n = /* @__PURE__ */ new Map();
function c(e) {
  const t = p.get(e);
  t && (t.unmount(), p.delete(e), n.delete(e));
}
function a(e) {
  console.log("Creating user app", e);
  const t = r(e).createApp(o);
  return t.provide("appId", e), p.set(e, t), t;
}
function f(e) {
  return p.get(e);
}
function u(e) {
  return n.get(e);
}
function A(e, t) {
  n.set(e, t);
}
s("_vt", {
  createUserApp: a,
  getUserApp: f,
  getUserAppRef: u,
  removeUserApp: c,
  USER_APPS: p,
  USER_APPS_REF: n
});
export {
  p as USER_APPS,
  n as USER_APPS_REF,
  a as createUserApp,
  f as getUserApp,
  u as getUserAppRef,
  A as registerUserAppRef,
  c as removeUserApp
};
