-------Deleting data in eg_action and eg_roleaction-----
delete from eg_roleaction where roleid = (select id from eg_role where name='Super User') and actionid = (select id from eg_action where name='CreateCaseType');
delete from eg_action where name ='CreateCaseType' and contextroot='lcms'; 
-------Inserting eg_module-----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'CaseTypeMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Case Type Master', 3);
--------Inserting to eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CasetypeMaster','/casetypemaster/new',(select id from eg_module where name='CaseTypeMaster'),1,'Create Case Type ',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CasetypeMaster','/casetypemaster/create',(select id from eg_module where name='CaseTypeMaster'),1,'Create-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-CasetypeMaster','/casetypemaster/update',(select id from eg_module where name='CaseTypeMaster'),1,'Update-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CasetypeMaster','/casetypemaster/view',(select id from eg_module where name='CaseTypeMaster'),1,'View-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CasetypeMaster','/casetypemaster/edit',(select id from eg_module where name='CaseTypeMaster'),1,'Edit-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CasetypeMaster','/casetypemaster/result',(select id from eg_module where name='CaseTypeMaster'),1,'Result-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CasetypeMaster','/casetypemaster/search/view',(select id from eg_module where name='CaseTypeMaster'),2,'View Case Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CasetypeMaster','/casetypemaster/search/edit',(select id from eg_module where name='CaseTypeMaster'),3,'Modify Case Type',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CasetypeMaster','/casetypemaster/ajaxsearch/view',(select id from eg_module where name='CaseTypeMaster'),1,'Search and View Result-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CasetypeMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CasetypeMaster','/casetypemaster/ajaxsearch/edit',(select id from eg_module where name='CaseTypeMaster'),1,'Search and Edit Result-CasetypeMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CasetypeMaster'));