import { getCurrentInstance as l, computed as o } from "vue";
const u = (e) => e.replace(/([A-Z])/g, "-$1").toLowerCase(), b = (e, t) => Object.prototype.hasOwnProperty.call(t, e) || Object.prototype.hasOwnProperty.call(t, u(e)), p = (e, t, c) => {
  const a = l(), i = new Set(c?.exclude ?? []), n = o(() => {
    const f = a?.vnode.props ?? {};
    return Object.fromEntries(
      Object.entries(e).filter(([s, r]) => i.has(s) || !b(s, f) || r == null ? !1 : typeof r != "object" || Array.isArray(r) ? !0 : Object.keys(r).length > 0)
    );
  }), d = o(() => ({
    ...n.value,
    ...t
  }));
  return {
    forwardedProps: n,
    forwardedBindings: d
  };
};
export {
  p as useForwardedBindings
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXNlRm9yd2FyZGVkQmluZGluZ3MuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7OyJ9
