INSERT into eg_module values (nextval('SEQ_EG_ACTION'),'CommonUOM',true,'common',(select id from eg_module where name='Common-Masters' and contextroot='common'),'Unit of Measurement',2);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-UOM','/uom/new',(select id from eg_module where name='CommonUOM'),1,'Create Unit of Measurement',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-UOM','/uom/create',(select id from eg_module where name='CommonUOM'),1,'Create-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-UOM','/uom/update',(select id from eg_module where name='CommonUOM'),1,'Update-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-UOM','/uom/view',(select id from eg_module where name='CommonUOM'),1,'View-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-UOM','/uom/edit',(select id from eg_module where name='CommonUOM'),1,'Edit-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-UOM','/uom/result',(select id from eg_module where name='CommonUOM'),1,'Result-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-UOM','/uom/search/view',(select id from eg_module where name='CommonUOM'),2,'View Unit of Measurement',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-UOM','/uom/search/edit',(select id from eg_module where name='CommonUOM'),3,'Modify Unit of Measurement',true,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-UOM','/uom/ajaxsearch/view',(select id from eg_module where name='CommonUOM'),1,'Search and View Unit of Measurement',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-UOM'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-UOM','/uom/ajaxsearch/edit',(select id from eg_module where name='CommonUOM'),1,'Search and Edit Result-UOM',false,'common',(select id from eg_module where name='Common' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-UOM'));

update eg_module set parentmodule = (select id from eg_module where name='Common-Masters' and contextroot='common') where name='CommonUOMCategory';


update eg_action set parentmodule = (select id from eg_module where name='CommonUOMCategory') where parentmodule=(select id from eg_module where name='Common-Masters');