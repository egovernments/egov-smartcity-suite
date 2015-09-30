insert into egf_budgetgroup (id,maxcode,mincode,name,description,budgetingtype,accounttype,isactive,updatedtimestamp)values 
(nextval('seq_egf_budgetgroup'),(select id from chartofaccounts where glcode='4120043'),(select id from chartofaccounts where glcode='4120043'),
'4120043-'||(select name from chartofaccounts where glcode='4120043'),'4120043-'||(select name from chartofaccounts where glcode='4120043'),
'DEBIT','REVENUE_EXPENDITURE',true,current_date);

insert into eg_wf_states(id,type,value,createdby,lastmodifiedby,createddate,lastmodifieddate,owner_pos)values(
nextval('seq_eg_wf_states'),'Budget','END',1,1,current_date,current_date,null);

insert into egf_budget(id,name,description,financialyearid,state_id,parent,isactivebudget,updatedtimestamp,isprimarybudget,createdby,lastmodifiedby,isbere,materializedpath)
values(nextval('seq_egf_budgetgroup'),'AP-BUDGET-2015-16','BE Budget 2015-16',(select id from financialyear where financialyear='2015-16'),
(select min(id) from eg_wf_states where type='Budget'),null,1,current_date,1,1,1,'BE','1');

insert into egf_budget(id,name,description,financialyearid,state_id,parent,isactivebudget,updatedtimestamp,isprimarybudget,createdby,lastmodifiedby,isbere,materializedpath)
values(nextval('seq_egf_budgetgroup'),'Works-BUDGET-2015-16','BE Works Budget 2015-16',(select id from financialyear where financialyear='2015-16'),
(select min(id) from eg_wf_states where type='Budget'),(select id from egf_budget where name='AP-BUDGET-2015-16'),1,current_date,1,1,1,'BE','1.1');

insert into eg_wf_states(id,type,value,createdby,lastmodifiedby,createddate,lastmodifieddate,owner_pos)values(
nextval('seq_eg_wf_states'),'BudgetDetail','END',1,1,current_date,current_date,null);

insert into egf_budgetdetail (id,executing_department,function,budget,budgetgroup,originalamount,approvedamount,budgetavailable,
modifieddate,createddate,createdby,state_id,fund,materializedpath,planningpercent) values 
(nextval('seq_egf_budgetdetail'),(select id from eg_department where code='W'),(select id from function where code='202102'),
(select id from egf_budget where name='Works-BUDGET-2015-16'),(select id from egf_budgetgroup where name='4120043-General-Road Widening'),
1000000,1000000,2000000,current_date,current_date,1,(select min(id) from eg_wf_states where type='BudgetDetail'),(select id from fund where code='02'),'1.1.1',200);


