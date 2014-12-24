#UP
Insert into eg_appconfig_values(id,key_id,effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where key_name='RETENTIONMONEYREFUND_SHOW_ACTIONS'),sysdate,'View,WorkFlow History');

#DOWN
Delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='RETENTIONMONEYREFUND_SHOW_ACTIONS');


