INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'PTIS-Data Entry Screen', '/create/createProperty-dataEntry.action', null, 
    (select id from eg_module where name='Existing property'), 1, 'Data Entry Screen', true, 'ptis', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Property Tax' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PTIS-Data Entry Screen'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'PTIS-Data Entry Screen'));
--rollback delete from eg_roleaction where actionid=(select id  from eg_action where name='PTIS-Data Entry Screen');
--rollback delete from eg_action where name='PTIS-Data Entry Screen';

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'PTIS-Create Data Entry Screen', '/create/createProperty-createDataEntry.action', null, 
    (select id from eg_module where name='Existing property'), 1, 'Create Data Entry Screen', false, 'ptis', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Property Tax' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'PTIS-Create Data Entry Screen'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'PTIS-Create Data Entry Screen'));


--rollback delete from eg_roleaction where actionid=(select id  from eg_action where name='PTIS-Create Data Entry Screen');
--rollback delete from eg_action where name='PTIS-Create Data Entry Screen';