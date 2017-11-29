INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ExecuteUpdateSearch', '/application/execute-update/search', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'Execute Water Connection', true,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ExecuteUpdateDisplayResult', '/application/execute-update/result', null,(select id from eg_module where name='WaterTaxTransactions'), 2, 'Execute Water Connection Result', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO EG_ROLE(id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version, internal) VALUES (nextval('seq_eg_role'),'Water Connection Executor','Role for updating water connection execution',now(),1,1,now(),0, false);

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='ExecuteUpdateSearch'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='ExecuteUpdateDisplayResult'));


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Water Connection Execution Update','Water Connection Execution Update Screen',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ExecuteUpdateSearch') ,(select id FROM eg_feature WHERE name = 'Water Connection Execution Update'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ExecuteUpdateDisplayResult') ,(select id FROM eg_feature WHERE name = 'Water Connection Execution Update'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Water Connection Executor') ,(select id FROM eg_feature WHERE name = 'Water Connection Execution Update'));



--rollback delete from eg_feature_role where feature = (select id FROM eg_feature WHERE name = 'Water Connection Execution Update') and role = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Water Connection Execution Update') and action = (select id FROM eg_action  WHERE name = 'ExecuteUpdateSearch');

--rollback delete from eg_feature_action where feature = (select id FROM eg_feature WHERE name = 'Water Connection Execution Update') and action = (select id FROM eg_action  WHERE name = 'ExecuteUpdateDisplayResult');

--rollback delete from eg_feature where name='Water Connection Execution Update' and module=(select id from eg_module  where name = 'Water Tax Management');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ExecuteUpdateSearch') and roleid = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ExecuteUpdateDisplayResult') and roleid = (select id from eg_role where name = 'Water Connection Executor');

--rollback delete from eg_action where name = 'ExecuteUpdateSearch' and parentmodule=(select id from eg_module where name='WaterTaxTransactions');

--rollback delete from eg_action where name = 'ExecuteUpdateDisplayResult' and parentmodule=(select id from eg_module where name='WaterTaxTransactions');

--rollback delete from eg_role where name='Water Connection Executor' and description='Role for updating water connection execution';





