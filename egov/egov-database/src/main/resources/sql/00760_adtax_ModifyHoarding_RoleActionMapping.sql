INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'HoardingSearchUpdate', '/hoarding/search-for-update', null, 
    (select id from eg_module where name='AdvertisementTaxTransactions'), 2, 'Update Hoarding', true, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'HoardingSearchUpdate'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'HoardingUpdate', '/hoarding/update', null, 
    (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Hoarding Update', false, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'HoardingUpdate'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'HoardingView', '/hoarding/view', null, 
    (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Hoarding View', false, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'HoardingView'));
