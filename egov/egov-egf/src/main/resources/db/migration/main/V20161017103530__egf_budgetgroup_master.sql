insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Budget Group Masters','true',null,
(select id from eg_module where name='Budgeting'),
'Budget Group', 5);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'NewBudgetGroup','/budgetgroup/new',(select id from eg_module where name='Budget Group Masters'),1,'Create Budget Group',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='NewBudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='NewBudgetGroup'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-BudgetGroup','/budgetgroup/create',(select id from eg_module where name='Budget Group Masters' ),1,'Create-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Create-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-BudgetGroup','/budgetgroup/update',(select id from eg_module where name='Budget Group Masters'),1,'Update-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Update-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-BudgetGroup','/budgetgroup/view',(select id from eg_module where name='Budget Group Masters'),1,'View-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='View-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-BudgetGroup','/budgetgroup/edit',(select id from eg_module where name='Budget Group Masters'),1,'Edit-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Edit-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-BudgetGroup','/budgetgroup/result',(select id from eg_module where name='Budget Group Masters'),1,'Result-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Result-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-BudgetGroup','/budgetgroup/search/view',(select id from eg_module where name='Budget Group Masters'),2,'View Budget Group',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and View-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-BudgetGroup','/budgetgroup/search/edit',(select id from eg_module where name='Budget Group Masters'),3,'Modify Budget Group',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and Edit-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-BudgetGroup','/budgetgroup/ajaxsearch/view',(select id from eg_module where name='Budget Group Masters'),1,'Search and View Result-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and View Result-BudgetGroup'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-BudgetGroup','/budgetgroup/ajaxsearch/edit',(select id from eg_module where name='Budget Group Masters'),1,'Search and Edit Result-BudgetGroup',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-BudgetGroup'));
Insert into eg_roleaction values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search and Edit Result-BudgetGroup'));
