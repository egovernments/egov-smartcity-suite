
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BudgetReject','/budgetapproval/reject',
(select id from eg_module where name='Budget Approval Screen'),1,'Budget Reject',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetReject'));