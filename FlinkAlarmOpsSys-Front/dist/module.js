System.register(["./components/logs", "./components/stream", "./components/config", "./components/keywords"], function (_export, _context) {
  "use strict";

  var LogsPageCtrl, StreamPageCtrl, ExampleAppConfigCtrl, KeyWordsCtrl;
  return {
    setters: [function (_componentsLogs) {
      LogsPageCtrl = _componentsLogs.LogsPageCtrl;
    }, function (_componentsStream) {
      StreamPageCtrl = _componentsStream.StreamPageCtrl;
    }, function (_componentsConfig) {
      ExampleAppConfigCtrl = _componentsConfig.ExampleAppConfigCtrl;
    }, function (_componentsKeywords) {
      KeyWordsCtrl = _componentsKeywords.KeyWordsCtrl;
    }],
    execute: function () {
      _export("KeyWordsCtrl", KeyWordsCtrl);

      _export("ConfigCtrl", ExampleAppConfigCtrl);

      _export("StreamPageCtrl", StreamPageCtrl);

      _export("LogsPageCtrl", LogsPageCtrl);
    }
  };
});
//# sourceMappingURL=module.js.map
