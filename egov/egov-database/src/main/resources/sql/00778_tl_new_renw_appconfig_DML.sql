INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Is Fee For Permanent and Temporary Same','Is Fee For Permanent and Temporary Same',(select id from eg_module where name='Trade License'));


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Is Fee For Permanent and Temporary Same' AND   MODULE =(select id from eg_module where name='Trade License')),current_date,'Y',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'Is Fee For New and Renew Same','Is Fee For New and Renew Same',(select id from eg_module where name='Trade License'));


INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Is Fee For New and Renew Same' AND   MODULE =(select id from eg_module where name='Trade License')),current_date,'Y',0);
