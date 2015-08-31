
UPDATE eg_appconfig_values set value = (select id from egf_accountcode_purpose where name='Fixed Assets') where key_id=(select id from eg_appconfig where key_name='ASSET_ACCOUNT_CODE_PURPOSEID');
UPDATE eg_appconfig_values set value = (select id from egf_accountcode_purpose where name='Revaluation Reserve Account') where key_id=(select id from eg_appconfig where key_name='REVALUATION_RESERVE_ACCOUNT_PURPOSEID');
UPDATE eg_appconfig_values set value = (select id from egf_accountcode_purpose where name='Depreciation Expense Account') where key_id=(select id from eg_appconfig where key_name='DEPRECIATION_EXPENSE_ACCOUNT_PURPOSEID');
UPDATE eg_appconfig_values set value = (select id from egf_accountcode_purpose where name='Accumulated Depreciation') where key_id=(select id from eg_appconfig where key_name='ACCUMULATED_DEPRECIATION_PURPOSEID');
