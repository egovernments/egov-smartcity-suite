--Transactions

------Auto Generate Demand
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 
'Auto Generate Demand For Sewerage', '/transactions/generatedemand', null, (select id from eg_module where name = 'SewerageTransactions'), 3, 'Auto Generate Demand For Sewerage', true, 'stms', 0, 1, now(), 1, now(), 
(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'Auto Generate Demand For Sewerage'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'Auto Generate Demand For Sewerage'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('Auto Generate Demand For Sewerage') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Super User'));
--rollback delete from eg_action where name in ('Auto Generate Demand For Sewerage') and contextroot = 'stms';

-----Check Demand Status
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 
'SewerageDemandStatusSearch', '/transactions/seweragedemand-status', null, (select id from eg_module where name = 'SewerageTransactions'), 4, 'Check Demand Generation Status For Sewerage', true, 'stms', 0, 1, now(), 1, now(), 
(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SewerageDemandStatusSearch'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'SewerageDemandStatusSearch'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('SewerageDemandStatusSearch') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Sewerage Tax Approver'));
--rollback delete from eg_action where name in ('SewerageDemandStatusSearch') and contextroot = 'stms';
-------Sewerage Records status view
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 
'SewerageDemandRecordsView', '/transactions/seweragedemand-status-records-view/', null, (select id from eg_module where name = 'SewerageTransactions'), 3, 'Check Demands Records Status', false, 'stms', 0, 1, now(), 1, now(), 
(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SewerageDemandRecordsView'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'SewerageDemandRecordsView'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('SewerageDemandRecordsView') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Super User'));
--rollback delete from eg_action where name in ('SewerageDemandRecordsView') and contextroot = 'stms';

--------Sewerage Demand 
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 
'SewerageDemandBatch', '/transactions/seweragedemand-batch', null, (select id from eg_module where name = 'SewerageTransactions'), 4, 'DemandGenerationBatchStatus ', false, 'stms', 0, 1, now(), 1, now(), 
(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SewerageDemandBatch'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'SewerageDemandBatch'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('SewerageDemandBatch') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Super User'));
--rollback delete from eg_action where name in ('SewerageDemandBatch') and contextroot = 'stms';



-----feature--------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Check Sewerage Demand Generation','Check Sewerage Tax Demand Generation Status',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Auto Generate Demand For Sewerage','Auto Generate Demand For Sewerage',(select id from eg_module  where name = 'Sewerage Tax Management'));

--------------------------------------------------------------feature action and role--------------------

----Auto generate Demands-------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Auto Generate Demand For Sewerage') ,(select id FROM eg_feature WHERE name = 'Auto Generate Demand For Sewerage'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Auto Generate Demand For Sewerage'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Auto Generate Demand For Sewerage'));

---Check Demand   status---------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageDemandStatusSearch') ,(select id FROM eg_feature WHERE name = 'Check Sewerage Demand Generation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Check Sewerage Demand Generation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Check Sewerage Demand Generation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageDemandBatch') ,(select id FROM eg_feature WHERE name = 'Check Sewerage Demand Generation'));
