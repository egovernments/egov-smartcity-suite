INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Financial Masters Fund', true, 'egf', (select id from eg_module where name = 'Masters'), 'Fund', 5);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Fund','/fund/new',(select id from eg_module where name='Financial Masters Fund' ),1,'Add-Fund',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Fund','/fund/create',(select id from eg_module where name='Financial Masters Fund' ),1,'Create-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Fund','/fund/update',(select id from eg_module where name='Financial Masters Fund' ),1,'Update-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Fund','/fund/view',(select id from eg_module where name='Financial Masters Fund' ),1,'View-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Fund','/fund/edit',(select id from eg_module where name='Financial Masters Fund' ),1,'Edit-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Fund','/fund/result',(select id from eg_module where name='Financial Masters Fund' ),1,'Result-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Fund','/fund/search/view',(select id from eg_module where name='Financial Masters Fund' ),2,'View-Fund',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Fund','/fund/search/edit',(select id from eg_module where name='Financial Masters Fund' ),3,'Edit-Fund',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Fund','/fund/ajaxsearch/view',(select id from eg_module where name='Financial Masters Fund' ),1,'Search and View Result-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Fund'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Fund','/fund/ajaxsearch/edit',(select id from eg_module where name='Financial Masters Fund' ),1,'Search and Edit Result-Fund',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Fund'));



delete from eg_roleaction where actionid in (select id from eg_action where name in ('Fund-Create','Fund','Fund-View','Fund-Modify','Fund-BeforeModify','FundSearch','FundEdit'));

delete from eg_action where name in ('Fund-Create','Fund','Fund-View','Fund-Modify','Fund-BeforeModify','FundSearch','FundEdit');