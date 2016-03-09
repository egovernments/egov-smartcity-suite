Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetLoadBeforeUpload','/budget/budgetLoad-beforeUpload.action',(select id from eg_module where name='Budgeting' ),1,'Upload Budget',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetLoadBeforeUpload'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetLoadUpload','/budget/budgetLoad-upload.action',(select id from eg_module where name='Budgeting' ),1,'Upload Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetLoadUpload'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetLoadExport','/budget/budgetLoad-export.action',(select id from eg_module where name='Budgeting' ),1,'Upload Budget',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetLoadExport'));






