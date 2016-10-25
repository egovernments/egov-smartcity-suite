alter table egf_budgetdetail drop constraint egf_budgetdetail_budget_budgetgroup_scheme_subscheme_functi_key;


alter table egf_budgetdetail add  CONSTRAINT egf_budgetdetail_budget_budgetgroup_function_key UNIQUE (budget, budgetgroup, function, executing_department, fund);


update eg_wf_types set link = '/EGF/budget/budgetProposal-modifyDetailList.action?budgetDetail.budget.id=:ID&mode=approve' where type='BudgetDetail';