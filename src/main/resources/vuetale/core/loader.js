import { shallowReactive as s, ref as f } from "vue";
import { hytaleRenderer as i } from "./renderer.js";
import g from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as _, applyStyles as m } from "./styles.js";
function A(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), a = /* @__PURE__ */ new Map(), l = /* @__PURE__ */ new Map(), u = /* @__PURE__ */ new Map(), r = /* @__PURE__ */ new Map();
function R(e, t, n) {
  let o = r.get(e);
  o || (o = s({}), r.set(e, o)), o[t] = n;
}
const c = /* @__PURE__ */ new Map();
function v(e, t) {
  c.set(e, t);
}
function P(e) {
  return c.get(e);
}
function S(e) {
  const t = p.get(e);
  t && (t.unmount(), p.delete(e), a.delete(e), l.delete(e), r.delete(e));
}
function w(e, t) {
  console.log("Creating user app", e, t ?? "(no component)"), _(), r.has(e) || r.set(e, s({}));
  const n = f(t);
  u.set(e, n);
  const o = i(e).createApp(g);
  return o.provide("appId", e), o.provide("componentPathRef", n), p.set(e, o), o;
}
function U(e, t) {
  const n = u.get(e);
  n ? (n.value = t, console.log("navigateTo", e, t)) : console.warn("navigateTo: no app found with id", e);
}
function E(e) {
  return p.get(e);
}
function M(e) {
  return a.get(e);
}
function N(e) {
  return l.get(e);
}
function T(e, t) {
  const n = {
    _vtContainerId: e,
    getRoot: () => t.root
  };
  Object.defineProperty(n, "_vnode", { value: null, writable: !0, enumerable: !1, configurable: !0 }), Object.defineProperty(n, "__vue_app__", { value: null, writable: !0, enumerable: !1, configurable: !0 }), a.set(e, n);
}
A("_vt", {
  applyStyles: m,
  createUserApp: w,
  getUserApp: E,
  getUserAppRef: M,
  registerUserAppRef: T,
  removeUserApp: S,
  navigateTo: U,
  registerComponent: v,
  setAppData: R,
  getRegisteredComponent: P,
  USER_APPS_REF: a,
  USER_APPS_DATA: r
});
export {
  p as USER_APPS,
  r as USER_APPS_DATA,
  l as USER_APPS_META,
  a as USER_APPS_REF,
  w as createUserApp,
  P as getRegisteredComponent,
  E as getUserApp,
  N as getUserAppMeta,
  M as getUserAppRef,
  U as navigateTo,
  v as registerComponent,
  T as registerUserAppRef,
  S as removeUserApp,
  R as setAppData
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7In0=
