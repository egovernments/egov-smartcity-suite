INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'demandnoticereport', '/advertisement/demandNotice', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'demandnoticereport', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'demandnoticereport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'demandnoticereport'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'HoardingLegacyview', '/hoarding/viewLegacy', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'View Legacy Hoarding', false, 'adtax', 0, 1, '2015-10-01 16:45:31.200641', 1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'HoardingLegacyview'));