
update eg_action set displayname='Execute Non-Metered Water Connection' where displayname='Execute Water Connection' and contextroot='wtms';

update eg_feature set name='Water Charges Non-metered Connection Execution' where name='Water Connection Execution Update' and module=(select id from eg_module where name='Water Tax Management');

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ExecuteMeteredWaterConnectionSearch', '/application/execute-update/search-form', null,(select id from eg_module where name='WaterTaxTransactions'), 2, 'Execute Metered Water Connection', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ExecuteMeteredWaterConnectionResults', '/application/execute-update/search-result', null,(select id from eg_module where name='WaterTaxTransactions'), 2, 'Execute Metered Water Connection Result', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='ExecuteMeteredWaterConnectionSearch'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='ExecuteMeteredWaterConnectionResults'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Water Charges Metered Connection Execution','Water Charges Metered Connection Execution Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ExecuteMeteredWaterConnectionSearch') ,(select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ExecuteMeteredWaterConnectionResults') ,(select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Water Connection Executor') ,(select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution'));


--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution') and role = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution') and action = (select id FROM eg_action  WHERE name = 'ExecuteMeteredWaterConnectionSearch');

--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Water Charges Metered Connection Execution') and action = (select id FROM eg_action  WHERE name = 'ExecuteMeteredWaterConnectionResults');

--rollback delete from eg_feature where name='Water Charges Metered Connection Execution' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ExecuteMeteredWaterConnectionSearch') and roleid = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ExecuteMeteredWaterConnectionResults') and roleid = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_action where name = 'ExecuteMeteredWaterConnectionSearch' and parentmodule=(select id from eg_module where name='WaterTaxTransactions');

--rollback delete from eg_action where name = 'ExecuteMeteredWaterConnectionResults' and parentmodule=(select id from eg_module where name='WaterTaxTransactions');
