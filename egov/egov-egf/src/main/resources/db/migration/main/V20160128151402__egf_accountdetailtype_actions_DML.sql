insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Financial Masters Account Entity','true',null,
(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),
'Account Entity', 8);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Accountdetailtype','/accountdetailtype/new',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Add Account Entity',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Accountdetailtype','/accountdetailtype/create',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Create-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Accountdetailtype','/accountdetailtype/update',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Update-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Accountdetailtype','/accountdetailtype/view',(select id from eg_module where name='Financial Masters Account Entity' ),1,'View-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Accountdetailtype','/accountdetailtype/edit',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Edit-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Accountdetailtype','/accountdetailtype/result',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Result-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Accountdetailtype','/accountdetailtype/search/view',(select id from eg_module where name='Financial Masters Account Entity' ),2,'View Account Entity',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Accountdetailtype','/accountdetailtype/search/edit',(select id from eg_module where name='Financial Masters Account Entity' ),3,'Edit Account Entity',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Accountdetailtype','/accountdetailtype/ajaxsearch/view',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Search and View Result-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Accountdetailtype'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Accountdetailtype','/accountdetailtype/ajaxsearch/edit',(select id from eg_module where name='Financial Masters Account Entity' ),1,'Search and Edit Result-Accountdetailtype',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Accountdetailtype'));

