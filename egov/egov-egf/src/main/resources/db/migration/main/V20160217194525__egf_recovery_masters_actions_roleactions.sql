INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Recovery', true, 'egf', (select id from eg_module where name = 'Masters'), 'Recovery', 4);

Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxGetAccountCodes','/recovery/ajax/getAccountCodes',null,(select id from eg_module where name='Recovery'),1,'AjaxGetAccountCodes',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxGetAccountCodes'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Recovery','/recovery/new',(select id from eg_module where name='Recovery' ),1,'Create Recovery',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Recovery','/recovery/create',(select id from eg_module where name='Recovery' ),1,'Create-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Recovery','/recovery/update',(select id from eg_module where name='Recovery' ),1,'Update-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Recovery','/recovery/view',(select id from eg_module where name='Recovery' ),1,'View-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Recovery','/recovery/edit',(select id from eg_module where name='Recovery' ),1,'Edit-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Recovery','/recovery/result',(select id from eg_module where name='Recovery' ),1,'Result-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Recovery','/recovery/search/view',(select id from eg_module where name='Recovery' ),2,'View Recovery',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Recovery','/recovery/search/edit',(select id from eg_module where name='Recovery' ),3,'Edit Recovery',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Recovery','/recovery/ajaxsearch/view',(select id from eg_module where name='Recovery' ),1,'Search and View Result-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Recovery'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Recovery','/recovery/ajaxsearch/edit',(select id from eg_module where name='Recovery' ),1,'Search and Edit Result-Recovery',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Recovery'));


