
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'MULTIPLENEWCONNECTIONFORPID', 'To restrict whether multiple new water tap connection allowed for single Property ID',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MULTIPLENEWCONNECTIONFORPID'),current_date, 'NO',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'NEWCONNECTIONALLOWEDIFPTDUE', 'To check whether New water tap connection application allowed if PT Tax due present',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='NEWCONNECTIONALLOWEDIFPTDUE'), current_date, 'NO',0);

--rollback DELETE FROM eg_appconfig_values WHERE key_id in(SELECT id FROM eg_appconfig WHERE key_name='NEWCONNECTIONALLOWEDIFPTDUE' and MODULE in(select id from eg_module where name='Water Tax Management'));
--rollback DELETE FROM eg_appconfig WHERE key_name='NEWCONNECTIONALLOWEDIFPTDUE' and MODULE in(select id from eg_module where name='Water Tax Management');

--rollback DELETE FROM eg_appconfig_values WHERE key_id in(SELECT id FROM eg_appconfig WHERE key_name='MULTIPLENEWCONNECTIONFORPID' and MODULE in(select id from eg_module where name='Water Tax Management'));
--rollback DELETE FROM eg_appconfig WHERE key_name='MULTIPLENEWCONNECTIONFORPID' and MODULE in(select id from eg_module where name='Water Tax Management');