INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, parentmodule, ORDERNUMBER, DISPLAYNAME, ENABLED, CONTEXTROOT, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'Generate Collection Bill', '/collection/collectPropertyTax-generateBill.action', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'Existing property'),
0, 'Generate Collection Bill', false, 'ptis', now(), 1, now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action  WHERE name = 'Generate Collection Bill' and CONTEXTROOT='ptis'));

--ROLLBACK DELETE FROM EG_ROLEACTION WHERE actionid=(SELECT id FROM eg_action WHERE name = 'Generate Collection Bill');
--ROLLBACK DELETE FROM EG_ACTION WHERE name='Generate Collection Bill';
