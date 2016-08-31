--Budget Appropriation Register
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Budget Appropriation Register'), id from eg_action where name  in('EstimateBudgetDetailsByFund','EstimateBudgetDetailsByDept','EstimateBudgetDetailsByFunc');
