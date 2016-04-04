insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Budget Approver'),(select id from eg_action where name='BudgetUploadReport-getReferenceBudget'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Add/Release Budget ReAppropriation'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-budgetReApp-loadBudget'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='BudgetReAppropriationLoadActuals'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='BudgetReAppropriationCreate'));

