import { defineComponent as u, useAttrs as a, useSlots as i, h as m } from "vue";
function c(r, t, e) {
  return u({
    name: t,
    // Let Vue treat incoming attrs as fallthrough while TypeScript keeps T for autocomplete.
    inheritAttrs: !0,
    setup(o) {
      const n = a(), s = i();
      return () => m(
        e,
        // or import Prefab and use the component directly
        {
          ...n,
          // spreads class, style, onClick, data-*, etc.
          ...o,
          // any explicitly passed props (if you declare them later)
          varFrom: r,
          varName: t
        },
        s
        // ← This forwards ALL slots perfectly (default + named)
      );
    }
  });
}
export {
  c as createPrefabComponent
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicHJlZmFiLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
