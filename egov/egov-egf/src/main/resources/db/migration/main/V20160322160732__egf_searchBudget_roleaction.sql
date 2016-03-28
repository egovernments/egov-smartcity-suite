
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'BudgetSearch-groupedBudgets','/budget/budgetSearch-groupedBudgets.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetSearch-groupedBudgets'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BudgetSearch-groupedBudgets'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetSearch-groupedBudgets'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Approver'),(select id from eg_action where name='BudgetSearch-groupedBudgets'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Search Budget'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Search Budget'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Approver'),(select id from eg_action where name='Search Budget'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='budgetSearch!groupedBudgetDetailList'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Creator'),(select id from eg_action where name='budgetSearch!groupedBudgetDetailList'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Approver'),(select id from eg_action where name='budgetSearch!groupedBudgetDetailList'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajax-budget-loadBudget'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Creator'),(select id from eg_action where name='ajax-budget-loadBudget'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Budget Approver'),(select id from eg_action where name='ajax-budget-loadBudget'));






