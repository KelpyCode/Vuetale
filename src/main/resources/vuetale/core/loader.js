import { shallowReactive as f, ref as m } from "vue";
import { hytaleRenderer as A } from "./renderer.js";
import R from "./components/App.vue.js";
/* empty css                    */
import { flushPendingStyles as v, applyStyles as P } from "./styles.js";
function b(e, t) {
  globalThis[e] = t;
}
const p = /* @__PURE__ */ new Map(), s = /* @__PURE__ */ new Map(), i = /* @__PURE__ */ new Map(), u = /* @__PURE__ */ new Map(), r = /* @__PURE__ */ new Map();
function h(e, t, o) {
  let n = r.get(e);
  n || (n = f({}), r.set(e, n));
  let c = o;
  try {
    if (o && typeof o == "object" && "_vtHostFnId" in o) {
      const a = o._vtHostFnId;
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
  n[t] = c;
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
  const o = m(t);
  u.set(e, o);
  const n = A(e).createApp(R);
  return n.provide("appId", e), n.provide("componentPathRef", o), p.set(e, n), n;
}
function M(e, t) {
  const o = u.get(e);
  o ? (o.value = t, console.log("navigateTo", e, t)) : console.warn("navigateTo: no app found with id", e);
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
function C(e, t) {
  const o = {
    _vtContainerId: e,
    getRoot: () => t.root
  };
  Object.defineProperty(o, "_vnode", { value: null, writable: !0, enumerable: !1, configurable: !0 }), Object.defineProperty(o, "__vue_app__", { value: null, writable: !0, enumerable: !1, configurable: !0 }), s.set(e, o);
}
b("_vt", {
  applyStyles: P,
  createUserApp: E,
  getUserApp: T,
  getUserAppRef: y,
  registerUserAppRef: C,
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
  C as registerUserAppRef,
  U as removeUserApp,
  h as setAppData
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9hZGVyLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
