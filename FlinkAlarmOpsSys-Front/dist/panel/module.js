System.register(["app/plugins/sdk", "../css/example-app.css!"], function (_export, _context) {
  "use strict";

  var PanelCtrl;
  return {
    setters: [function (_appPluginsSdk) {
      PanelCtrl = _appPluginsSdk.PanelCtrl;
    }, function (_cssExampleAppCss) {}],
    execute: function () {
      class ExampleAppPanelCtrl extends PanelCtrl {
        constructor($scope, $injector) {
          super($scope, $injector);
        }

      }

      ExampleAppPanelCtrl.template = '<h2 class="example-app-heading">Example app!</h2>';

      _export("PanelCtrl", ExampleAppPanelCtrl);
    }
  };
});
//# sourceMappingURL=module.js.map
