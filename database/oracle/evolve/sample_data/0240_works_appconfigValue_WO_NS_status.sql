#UP


Insert into eg_appconfig_values(id,key_id,effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where key_name='NEGOTIATIONSTMT_WO_STATUS'),sysdate,'Approved'); 

#DOWN

delete from EG_APPCONFIG_VALUES where key_id=(select id from eg_appconfig where key_name='NEGOTIATIONSTMT_WO_STATUS');
