INSERT INTO EG_APPCONFIG_VALUES(id,key_id,effective_from,value)
VALUES(nextval('seq_eg_appconfig_values'),(SELECT id FROM eg_appconfig WHERE key_name='smsSender'),current_date,'eGovFoundation');

INSERT INTO EG_APPCONFIG_VALUES(id,key_id,effective_from,value)
VALUES(nextval('seq_eg_appconfig_values'),(SELECT id FROM eg_appconfig WHERE key_name='serviceProviderUrl'),current_date,'http://www.unicel.in/SendSMS/sendmsg.php?uname=chennaicorp&amp;pass=corp13&amp;send=CHENNAICORP&amp;dest=#&amp;msg=#');

--rollback DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='smsSender');
--rollback DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='serviceProviderUrl');

