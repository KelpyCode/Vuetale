const r = globalThis;
r.console = {
  log: (...t) => ktConsole.log(t.map((e) => String(e)).join(" ")),
  info: (...t) => ktConsole.info(t.map((e) => String(e)).join(" ")),
  warn: (...t) => ktConsole.warn(t.map((e) => String(e)).join(" ")),
  error: (...t) => ktConsole.error(t.map((e) => String(e)).join(" ")),
  debug: (...t) => ktConsole.debug(t.map((e) => String(e)).join(" "))
};
(function() {
  r.__vt_timers_installed || (r.__vt_timers_installed = !0, r.__vt_timer_callbacks = /* @__PURE__ */ Object.create(null), r.__vt_nextTimerId = 1, r.setTimeout = function(t, e) {
    const c = r.__vt_nextTimerId++;
    r.__vt_timer_callbacks[c] = function(...o) {
      try {
        t(...o);
      } catch (l) {
        console.error("__vt timer callback error", l);
      }
    };
    try {
      ktTimer.schedule(c, e || 0, !1);
    } catch (o) {
      console.error("ktTimer.schedule failed", o);
    }
    return c;
  }, r.setInterval = function(t, e) {
    const c = r.__vt_nextTimerId++;
    r.__vt_timer_callbacks[c] = function(...o) {
      try {
        t(...o);
      } catch (l) {
        console.error("__vt interval callback error", l);
      }
    };
    try {
      ktTimer.schedule(c, e || 0, !0);
    } catch (o) {
      console.error("ktTimer.schedule failed", o);
    }
    return c;
  }, r.clearTimeout = r.clearInterval = function(t) {
    try {
      ktTimer.cancel(t);
    } catch {
    }
    try {
      delete r.__vt_timer_callbacks[t];
    } catch (e) {
      console.error("delete timer callback failed", e);
    }
  }, r.__vt_clearTimers = function() {
    try {
      const t = Object.keys(r.__vt_timer_callbacks || {});
      for (const e of t) {
        try {
          ktTimer.cancel(Number(e));
        } catch (c) {
          console.error("ktTimer.cancel failed", c);
        }
        try {
          delete r.__vt_timer_callbacks[e];
        } catch (c) {
          console.error("delete timer callback failed", c);
        }
      }
    } catch (t) {
      console.error("__vt_clearTimers error", t);
    }
  }, r.__vt_invokeTimer = function(t) {
    try {
      const e = r.__vt_timer_callbacks[t];
      e && e();
    } catch (e) {
      console.error("__vt invoke error", e);
    }
  });
})();
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZWFybHkuanMiLCJzb3VyY2VzIjpbXSwic291cmNlc0NvbnRlbnQiOltdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OyJ9
