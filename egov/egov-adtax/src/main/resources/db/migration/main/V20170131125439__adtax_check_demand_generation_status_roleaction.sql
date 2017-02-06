INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'demandgenerationstatussearch', '/advertisement/demand-status', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 9, 'Check Advertisement Demand Generation Status', true, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatussearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatussearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatussearch'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'demandgenerationstatusrecordsview', '/advertisement/demand-status-records-view/', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 9, 'Check Advertisement Demand Generation Status Records', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatusrecordsview'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatusrecordsview'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'demandgenerationstatusrecordsview'));


--feature for View Demand Generation Status
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Adtax Demand Generation Status','View Advertisement tax Demand Generation Status',(select id from eg_module  where name = 'Advertisement Tax'));

--feature action for View Demand Generation Status
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'demandgenerationstatussearch') ,(select id FROM eg_feature WHERE name = 'View Adtax Demand Generation Status'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'demandgenerationstatusrecordsview') ,(select id FROM eg_feature WHERE name = 'View Adtax Demand Generation Status'));

--feature role for View Demand Generation Status
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Approver') ,(select id FROM eg_feature WHERE name ='View Adtax Demand Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View Adtax Demand Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Creator') ,(select id FROM eg_feature WHERE name ='View Adtax Demand Generation Status'));

--rollback delete from eg_feature_role where role in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Advertisement Tax Approver'),(select id from eg_role where name = 'Advertisement Tax Creator')) and feature = (select id from eg_feature where name = 'View Adtax Demand Generation Status');
--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'demandgenerationstatussearch' and contextroot='adtax') and feature = (select id from eg_feature where name = 'View Adtax Demand Generation Status');
--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'demandgenerationstatusrecordsview' and contextroot='adtax') and feature = (select id from eg_feature where name = 'View Adtax Demand Generation Status');
--rollback delete from eg_feature where name = 'View Adtax Demand Generation Status';

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('demandgenerationstatussearch') and contextroot = 'adtax') and roleid in((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Advertisement Tax Approver'),(select id from eg_role where name = 'Advertisement Tax Creator'));
--rollback delete from eg_action where name='demandgenerationstatussearch' and url = '/advertisement/demand-status' and contextroot = 'adtax';
--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('demandgenerationstatusrecordsview') and contextroot = 'adtax') and roleid in((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Advertisement Tax Approver'),(select id from eg_role where name = 'Advertisement Tax Creator'));
--rollback delete from eg_action where name='demandgenerationstatusrecordsview' and url = '/advertisement/demand-status-records-view/' and contextroot = 'adtax';
