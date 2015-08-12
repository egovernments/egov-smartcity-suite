INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'ADDCONNECTIONALLOWEDIFWTDUE', 'To restrict whether multiple additional water tap connection allowed for single Property ID',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADDCONNECTIONALLOWEDIFWTDUE'),current_date, 'NO',0);
