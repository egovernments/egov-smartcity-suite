
alter table egadtax_demandgenerationlogdetail alter column detail type character varying(600);


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'demandbatchsearch', '/advertisement/demand-batch', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 9, 'View Advertisement Demand batch status', false, 'adtax', 0, 1, now(), 1, now(), (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'demandbatchsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'demandbatchsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'demandbatchsearch'));

--feature for  View Advertisement Demand batch status


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'demandbatchsearch') ,(select id FROM eg_feature WHERE name = 'View Advertisement Demand batch status'));



