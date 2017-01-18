INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_DEPARTMENT_CODE', 'Marriage Department Code',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_DEPARTMENT_CODE' and module= (select id from eg_module where name='Marriage Registration')), current_date, 'REV',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_DEFAULT_FUNCTIONARY_CODE', 'Marriage default functionary code',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_DEFAULT_FUNCTIONARY_CODE' and module= (select id from eg_module where name='Marriage Registration')), current_date, '1',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_DEFAULT_FUND_SRC_CODE', 'Marriage default fund src code',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_DEFAULT_FUND_SRC_CODE' and module= (select id from eg_module where name='Marriage Registration')), current_date, '01',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_DEFAULT_FUND_CODE', 'Marriage default fund code',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_DEFAULT_FUND_CODE' and module= (select id from eg_module where name='Marriage Registration')), current_date, '01',0);