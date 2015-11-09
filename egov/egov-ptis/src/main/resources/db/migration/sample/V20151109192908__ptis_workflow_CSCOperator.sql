DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig  where key_name  = 'PROPERTYTAXDEPARTMENTFORWORKFLOW');
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROPERTYTAXDEPARTMENTFORWORKFLOW'), current_date, 'Revenue,Accounts,Administration',0);

DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig  where key_name  = 'PROPERTYTAXDESIGNATIONFORWORKFLOW');
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROPERTYTAXDESIGNATIONFORWORKFLOW'), current_date, 'Senior Assistant,Junior Assistant',0);