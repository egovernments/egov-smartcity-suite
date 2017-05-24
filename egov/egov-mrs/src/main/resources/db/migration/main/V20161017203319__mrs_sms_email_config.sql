INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDSMSFROOMMARRIAGEMODULE', 'SMS Notification for marriage registration',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDSMSFROOMMARRIAGEMODULE' AND MODULE =(select id from eg_module where name='Marriage Registration')),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDEMAILFROOMMARRIAGEMODULE', 'Email Notification for marriage registration',0, (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFROOMMARRIAGEMODULE' AND MODULE =(select id from eg_module where name='Marriage Registration')),current_date, 'YES',0)