DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='CHANGEOFUSEALLOWEDIFWTDUE' and module = (select id from eg_module where name='Water Tax Management'));
DELETE FROM eg_appconfig where key_name='CHANGEOFUSEALLOWEDIFWTDUE' and module = (select id from eg_module where name='Water Tax Management');

INSERT INTO eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'CHANGEOFUSEALLOWEDIFWTDUE','To restrict change of water tap connection when water tax is due',0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CHANGEOFUSEALLOWEDIFWTDUE'), current_date, 'NO',0);

INSERT INTO eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'CLOSUREALLOWEDIFWTDUE','To restrict change of water tap connection when water tax is due',0,null,null,null,null,(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='CLOSUREALLOWEDIFWTDUE'), current_date, 'NO',0);

--rollback DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='CLOSUREALLOWEDIFWTDUE' and module = (select id from eg_module where name='Water Tax Management'));
--rolback DELETE FROM eg_appconfig where key_name='CLOSUREALLOWEDIFWTDUE' and module = (select id from eg_module where name='Water Tax Management');
