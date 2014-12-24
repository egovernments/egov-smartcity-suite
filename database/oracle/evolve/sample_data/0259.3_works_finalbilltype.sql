#UP
update eg_appconfig_values set value='Final Bill' where key_id=(select id from  eg_appconfig where key_name='FinalBillType');
#DOWN
