INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SLAFORNEWSEWERAGECONNECTION', 'NEW SEWERAGE CONNECTION  SLA VALUE',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SLAFORNEWSEWERAGECONNECTION' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, '15',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SLAFORCHANGEINCLOSET', 'CHANGE IN CLOSET  SLA VALUE',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SLAFORCHANGEINCLOSET' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, '7',0);
