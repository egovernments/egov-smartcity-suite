#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WORKS_SETSTATUS_VALUE'),
to_date('03/05/2010','dd/MM/yyyy'),'Set Status');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WORKS_SETSTATUS_VALUE');

