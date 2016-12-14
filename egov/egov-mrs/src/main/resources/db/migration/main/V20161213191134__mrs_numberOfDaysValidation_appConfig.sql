INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGEREGISTRATION_DAYS_VALIDATION', 'validate No of Days allowed to register marriage',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGEREGISTRATION_DAYS_VALIDATION' and module= (select id from eg_module where name='Marriage Registration')), current_date, 'NO',0);
