INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ASSET_MANDATORY', 'To make Asset linking required or not in Estimate screen', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='ASSET_MANDATORY'), to_date('01-10-2015','DD-MM-YYYY'), 'yes', 0);


--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='ASSET_MANDATORY');
--rollback delete from eg_appconfig where key_name='ASSET_MANDATORY';