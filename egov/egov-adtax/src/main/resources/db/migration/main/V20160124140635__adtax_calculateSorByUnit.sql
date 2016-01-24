INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 
'Calculate SOR By Unit', 
'Calculate SOR By Unit',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Calculate SOR By Unit' AND 
 MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,
  'NO',0);