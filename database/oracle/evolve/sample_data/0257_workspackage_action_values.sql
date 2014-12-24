#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='SKIP_BUDGET_CHECK'),
to_date('03/05/2010','dd/MM/yyyy'),'Deposit Works - Own Asset');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='SKIP_BUDGET_CHECK'),
to_date('03/05/2010','dd/MM/yyyy'),'Deposit Works - Third Party Asset');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='SKIP_BUDGET_CHECK'),
to_date('03/05/2010','dd/MM/yyyy'),'Deposit Works - No Asset Created');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='SKIP_BUDGET_CHECK'),
to_date('03/05/2010','dd/MM/yyyy'),'Deposit Works - Road Cut Restoration');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='SKIP_BUDGET_CHECK');

