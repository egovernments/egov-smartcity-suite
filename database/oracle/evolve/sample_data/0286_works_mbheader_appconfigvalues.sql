#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WORKORDER_STATUS'),
to_date('03/05/2010','dd/MM/yyyy'),'Work commenced');
INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ESTIMATE_OR_WP_FOR_WO'),SYSDATE, 'both'); 
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='MB_CREATED_BY_SELECTION'),
to_date('03/05/2010','dd/MM/yyyy'),'no');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WORKORDER_STATUS');
delete from EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='ESTIMATE_OR_WP_FOR_WO');
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='MB_CREATED_BY_SELECTION');
