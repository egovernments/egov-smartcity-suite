#UP

insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorkOrder.laststatus'),
to_date('07/06/2010','dd/MM/yyyy'),'Work commenced');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='WorkOrder.setstatus'),
to_date('07/06/2010','dd/MM/yyyy'),'Work Order acknowledged,Site handed over,Work commenced');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WorkOrder.setstatus');
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='WorkOrder.laststatus');

