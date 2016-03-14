Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'BudgetReAppropriationLoadActuals','/budget/budgetReAppropriation-loadActuals.action',null,(select id from eg_module where name='Budgeting'),1,'BudgetReAppropriationLoadActuals',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetReAppropriationLoadActuals'));


Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'BudgetReAppropriationCreate','/budget/budgetReAppropriation-create.action',null,(select id from eg_module where name='Budgeting'),1,'BudgetReAppropriationCreate',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetReAppropriationCreate'));

Insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'BudgetReAppropriationCreateAndForward','/budget/budgetReAppropriation-createAndForward.action',null,(select id from eg_module where name='Budgeting'),1,'BudgetReAppropriationCreateAndForward',false,'EGF',0,1,current_date,1,current_date,(select id from eg_module where name = 'EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetReAppropriationCreateAndForward'));

