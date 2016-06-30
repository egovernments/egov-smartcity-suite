-------Application config for Exceptional SOR details------
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'Retention money deduction percentage for Part Bill', 'This is to calculate retention money deduction amount for Part Bill', (select id from eg_module where name='Works Management'), 0);

INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'Retention money deduction percentage for Final Bill', 'This is to calculate retention money deduction amount for Final Bill', (select id from eg_module where name='Works Management'), 0);

INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='Retention money deduction percentage for Part Bill'), now(), '2.5', 0);

INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='Retention money deduction percentage for Final Bill'), now(), '5', 0);


--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='EXCEPTIONALUOMS');
--rollback delete from eg_appconfig where key_name='Retention money deduction percentage for Part Bill';
--rollback delete from eg_appconfig where key_name='Retention money deduction percentage for Final Bill';
