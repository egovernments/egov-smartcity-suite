
update eg_appconfig_values set value='Accounts' where key_id 
in(select id from eg_appconfig where key_name='DEPARTMENTFORWORKFLOW');