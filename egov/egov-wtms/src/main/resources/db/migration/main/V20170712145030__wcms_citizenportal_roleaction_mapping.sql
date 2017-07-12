----------------------------------new connection-------------------------------------------------------------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='WaterTaxCreateNewConnectionNewForm') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='WaterTaxAjaxCheckPrimaryConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='ajax edit watertax donation amount') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='categorytypebypropertytypeajax') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='usagetypebypropertytypeajax') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='pipesizesbypropertytypeajax') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='WaterTaxCreateNewConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='watertaxAcknowledgement') and roleid in (select id from eg_role where name='CITIZEN');
-----------------Additional Connection-------------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='CreateAdditionalConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='AddtionalWaterConnection') and roleid in (select id from eg_role where name='CITIZEN');
---------------------change of use-----------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='CreateChangeOfUseConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='WaterTaxChangeOfUseApplication') and roleid in (select id from eg_role where name='CITIZEN');
------------------------------closure connection---------------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='createClosureConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='CloseWaterConnectionApplication') and roleid in (select id from eg_role where name='CITIZEN');
---------------------------Reconnection---------------
delete from eg_roleaction where actionid in (select id from eg_action where name ='createReConnection') and roleid in (select id from eg_role where name='CITIZEN');
delete from eg_roleaction where actionid in (select id from eg_action where name ='WaterReConnectionApplication') and roleid in (select id from eg_role where name='CITIZEN');

----------------------------------new connection-------------------------------------------------------------------
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='WaterTaxCreateNewConnectionNewForm'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='WaterTaxAjaxCheckPrimaryConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='ajax edit watertax donation amount'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='categorytypebypropertytypeajax'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='usagetypebypropertytypeajax'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='pipesizesbypropertytypeajax'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='WaterTaxCreateNewConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='watertaxAcknowledgement'),(select id from eg_role where name='CITIZEN'));
-----------------Additional Connection-------------------
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CreateAdditionalConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='AddtionalWaterConnection'),(select id from eg_role where name='CITIZEN'));
---------------------change of use-----------------
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CreateChangeOfUseConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='WaterTaxChangeOfUseApplication'),(select id from eg_role where name='CITIZEN'));
------------------------------closure connection---------------------
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='createClosureConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CloseWaterConnectionApplication'),(select id from eg_role where name='CITIZEN'));
---------------------------Reconnection---------------
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='createReConnection'),(select id from eg_role where name='CITIZEN'));
insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='WaterReConnectionApplication'),(select id from eg_role where name='CITIZEN'));
