Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'serviceListNotMappedToAccount','/receipts/ajaxBankRemittance-serviceListNotMappedToAccount.action',null,(select id from eg_module where name='Receipt Services'),1,'serviceListNotMappedToAccount',false,'collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='serviceListNotMappedToAccount'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'bankBranchsByBankForReceiptPayments','/receipts/ajaxBankRemittance-bankBranchsByBankForReceiptPayments.action',null,(select id from eg_module where name='Receipt Services'),1,'bankBranchsByBankForReceiptPayments',false,'collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='bankBranchsByBankForReceiptPayments'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'bankAccountByBankBranch','/receipts/ajaxBankRemittance-bankAccountByBankBranch.action',null,(select id from eg_module where name='Receipt Services'),1,'bankAccountByBankBranch',false,'collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='bankAccountByBankBranch'));

