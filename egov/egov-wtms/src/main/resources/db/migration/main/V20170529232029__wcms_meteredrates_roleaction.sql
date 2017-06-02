INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'AddWaterChargeRates', '/masters/metered-rate-create', null,(select id from eg_module where name='MeteredRatesMaster'), 1, 'Add Metered Water Charge Rates', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'CreateWaterChargeRates', '/masters/metered-rate-create/', null,(select id from eg_module where name='MeteredRatesMaster'), 4, 'Create Metered Water Charge Rate', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ViewMeteredRateMaster', '/masters/metered-rate-view', null,(select id from eg_module where name='MeteredRatesMaster'), 2, 'View Metered Water Chage Rates', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxSearchMeteredRateMaster', '/masters/metered-rate-search/', null,(select id from eg_module where name='MeteredRatesMaster'), null, 'Ajax Search Metered Rate Master', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='AddWaterChargeRates'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='ViewMeteredRateMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='AjaxSearchMeteredRateMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='CreateWaterChargeRates'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='AddWaterChargeRates'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ViewMeteredRateMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='AjaxSearchMeteredRateMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='CreateWaterChargeRates'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Metered Rate Master','Create Metered Rate Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Metered Rate Master','View Metered Rate Master Screen',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddWaterChargeRates') ,(select id FROM eg_feature WHERE name = 'Create Metered Rate Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMeteredRateMaster') ,(select id FROM eg_feature WHERE name = 'View Metered Rate Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMeteredRateMaster') ,(select id FROM eg_feature WHERE name = 'View Metered Rate Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateWaterChargeRates') ,(select id FROM eg_feature WHERE name = 'Create Metered Rate Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Metered Rate Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'Create Metered Rate Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Metered Rate Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name = 'View Metered Rate Master'));


--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Metered Rate Master') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'View Metered Rate Master') and role = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Metered Rate Master') and action = (select id FROM eg_action  WHERE name = 'ViewMeteredRateMaster');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'View Metered Rate Master') and action = (select id FROM eg_action  WHERE name = 'AjaxSearchMeteredRateMaster');
--rollback delete from eg_feature where name='View Metered Rate Master' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Create Metered Rate Master') and role = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Create Metered Rate Master') and role = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Create Metered Rate Master') and action = (select id FROM eg_action  WHERE name = 'AddWaterChargeRates');
--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Create Metered Rate Master') and action = (select id FROM eg_action  WHERE name = 'CreateWaterChargeRates');
--rollback delete from eg_feature where name='Create Metered Rate Master' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AddWaterChargeRates') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='CreateWaterChargeRates') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewMeteredRateMaster') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxSearchMeteredRateMaster') and roleid = (select id from eg_role where name = 'SYSTEM');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AddWaterChargeRates') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='CreateWaterChargeRates') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ViewMeteredRateMaster') and roleid = (select id from eg_role where name = 'Property Administrator');
--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='AjaxSearchMeteredRateMaster') and roleid = (select id from eg_role where name = 'Property Administrator');

--rollback delete from eg_action where name = 'AddWaterChargeRates' and parentmodule=(select id from eg_module where name='MeteredRatesMaster');
--rollback delete from eg_action where name = 'CreateWaterChargeRates' and parentmodule=(select id from eg_module where name='MeteredRatesMaster');
--rollback delete from eg_action where name = 'ViewMeteredRateMaster' and parentmodule=(select id from eg_module where name='MeteredRatesMaster');
--rollback delete from eg_action where name = 'AjaxSearchMeteredRateMaster' and parentmodule=(select id from eg_module where name='MeteredRatesMaster');
