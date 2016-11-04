update EG_APPCONFIG_VALUES set value = 'Yes' where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME = 'SECOND_LEVEL_EDIT_MB');

--rollback update EG_APPCONFIG_VALUES set value = 'Yes' where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME = 'SECOND_LEVEL_EDIT_MB');
