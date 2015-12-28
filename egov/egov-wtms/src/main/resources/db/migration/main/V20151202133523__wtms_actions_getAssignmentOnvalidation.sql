INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'positionOnvalidatebyposId', '/ajaxconnection/assignmentByPositionId', null,
     (select id from eg_module where name='WaterTaxTransactions'), null, 'positionOnvalidatebyposId', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where (name) = 'Property Verifier'),(select id FROM eg_action 
 WHERE NAME = 'positionOnvalidatebyposId' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where (name) = 'Property Administrator'),(select id FROM eg_action 
 WHERE NAME = 'positionOnvalidatebyposId' and CONTEXTROOT='wtms'));
 
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where (name) = 'ULB Operator'),(select id FROM eg_action 
 WHERE NAME = 'positionOnvalidatebyposId' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where (name) = 'CSC Operator'),(select id FROM eg_action 
 WHERE NAME = 'positionOnvalidatebyposId' and CONTEXTROOT='wtms'));