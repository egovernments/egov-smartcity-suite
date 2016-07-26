INSERT into eg_module values (nextval('SEQ_EG_ACTION'),'CommonUOMCategory',true,'common',(select id from eg_module where name='Common-Masters' and contextroot='common'),'UOM Category',1);

INSERT into eg_module values (nextval('SEQ_EG_ACTION'),'Common-Masters',true,'common',(select id from eg_module where name='Common' and parentmodule is null),'Masters',1);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'New-UOMCategory','/uomcategory/new',(select id from eg_module where name='Common-Masters' ),1,'Create UOM Category',true,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Create-UOMCategory','/uomcategory/create',(select id from eg_module where name='Common-Masters'),1,'Create-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Update-UOMCategory','/uomcategory/update',(select id from eg_module where name='Common-Masters' ),1,'Update-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'View-UOMCategory','/uomcategory/view',(select id from eg_module where name='Common-Masters'),1,'View-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Edit-UOMCategory','/uomcategory/edit',(select id from eg_module where name='Common-Masters' ),1,'Edit-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Result-UOMCategory','/uomcategory/result',(select id from eg_module where name='Common-Masters'),1,'Result-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Search and View-UOMCategory','/uomcategory/search/view',(select id from eg_module where name='Common-Masters'),2,'View UOM Category',true,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Search and Edit-UOMCategory','/uomcategory/search/edit',(select id from eg_module where name='Common-Masters'),3,'Modify UOM Category',true,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Search and View Result-UOMCategory','/uomcategory/ajaxsearch/view',(select id from eg_module where name='Common-Masters'),1,'Search and View Result-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-UOMCategory'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application,createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-UOMCategory','/uomcategory/ajaxsearch/edit',(select id from eg_module where name='Common-Masters' ),1,'Search and Edit Result-UOMCategory',false,'common',(select id from eg_module where name='Common' and parentmodule is null),1,now(),1,now());
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-UOMCategory'));