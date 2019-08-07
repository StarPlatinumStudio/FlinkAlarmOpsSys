System.register(["./datasource"], function (_export, _context) {
  "use strict";

  var ExampleAppDatasource;
  return {
    setters: [function (_datasource) {
      ExampleAppDatasource = _datasource.ExampleAppDatasource;
    }],
    execute: function () {
      class ExampleAppConfigCtrl {}

      ExampleAppConfigCtrl.template = '<datasource-http-settings current="ctrl.current"></datasource-http-settings>';

      _export("ExampleAppDatasource", ExampleAppDatasource);

      _export("ConfigCtrl", ExampleAppConfigCtrl);
    }
  };
});
//# sourceMappingURL=module.js.map
