INSERT into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER)
VALUES (NEXTVAL('SEQ_EG_MODULE'),'Budget Approval Screen','true',null,
(select id from eg_module where name='Budgeting'),
'Budget Approval', 6);


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'NewBudgetApproval','/budgetapproval/new',(select id from eg_module where name='Budget Approval Screen'),1,'Budget Approval',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='NewBudgetApproval'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetSearch','/budgetapproval/search',
(select id from eg_module where name='Budget Approval Screen'),1,'Budget Search',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetSearch'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetApprove','/budgetapproval/approve',
(select id from eg_module where name='Budget Approval Screen'),1,'Budget Approve',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetApprove'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Success-Approval','/budgetapproval/success',(select id from eg_module where name='Budget Approval Screen'),1,'Success-Approval',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Success-Approval'));

