insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Budget Definition Masters','true',null,
(select id from eg_module where name='Budgeting'),
'Budget Definition', 6);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'NewBudgetDefinition','/budgetdefinition/new',(select id from eg_module where name='Budget Definition Masters'),1,'Create Budget Definition',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='NewBudgetDefinition'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='NewBudgetDefinition'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Budget','/budgetdefinition/create',(select id from eg_module where name='Budget Definition Masters'),1,'Create-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Create-Budget'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Budget','/budgetdefinition/update',(select id from eg_module where name='Budget Definition Masters'),1,'Update-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Update-Budget')); 

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Budget','/budgetdefinition/view',(select id from eg_module where name='Budget Definition Masters'),1,'View-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='View-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Budget','/budgetdefinition/edit',(select id from eg_module where name='Budget Definition Masters'),1,'Edit-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Budget','/budgetdefinition/result',(select id from eg_module where name='Budget Definition Masters'),1,'Result-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Result-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-Budget','/budgetdefinition/search/view',(select id from eg_module where name='Budget Definition Masters'),2,'View Budget Definition',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and View-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-Budget','/budgetdefinition/search/edit',(select id from eg_module where name='Budget Definition Masters'),3,'Modify Budget Definition',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and Edit-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-Budget','/budgetdefinition/ajaxsearch/view',(select id from eg_module where name='Budget Definition Masters'),1,'Search and View Result-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and View Result-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-Budget','/budgetdefinition/ajaxsearch/edit',(select id from eg_module where name='Budget Definition Masters'),1,'Search and Edit Result-Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Budget'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and Edit Result-Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Ajax-BudgetdefinitionByFinancialYear','/budgetdefinition/ajaxgetparentbyfinancialyear',(select id from eg_module where name='Budget Definition Masters'),1,'Ajax-BudgetByParent',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-BudgetdefinitionByFinancialYear'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Ajax-BudgetdefinitionByFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Ajax-ReferenceByFinancialYear','/budgetdefinition/ajaxgetrefencebudget',(select id from eg_module where name='Budget Definition Masters'),1,'Ajax-BudgetByReference',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-ReferenceByFinancialYear'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Ajax-ReferenceByFinancialYear'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Ajax-ReferenceByBudgetId','/budgetdefinition/ajaxgetdropdownsformodify',(select id from eg_module where name='Budget Definition Masters'),1,'Ajax-BudgetByReference',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-ReferenceByBudgetId'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Ajax-ReferenceByBudgetId'));
