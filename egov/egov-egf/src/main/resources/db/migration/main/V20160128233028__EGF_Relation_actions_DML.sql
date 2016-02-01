
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Financial Masters Supplier','true',null,
(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='EGF' and parentmodule is null)),
'Supplier', 8);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Relation','/relation/new',(select id from eg_module where name='Financial Masters Supplier' ),1,'Add Supplier',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Relation','/relation/create',(select id from eg_module where name='Financial Masters Supplier' ),1,'Create-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Relation','/relation/update',(select id from eg_module where name='Financial Masters Supplier' ),1,'Update-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Relation','/relation/view',(select id from eg_module where name='Financial Masters Supplier' ),1,'View-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Relation','/relation/edit',(select id from eg_module where name='Financial Masters Supplier' ),1,'Edit-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Relation','/relation/result',(select id from eg_module where name='Financial Masters Supplier' ),1,'Result-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Relation','/relation/search/view',(select id from eg_module where name='Financial Masters Supplier' ),2,'View Supplier',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Relation','/relation/search/edit',(select id from eg_module where name='Financial Masters Supplier' ),3,'Edit Supplier',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Relation','/relation/ajaxsearch/view',(select id from eg_module where name='Financial Masters Supplier' ),1,'Search and View Result-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Relation'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Relation','/relation/ajaxsearch/edit',(select id from eg_module where name='Financial Masters Supplier' ),1,'Search and Edit Result-Relation',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Relation'));

