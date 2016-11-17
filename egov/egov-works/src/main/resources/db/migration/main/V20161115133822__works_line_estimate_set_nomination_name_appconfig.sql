INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'NOMINATION_NAME', 'Set nomination name for mode of entrustment', (select id from eg_module where name='Works Management'));

--rollback delete from eg_appconfig where key_name='NOMINATION_NAME';