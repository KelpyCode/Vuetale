import { inject as e, computed as i } from "vue";
import { USER_APPS_DATA as p } from "../loader.js";
function s(o, t) {
  const n = e("appId");
  return n || console.warn("[useData] Could not find injected 'appId'. Are you inside a Vuetale app?"), i(() => {
    if (!n) return t;
    const r = p.get(n);
    return r && o in r ? r[o] : t;
  });
}
export {
  s as useData
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXNlRGF0YS5qcyIsInNvdXJjZXMiOltdLCJzb3VyY2VzQ29udGVudCI6W10sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7In0=
