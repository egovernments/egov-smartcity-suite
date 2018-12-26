INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SLAFORCLOSURE', 'CLOSURE CONNECTION SLA VALUE',0, (select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SLAFORCLOSURE' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, '15',0);

update eg_appconfig_values set value = '15' where key_id in (select id from eg_appconfig where module =(select id from eg_module where name='Sewerage Tax Management') and key_name = 'SLAFORCHANGEINCLOSET');