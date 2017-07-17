insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetAccountDetailTypesByGlcodeId','/common/getaccountdetailtypesbyglcodeid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get AccountDetailTypes By GlcodeId','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='GetAllAccountCodes' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='GetAllAccountCodes' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Creator'),(select id from eg_action where name ='GetEntitesByAccountDetailType' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Voucher Approver'),(select id from eg_action where name ='GetEntitesByAccountDetailType' and contextroot = 'EGF'));


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Approver') and actionid = (SELECT id FROM eg_action WHERE name ='GetAllAccountCodes' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetAllAccountCodes' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Approver') and actionid = (SELECT id FROM eg_action WHERE name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Voucher Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetAccountDetailTypesByGlcodeId' and contextroot = 'EGF';

