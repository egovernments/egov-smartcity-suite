INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Balance Check Control Type' AND 
MODULE =(select id from eg_module where name='EGF')),current_date,
 'warning',0);
