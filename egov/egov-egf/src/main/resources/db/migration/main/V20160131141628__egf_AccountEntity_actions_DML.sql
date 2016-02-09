delete from eg_roleaction where actionid in (select id from eg_action where url like '%userDefinedCodes%');
delete from eg_action where  url like '%userDefinedCodes%';
update eg_module set name='Financials Masters User Defined Codes' where name='User Defined Codes';

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-AccountEntity','/accountentity/new',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Add User Defined Code',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-AccountEntity','/accountentity/create',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Create-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-AccountEntity','/accountentity/update',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Update-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-AccountEntity','/accountentity/view',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'View-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-AccountEntity','/accountentity/edit',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'View-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-AccountEntity','/accountentity/result',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Result AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-AccountEntity','/accountentity/search/view',(select id from eg_module where name='Financials Masters User Defined Codes'),2,'View User Defined Code',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-AccountEntity','/accountentity/search/edit',(select id from eg_module where name='Financials Masters User Defined Codes'),3,'Edit User Defined Code',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-AccountEntity','/accountentity/ajaxsearch/view',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Search and View Result-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-AccountEntity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-AccountEntity','/accountentity/ajaxsearch/edit',(select id from eg_module where name='Financials Masters User Defined Codes'),1,'Search and Edit Result-AccountEntity',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-AccountEntity'));


-- adding to Financial Admin
Insert into eg_roleaction (select (select id from eg_role where name='Financial Administrator'), id from eg_action where url like '%/accountentity/%');
Insert into eg_roleaction (select (select id from eg_role where name='Financial Administrator'), id from eg_action where url like '%/relation/%');
Insert into eg_roleaction (select (select id from eg_role where name='Financial Administrator'), id from eg_action where url like '%/accountdetailtype/%');
