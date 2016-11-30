INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Financial Masters BankAccount', true, 'egf', (select id from eg_module where name = 'Masters'), 'Bank Account', 5);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-BankAccount','/bankaccount/new',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Create Bank Account',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='New-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-BankAccount','/bankaccount/create',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Create-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Create-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-BankAccount','/bankaccount/update',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Update-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Update-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-BankAccount','/bankaccount/view',(select id from eg_module where name='Financial Masters BankAccount' ),1,'View-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='View-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-BankAccount','/bankaccount/edit',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Edit-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Edit-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-BankAccount','/bankaccount/success',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Result-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Result-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-BankAccount','/bankaccount/search/view',(select id from eg_module where name='Financial Masters BankAccount' ),2,'View Bank Account',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View-BankAccount'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-BankAccount','/bankaccount/search/edit',(select id from eg_module where name='Financial Masters BankAccount' ),3,'Update Bank Account',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-BankAccount','/bankaccount/ajaxsearch/view',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Search and View Result-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View Result-BankAccount'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-BankAccount','/bankaccount/ajaxsearch/edit',(select id from eg_module where name='Financial Masters BankAccount' ),1,'Search and Edit Result-BankAccount',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-BankAccount'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit Result-BankAccount'));



