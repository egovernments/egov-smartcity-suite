INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Financial Masters Bank', true, 'egf', (select id from eg_module where name = 'Masters'), 'Bank', 5);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Bank','/bank/new',(select id from eg_module where name='Financial Masters Bank' ),1,'Create Bank',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='New-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Bank','/bank/create',(select id from eg_module where name='Financial Masters Bank' ),1,'Create-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Create-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Bank','/bank/update',(select id from eg_module where name='Financial Masters Bank' ),1,'Update-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Update-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Bank','/bank/view',(select id from eg_module where name='Financial Masters Bank' ),1,'View-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='View-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Bank','/bank/edit',(select id from eg_module where name='Financial Masters Bank' ),1,'Edit-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Edit-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Bank','/bank/success',(select id from eg_module where name='Financial Masters Bank' ),1,'Result-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Result-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Bank','/bank/search/view',(select id from eg_module where name='Financial Masters Bank' ),2,'View Bank',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View-Bank'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Bank','/bank/search/edit',(select id from eg_module where name='Financial Masters Bank' ),3,'Update Bank',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Bank','/bank/ajaxsearch/view',(select id from eg_module where name='Financial Masters Bank' ),1,'Search and View Result-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View Result-Bank'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Bank','/bank/ajaxsearch/edit',(select id from eg_module where name='Financial Masters Bank' ),1,'Search and Edit Result-Bank',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Bank'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit Result-Bank'));


