
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES 
( nextval('SEQ_EG_APPCONFIG'), 'DEPARTMENTFORGENERATEBILL',
'Department code used for generate Bill',(select id from eg_module where name='Trade License'));



INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DEPARTMENTFORGENERATEBILL' AND   
 MODULE =(select id from eg_module where name='Trade License')),current_date,'H',0);
