-----------------Role action mappings to Create Journal Voucher ----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateJournalVoucherNewForm','/journalvoucher/newform',null,(select id from EG_MODULE where name = 'Journal Vouchers'),1,'New Create Journal Voucher','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateJournalVoucherNewForm' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='CreateJournalVoucherNewForm' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateJournalVoucherCreate','/journalvoucher/create',null,(select id from EG_MODULE where name = 'Journal Vouchers'),1,'Create Journal Voucher','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateJournalVoucherCreate' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='CreateJournalVoucherCreate' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateJournalVoucherSuccess','/journalvoucher/success',null,(select id from EG_MODULE where name = 'Journal Vouchers'),1,'Create Journal Voucher','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateJournalVoucherSuccess' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='CreateJournalVoucherSuccess' and contextroot = 'EGF'));




--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherSuccess' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherSuccess' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateJournalVoucherSuccess' and contextroot = 'EGF';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherCreate' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherCreate' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateJournalVoucherCreate' and contextroot = 'EGF';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherNewForm' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateJournalVoucherNewForm' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateJournalVoucherNewForm' and contextroot = 'EGF';

