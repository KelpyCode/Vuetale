import { shallowReactive as f, ref as A } from "vue";
import { hytaleRenderer as m } from "./renderer.js";
import R from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as v, applyStyles as P } from "./styles.js";
function b(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), s = /* @__PURE__ */ new Map(), i = /* @__PURE__ */ new Map(), u = /* @__PURE__ */ new Map(), r = /* @__PURE__ */ new Map();
function h(e, t, n) {
  let o = r.get(e);
  o || (o = f({}), r.set(e, o));
  let c = n;
  try {
    if (n && typeof n == "object" && "_vtHostFnId" in n) {
      const a = n._vtHostFnId;
      c = function(..._) {
        try {
          return ktBridge.invokeHostCallback(a, ..._);
        } catch (l) {
          throw console.error("invokeHostCallback failed for", a, l), l;
        }
      };
    }
  } catch (a) {
    console.error("setAppData: failed to convert host callback marker", a);
  }
  o[t] = c;
}
const g = /* @__PURE__ */ new Map();
function w(e, t) {
  g.set(e, t);
}
function S(e) {
  return g.get(e);
}
function U(e) {
  const t = p.get(e);
  t && (t.unmount(), p.delete(e), s.delete(e), i.delete(e), r.delete(e));
}
function E(e, t) {
  console.log("Creating user app", e, t ?? "(no component)"), v(), r.has(e) || r.set(e, f({}));
  const n = A(t);
  u.set(e, n);
  const o = m(e).createApp(R);
  return o.provide("appId", e), o.provide("componentPathRef", n), p.set(e, o), o;
}
function M(e, t) {
  const n = u.get(e);
  n ? (n.value = t, console.log("navigateTo", e, t)) : console.warn("navigateTo: no app found with id", e);
}
function T(e) {
  return p.get(e);
}
function y(e) {
  return s.get(e);
}
function N(e) {
  return i.get(e);
}
function d(e, t) {
  const n = {
    _vtContainerId: e,
    getRoot: () => t.root
  };
  Object.defineProperty(n, "_vnode", { value: null, writable: !0, enumerable: !1, configurable: !0 }), Object.defineProperty(n, "__vue_app__", { value: null, writable: !0, enumerable: !1, configurable: !0 }), s.set(e, n);
}
b("_vt", {
  applyStyles: P,
  createUserApp: E,
  getUserApp: T,
  getUserAppRef: y,
  registerUserAppRef: d,
  removeUserApp: U,
  navigateTo: M,
  registerComponent: w,
  setAppData: h,
  getRegisteredComponent: S,
  USER_APPS_REF: s,
  USER_APPS_DATA: r
});
export {
  p as USER_APPS,
  r as USER_APPS_DATA,
  i as USER_APPS_META,
  s as USER_APPS_REF,
  E as createUserApp,
  S as getRegisteredComponent,
  T as getUserApp,
  N as getUserAppMeta,
  y as getUserAppRef,
  M as navigateTo,
  w as registerComponent,
  d as registerUserAppRef,
  U as removeUserApp,
  h as setAppData
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
