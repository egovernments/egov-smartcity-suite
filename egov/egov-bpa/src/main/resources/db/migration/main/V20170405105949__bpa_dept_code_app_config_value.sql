INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'BPA_DEPARTMENT_CODE', 'Bpa Department Code',0, (select id from eg_module where name='BPA')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BPA_DEPARTMENT_CODE' and module= (select id from eg_module where name='BPA')), current_date, 'REV',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'BPA_DEFAULT_FUNCTIONARY_CODE', 'Bpa default functionary code',0, (select id from eg_module where name='BPA')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BPA_DEFAULT_FUNCTIONARY_CODE' and module= (select id from eg_module where name='BPA')), current_date, '1',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'BPA_DEFAULT_FUND_SRC_CODE', 'Bpa default fund src code',0, (select id from eg_module where name='BPA')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BPA_DEFAULT_FUND_SRC_CODE' and module= (select id from eg_module where name='BPA')), current_date, '01',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'BPA_DEFAULT_FUND_CODE', 'Bpa default fund code',0, (select id from eg_module where name='BPA')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BPA_DEFAULT_FUND_CODE' and module= (select id from eg_module where name='BPA')), current_date, '01',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDSMSFROOMBPAMODULE', 'SMS Notification for Bpa',0, (select id from eg_module where name='BPA'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDSMSFROOMBPAMODULE' AND MODULE =(select id from eg_module where name='BPA')),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDEMAILFROOMBPAMODULE', 'Email Notification for Bpa',0, (select id from eg_module where name='BPA'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFROOMBPAMODULE' AND MODULE =(select id from eg_module where name='BPA')),current_date, 'YES',0)

