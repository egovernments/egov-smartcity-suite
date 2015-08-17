INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'WaterTaxConnectionDataEntry-message', '/application/newConnection-existingMessage', null, 
    (select id from eg_module where name='WaterTaxTransactions'), 2, 'WaterTaxConnectionDataEntry-message', false, 'wtms', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Water Tax Management' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxConnectionDataEntry-message'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxConnectionDataEntry-message'));

