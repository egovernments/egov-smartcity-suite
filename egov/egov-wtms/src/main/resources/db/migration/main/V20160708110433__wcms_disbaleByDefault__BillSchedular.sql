update eg_appconfig_values set value='NO' where key_id in(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ENABLEBILLSCHEDULAR');
