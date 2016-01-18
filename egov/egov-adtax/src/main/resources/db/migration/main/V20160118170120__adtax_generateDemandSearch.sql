INSERT INTO eg_action (id, name, url, queryparams, parentmodule,
 ordernumber, displayname, enabled, contextroot, version, createdby, 
 createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
 (nextval('seq_eg_action'), 'generateDemandAdvertisementTax', '/hoarding/generate-search', 
 NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 2, 
 'Generate Demand Advertisement Tax', true, 'adtax', 0, 1, '2015-09-23 15:22:18.385446', 1, 
 '2015-09-23 15:22:18.385446', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, 
enabled, contextroot, version, createdby, createddate, lastmodifiedby, 
lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'generateDemandHoardingResult', '/hoarding/genareteDemand-list', NULL,
  (select id from eg_module where name='AdvertisementTaxTransactions'),
   1, 'GenerateDemand HoardingResult', false, 'adtax', 0, 1, '2015-09-24 18:52:49.736388', 1,
    '2015-09-24 18:52:49.736388', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,
(select id FROM eg_action  WHERE name = 'generateDemandAdvertisementTax'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,
(select id FROM eg_action  WHERE name = 'generateDemandHoardingResult'));



INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, 
displayname, enabled, contextroot, version, createdby, 
createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'updateDemandForAdvertisemnt',
 '/hoarding/generateDemand/', NULL,
  (select id from eg_module where name='AdvertisementTaxTransactions'), 
  2, 'updateDemandForAdvertisemnt', false, 'adtax', 0, 1, '2015-10-01 16:45:31.200641',
   1, '2015-10-01 16:45:31.200641', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,
(select id FROM eg_action  WHERE name = 'updateDemandForAdvertisemnt'));
