
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'DOCUMENTREQUIREDFORBPL', 'The document which is mandatory to get water tap connection for BPL category',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DOCUMENTREQUIREDFORBPL'),current_date, 'Ration card',0);

--rollback DELETE FROM eg_appconfig_values WHERE key_id in(SELECT id FROM eg_appconfig WHERE key_name='DOCUMENTREQUIREDFORBPL' and MODULE in(select id from eg_module where name='Water Tax Management'));
--rollback DELETE FROM eg_appconfig WHERE key_name='DOCUMENTREQUIREDFORBPL' and MODULE in(select id from eg_module where name='Water Tax Management');