
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'PUBLIC'),(select id FROM eg_action  WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms'));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxCreateNewConnectionNewForm')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxAjaxCheckPrimaryConnection')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('ajax edit watertax donation amount')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('categorytypebypropertytypeajax')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('usagetypebypropertytypeajax')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('pipesizesbypropertytypeajax')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxCreateNewConnection')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('PrintMeesevaReceipt')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('watertaxAcknowledgement')));

 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('AddtionalWaterConnection')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxAddConnection')));

 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxChangeOfUseApplication')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterTaxCreateChangeOfUseApplication')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('CreateChangeOfUseConnection')));


 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('CreateAdditionalConnection')));


 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('WaterReConnectionApplication')));
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('createReConnection')));

 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('CloseWaterConnectionApplication'))); 
 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('createClosureConnection')));


 INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'PUBLIC'),(select id from eg_action where name in('collectTaxForwatrtax')));






