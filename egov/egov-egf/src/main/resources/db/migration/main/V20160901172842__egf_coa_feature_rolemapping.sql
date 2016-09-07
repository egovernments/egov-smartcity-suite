

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create/Modify/View Detailed Code'), id from eg_action where name  in('ChartOfAccounts-editDetailedCode','ChartOfAccounts-viewDetailedCode');
