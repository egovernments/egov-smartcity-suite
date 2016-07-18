INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'GIS_INTEGRATION', 'Whether location details required for estimate or not ', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='GIS_INTEGRATION'), now(), 'Yes', 0);

--rollback delete from eg_appconfig_values  where key_id = (select id from eg_appconfig  where key_name ='GIS_INTEGRATION');
--rollback delete from eg_appconfig  where key_name ='GIS_INTEGRATION';