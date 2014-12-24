#UP
update eg_appconfig_values
set value='ADMIN_SANCTIONED'
where key_id=(select id from  eg_appconfig where key_name='ESTIMATE_STATUS');

update eg_appconfig_values
set value='APPROVED'
where key_id=(select id from  eg_appconfig where key_name='WORKS_PACKAGE_STATUS');

#DOWN
update eg_appconfig_values
set value='Approved'
where key_id=(select id from  eg_appconfig where key_name='WORKS_PACKAGE_STATUS');

update eg_appconfig_values
set value=''Admin Sanctioned''
where key_id=(select id from  eg_appconfig where key_name='ESTIMATE_STATUS');
