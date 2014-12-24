#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='percentage_grant'),to_date('13/04/2009','dd/MM/yyyy'),
'1.5');
#DOWN
delete from EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='percentage_grant');
