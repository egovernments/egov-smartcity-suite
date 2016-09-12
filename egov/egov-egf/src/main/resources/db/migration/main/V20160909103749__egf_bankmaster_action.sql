
insert into eg_module values(nextval('seq_eg_module'),'Bank',true,null,(select id from eg_module  where name='Chart of Accounts'),'Bank',4);

update eg_action set parentmodule=(select id from eg_module where name='Bank'),ordernumber=1
where name='BankMaster';
update eg_action set parentmodule=(select id from eg_module where name='Bank'),ordernumber=2
where name='BankMasterModify';

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'BankMasterView','/masters/bank.action','mode=VIEW',
(select id from eg_module where name='Bank'),
3,'View Bank','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='BankMasterView'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='BankMasterView'));
