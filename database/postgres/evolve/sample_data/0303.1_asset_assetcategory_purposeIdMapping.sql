#UP
update eg_appconfig_values set value=(select id from EGF_ACCOUNTCODE_PURPOSE where name='ASSET_CATEGORY_ACCOUNT_CODE') where key_id = 
(select id from eg_appconfig where key_name='ASSET_ACCOUNT_CODE_PURPOSEID');
update chartofaccounts set purposeid=(select id from EGF_ACCOUNTCODE_PURPOSE where name='ASSET_CATEGORY_ACCOUNT_CODE') where glcode='410';
#DOWN
update eg_appconfig_values set value=19 where key_id = 
(select id from eg_appconfig where key_name='ASSET_ACCOUNT_CODE_PURPOSEID');
update chartofaccounts set purposeid=19 where glcode='410';
