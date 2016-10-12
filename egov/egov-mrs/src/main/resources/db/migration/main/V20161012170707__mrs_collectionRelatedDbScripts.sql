
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'MARRIAGE_FUNCTION_CODE', 'Function Code for Collections',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MARRIAGE_FUNCTION_CODE' and module= (select id from eg_module where name='Marriage Registration')), current_date, '0002',0);

update egcl_servicedetails set code='MR' WHERE NAME='MR Fee';

update egcl_servicedetails set code='MRR' WHERE NAME='MRR Fee';
