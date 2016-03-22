delete from eg_roleaction where  roleid in (select id from eg_role where name='Payment Creator') and actionid in (select id from eg_action where name in ('Add/Release Budget ReAppropriation','ajax-budgetReApp-loadBudget','BudgetReAppropriationLoadActuals','BudgetReAppropriationCreate'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='Add/Release Budget ReAppropriation'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='ajax-budgetReApp-loadBudget'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetReAppropriationLoadActuals'));


insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name='Budget Creator'),(select id from eg_action where name='BudgetReAppropriationCreate'));
