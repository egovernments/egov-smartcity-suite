update eg_action set url='/city/setup' where name='CitySetup';
delete from eg_feature_action where action=(select id from eg_action where name='CitySetupUpdate');
delete from eg_roleaction where actionid=(select id from eg_action where name='CitySetupUpdate');
delete from eg_action where name='CitySetupUpdate';