------adding new module Bank Accounts Setup
insert into eg_module values(nextval('seq_eg_module'),'Bank Accounts Setup',true,'egf',(select id from eg_module  where name='Masters'),'Bank Accounts Setup',3);

update eg_module set parentmodule =(select id from eg_module where name='Bank Accounts Setup'),ordernumber=1 where name='Financial Masters Bank';
update eg_module set parentmodule =(select id from eg_module where name='Bank Accounts Setup'),ordernumber=2 where name='Financial Masters BankBranch';
update eg_module set parentmodule =(select id from eg_module where name='Bank Accounts Setup'),ordernumber=3 where name='Financial Masters BankAccount';
update eg_module set parentmodule =(select id from eg_module where name='Bank Accounts Setup'),ordernumber=4 where name='Bank Account Cheque';

------spelling correction
update eg_module set displayname ='Bank Branch' where name='Financial Masters BankBranch';

------disabled old bank master
update eg_module set enabled =false where parentmodule in (select id from eg_module where name = 'Chart of Accounts') and  name = 'Bank';

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetBankbranchesByBankId','/common/getbankbranchesbybankid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get Bankbranches By BankId','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetBankbranchesByBankId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'),(select id from eg_action where name ='GetBankbranchesByBankId' and contextroot = 'EGF'));
