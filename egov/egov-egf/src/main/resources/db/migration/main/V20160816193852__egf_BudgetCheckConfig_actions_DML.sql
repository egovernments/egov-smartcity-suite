INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE') ,'Financials Budget Config', true, NULL, (select id from eg_module where name='Set-up'), 'Budget Control Type', 5);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'New-BudgetCheckConfig','/budgetcheckconfig/new',
(select id from eg_module where name='Financials Budget Config' ),1,'Create/Update-Budget Control Type',true,'EGF',
(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='New-BudgetCheckConfig')); 

Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='New-BudgetCheckConfig'));     



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'update-BudgetCheckConfig','/budgetcheckconfig/update',
(select id from eg_module where name='Financials Budget Config' ),1,'Update-BudgetCheckConfig',false,'EGF',
(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='update-BudgetCheckConfig'));  
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='update-BudgetCheckConfig'));  
 
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'result-BudgetCheckConfig','/budgetcheckconfig/result',
(select id from eg_module where name='Financials Budget Config' ),1,'result-BudgetCheckConfig',false,'EGF',
(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='result-BudgetCheckConfig'));  
 
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='result-BudgetCheckConfig'));  


insert into EGF_BudgetControlType  (id,value,version) values (1,'MANDATORY',0);
 