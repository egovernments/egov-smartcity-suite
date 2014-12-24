#UP

Insert into eg_appconfig_values(id,key_id,effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where key_name='TENDERNEGOTIATION_SHOW_ACTIONS'),sysdate,'View,View PDF,WorkFlow History,View Document');

#DOWN

Delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='TENDERNEGOTIATION_SHOW_ACTIONS');
