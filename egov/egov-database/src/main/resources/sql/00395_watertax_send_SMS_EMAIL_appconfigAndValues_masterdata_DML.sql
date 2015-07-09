
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'SENDSMSFORWATERTAX', 'SMS Notification for Water Tax module is enabled or not',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDSMSFORWATERTAX'),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'SENDEMAILFORWATERTAX', 'Email Notification for Water Tax module is enabled or not',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFORWATERTAX'), current_date, 'YES',0);

--rollback DELETE FROM eg_appconfig_values WHERE key_id in(SELECT id FROM eg_appconfig WHERE key_name='SENDEMAILFORWATERTAX' and MODULE in(select id from eg_module where name='Water Tax Management'));
--rollback DELETE FROM eg_appconfig WHERE key_name='SENDEMAILFORWATERTAX' and MODULE in(select id from eg_module where name='Water Tax Management');

--rollback DELETE FROM eg_appconfig_values WHERE key_id in(SELECT id FROM eg_appconfig WHERE key_name='SENDSMSFORWATERTAX' and MODULE in(select id from eg_module where name='Water Tax Management'));
--rollback DELETE FROM eg_appconfig WHERE key_name='SENDSMSFORWATERTAX' and MODULE in(select id from eg_module where name='Water Tax Management');