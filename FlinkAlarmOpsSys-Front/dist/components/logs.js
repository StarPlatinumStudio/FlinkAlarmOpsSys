System.register(["app/core/config", "./clippy.css!", "./clippy"], function (_export, _context) {
  "use strict";

  var config;
  return {
    setters: [function (_appCoreConfig) {
      config = _appCoreConfig.default;
    }, function (_clippyCss) {}, function (_clippy) {}],
    execute: function () {
      //import css
      //import js
      class LogsPageCtrl {
        constructor() {
          this.name = config.bootData.user.name;
          var c = 'hihijhijhjk';
          this.n1 = 'AA';
          this.n2 = c;
        }

      }

      _export("LogsPageCtrl", LogsPageCtrl);

      LogsPageCtrl.templateUrl = 'components/logs.html';
    }
  };
});
//# sourceMappingURL=logs.js.map
