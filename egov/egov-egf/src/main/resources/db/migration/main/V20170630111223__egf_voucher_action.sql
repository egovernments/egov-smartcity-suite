Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
'Create Voucher Rest','/vouchers/_create',null,(select id from EG_MODULE where name = 'Journal Vouchers'),null,
'Create Voucher Rest','false','EGF',0,1,'now()',1,'now()',(select id from eg_module  where name = 'EGF'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Asset Administrator'),
(select id from eg_action where name='Create Voucher Rest'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Create Voucher Rest'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Asset Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='Create Voucher Rest' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'SYSTEM') and actionid = (SELECT id FROM eg_action WHERE name ='Create Voucher Rest' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'Create Voucher Rest' and contextroot = 'EGF';

