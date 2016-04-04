Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search BudgetUploadReport','/budgetuploadreport/search',(select id from eg_module where name='Budget Reports'),2,'Budget Upload Report',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search BudgetUploadReport'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search Result-BudgetUploadReport','/budgetuploadreport/ajaxsearch',(select id from eg_module where name='Budget Reports'),1,'Search Result-BudgetUploadReport',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search Result-BudgetUploadReport'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetUploadReport-getReferenceBudget','/budgetuploadreport/ajax/getReferenceBudget',(select id from eg_module where name='Budget Reports'),1,'Search Result-BudgetUploadReport',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetUploadReport-getReferenceBudget'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search Approve Uploaded Budget','/approvebudget/search',(select id from eg_module where name='Budgeting'),2,'Approve Uploaded Budget',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search Approve Uploaded Budget'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Approve Uploaded Budget','/approvebudget/update',(select id from eg_module where name='Budgeting'),1,'Update Approve Uploaded Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Approve Uploaded Budget'));

