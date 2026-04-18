import { ref as l } from "vue";
import { hytaleRenderer as c } from "./renderer.js";
import f from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as i, applyStyles as g } from "./styles.js";
function _(e, t) {
  globalThis[e] = t;
}
const o = /* @__PURE__ */ new Map(), p = /* @__PURE__ */ new Map(), a = /* @__PURE__ */ new Map(), s = /* @__PURE__ */ new Map(), u = /* @__PURE__ */ new Map();
function m(e, t) {
  u.set(e, t);
}
function R(e) {
  return u.get(e);
}
function v(e) {
  const t = o.get(e);
  t && (t.unmount(), o.delete(e), p.delete(e), a.delete(e));
}
function A(e, t) {
  console.log("Creating user app", e, t ?? "(no component)"), i();
  const n = l(t);
  s.set(e, n);
  const r = c(e).createApp(f);
  return r.provide("appId", e), r.provide("componentPathRef", n), o.set(e, r), r;
}
function P(e, t) {
  const n = s.get(e);
  n ? (n.value = t, console.log("navigateTo", e, t)) : console.warn("navigateTo: no app found with id", e);
}
function S(e) {
  return o.get(e);
}
function U(e) {
  return p.get(e);
}
function y(e) {
  return a.get(e);
}
function b(e, t) {
  const n = {
    _vtContainerId: e,
    getRoot: () => t.root
  };
  Object.defineProperty(n, "_vnode", { value: null, writable: !0, enumerable: !1, configurable: !0 }), Object.defineProperty(n, "__vue_app__", { value: null, writable: !0, enumerable: !1, configurable: !0 }), p.set(e, n);
}
_("_vt", {
  applyStyles: g,
  createUserApp: A,
  getUserApp: S,
  getUserAppRef: U,
  registerUserAppRef: b,
  removeUserApp: v,
  navigateTo: P,
  registerComponent: m,
  getRegisteredComponent: R,
  USER_APPS: o,
  USER_APPS_REF: p
});
export {
  o as USER_APPS,
  a as USER_APPS_META,
  p as USER_APPS_REF,
  A as createUserApp,
  R as getRegisteredComponent,
  S as getUserApp,
  y as getUserAppMeta,
  U as getUserAppRef,
  P as navigateTo,
  m as registerComponent,
  b as registerUserAppRef,
  v as removeUserApp
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
