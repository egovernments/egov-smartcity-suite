INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDEPARTMENTCOLLMODES','Department Code for Collection Modes',(select id from eg_module where name='Collection'));
INSERT into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='COLLECTIONDEPARTMENTCOLLMODES'),current_date,'REV');
INSERT into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='COLLECTIONDEPARTMENTCOLLMODES'),current_date,'ACC');




