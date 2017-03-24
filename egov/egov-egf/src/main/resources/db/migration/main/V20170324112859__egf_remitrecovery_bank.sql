
insert into eg_roleaction values((select id from eg_role where name ='Payment Creator'),(select id from eg_action where name ='GetBankbranchesByBankId'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetBankaccountByBranchId','/common/getbankaccountbybranchid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,null,'false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetBankaccountByBranchId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Payment Creator'),(select id from eg_action where name ='GetBankaccountByBranchId' and contextroot = 'EGF'));

