insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Asset Transactions','true',null,
(select id from eg_module where name='Asset Management' and parentmodule is null),
'Transactions', 1);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'New-Asset','/asset/new',(select id from eg_module where name='Asset Transactions' 
and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'New-Asset',
true,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where
 name='New-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Asset','/asset/create',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Create-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Asset','/asset/update',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Update-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Asset','/asset/view',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'View-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Asset','/asset/edit',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Edit-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Asset','/asset/result',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Result-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Asset','/asset/search/view',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),2,'View Asset',true,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Asset','/asset/search/edit',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),3,'Edit Asset',true,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Asset','/asset/ajaxsearch/view',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Search and View Result-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Asset'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Asset','/asset/ajaxsearch/edit',(select id from eg_module where name='Asset Transactions' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),1,'Search and Edit Result-Asset',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Asset'));

