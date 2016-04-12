INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ENGINEERINCHARGE_DESIGNATION', 'Designation for loading Engineer incharge employees in Letter of acceptance screen', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='ENGINEERINCHARGE_DESIGNATION'), to_date('01-03-2016','DD-MM-YYYY'), 'Assistant engineer', 0);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('ENGINEERINCHARGE_DESIGNATION'));
--rollback delete from eg_appconfig where key_name in('ENGINEERINCHARGE_DESIGNATION');