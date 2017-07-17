                     
----------- donation charge config----------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'LEGACY_DONATION_CHARGE', 'Collect Donation Charge',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='LEGACY_DONATION_CHARGE' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, 'YES',0);