
update eg_appconfig_values set value = 7 where KEY_ID in (select id from eg_appconfig where key_name = 'coa_detailcode_length');

update eg_appconfig_values set value = 5 where KEY_ID in (select id from eg_appconfig where key_name = 'coa_subminorcode_length');

update eg_appconfig_values set value = 2 where KEY_ID in (select id from eg_appconfig where key_name = 'parent_for_detailcode');
