-------Application config for asset details for abstractestimate------
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'ASSETDETAILS_REQUIRED', 'Asset detials for abstract estimate', (select id from eg_module where name='Works Management'), 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='ASSETDETAILS_REQUIRED'), now(), 'No', 0);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='ASSETDETAILS_REQUIRED');
--rollback delete from eg_appconfig where key_name='ASSETDETAILS_REQUIRED';