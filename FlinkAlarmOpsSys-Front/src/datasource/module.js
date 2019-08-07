import {ExampleAppDatasource} from  './datasource';

class ExampleAppConfigCtrl {}
ExampleAppConfigCtrl.template = '<datasource-http-settings current="ctrl.current"></datasource-http-settings>';

export {
  ExampleAppDatasource,
  ExampleAppConfigCtrl as ConfigCtrl
};