#UP

update eg_appconfig_values set value='View,View PDF,View Completion Certificate,WorkFlow History,View Document' where key_id = (select id from eg_appconfig where key_name='BILL_SHOW_ACTIONS');


#DOWN

update eg_appconfig_values set value='View,View PDF,View Completion Certificate,WorkFlow History,View Document' where key_id = (select id from eg_appconfig where key_name='BILL_SHOW_ACTIONS');

