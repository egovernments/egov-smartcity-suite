delete from eg_roleaction where actionid in (select id from eg_action where name='viewadvertisement');

delete from eg_action where name='viewadvertisement';

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'adtaxview', '/hoarding/adView_/', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 4, 'adtaxView', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'adtaxview'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'adtaxview'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'adtaxview'));
