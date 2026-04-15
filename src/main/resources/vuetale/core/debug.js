const r = _vt.createUserApp.bind(_vt);
_vt.createUserApp = function(e) {
  const n = r(e);
  return n.config.performance = !0, n.config.warnHandler = (t, p, o) => {
    console.warn(`[Vue warn] ${t}
${o}`);
  }, console.log(`[vtdebug] App '${e}' created with performance tracking enabled`), n;
};
const a = {
  /** Dump a JSON summary of all registered app IDs. */
  dumpApps() {
    const e = Array.from(_vt.USER_APPS.keys());
    return JSON.stringify({ count: e.length, apps: e }, null, 2);
  },
  /** List all registered app IDs. */
  appIds() {
    return Array.from(_vt.USER_APPS.keys());
  },
  /** Get the raw Vue App instance for the given ID. */
  getApp(e) {
    return _vt.USER_APPS.get(e);
  },
  /** Enable performance tracking on a specific app by ID. */
  enablePerformance(e) {
    const n = _vt.USER_APPS.get(e);
    n ? (n.config.performance = !0, console.log(`[vtdebug] Performance tracking enabled for '${e}'`)) : console.warn(`[vtdebug] App '${e}' not found`);
  },
  /** Log a summary of all apps to the console. */
  inspect() {
    const e = Array.from(_vt.USER_APPS.keys());
    console.log(`[vtdebug] ${e.length} app(s): ${e.join(", ") || "(none)"}`);
  }
};
globalThis.__vt_debug__ = a;
console.log("[vtdebug] Vuetale debug module loaded. Use globalThis.__vt_debug__ to inspect apps.");
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGVidWcuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==
