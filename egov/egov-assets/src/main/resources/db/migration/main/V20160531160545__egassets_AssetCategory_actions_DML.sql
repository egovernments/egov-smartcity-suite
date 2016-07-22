insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Asset Masters','true',null,
(select id from eg_module where name='Asset Management' and parentmodule is null),
'Masters', 1);

insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Asset Masters Category','true',null,
(select id from eg_module where name='Asset Masters' and parentmodule=(select id from eg_module where name='Asset Management' and parentmodule is null)),
'Category', 1);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'New-AssetCategory','/assetcategory/new',(select id from eg_module where name='Asset Masters Category' ),1,'Add-AssetCategory',true,'egassets',(select id from eg_module where
name='Asset Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'Create-AssetCategory','/assetcategory/create',(select id from eg_module where name='Asset Masters Category' ),1,'Create-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-AssetCategory','/assetcategory/update',(select id from eg_module where name='Asset Masters Category' ),1,'Update-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-AssetCategory','/assetcategory/view',(select id from eg_module where name='Asset Masters Category' ),1,'View-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-AssetCategory','/assetcategory/edit',(select id from eg_module where name='Asset Masters Category' ),1,'Edit-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-AssetCategory','/assetcategory/result',(select id from eg_module where name='Asset Masters Category' ),1,'Result-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-AssetCategory','/assetcategory/search/view',(select id from eg_module where name='Asset Masters Category' ),2,'View AssetCategory',true,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-AssetCategory','/assetcategory/search/edit',(select id from eg_module where name='Asset Masters Category' ),3,'Edit AssetCategory',true,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-AssetCategory','/assetcategory/ajaxsearch/view',(select id from eg_module where name='Asset Masters Category' ),1,'Search and View Result-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-AssetCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-AssetCategory','/assetcategory/ajaxsearch/edit',(select id from eg_module where name='Asset Masters Category' ),1,'Search and Edit Result-AssetCategory',false,'egassets',(select id from eg_module where name='Asset Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-AssetCategory'));

