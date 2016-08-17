delete from eg_feature_action where action in (select id from eg_action where url in ('/appConfig/modules', '/appConfig/viewList/'));
delete from eg_roleaction where actionid in (select id from eg_action where url in ('/appConfig/modules', '/appConfig/viewList/'));
delete from eg_action where url = '/appConfig/modules';
delete from eg_action where url = '/appConfig/viewList/';

update eg_action set url ='/app/config/list' where url = '/appConfig/ajax/result';
update eg_action set url ='/app/config/formodule' where url = '/appConfig/ajax-appConfigpopulate';
update eg_action set url ='/app/config/view' where url = '/appConfig/view';
update eg_action set url ='/app/config/create' where url = '/appConfig/create';
update eg_action set url ='/app/config/update' where url = '/appConfig/update';
