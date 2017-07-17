
insert into eg_roleaction (select (select id from eg_role where name ='Budget Creator') ,id from eg_action where url like '%budget%Proposal%');

update eg_wf_types set link='/EGF/budget/budgetProposal-modifyBudgetDetailList.action?budgetDetail.budget.id=:ID&mode=approve' where type='BudgetDetail';

update eg_action set url='/budget/budgetProposal-update.action' where name='BudgetProposal-update';
