update eg_wf_matrix set validactions ='GENERATE DEMAND NOTICE' where additionalrule  ='CREATEADVERTISEMENT' and currentstate='Commissioner Approved';

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'permitorderreport', '/advertisement/permitOrder', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'permitorderreport', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'permitorderreport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'permitorderreport'));

update eg_action set enabled= false where name='generateDemandAdvertisementTax' and contextroot='adtax';
