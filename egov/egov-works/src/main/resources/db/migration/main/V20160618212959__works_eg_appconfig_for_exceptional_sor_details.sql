-------Application config for Exceptional SOR details------
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'EXCEPTIONALSOR', 'Exceptional SOR', (select id from eg_module where name='Works Management'), 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='EXCEPTIONALSOR'), now(), '30 MTR,30', 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='EXCEPTIONALSOR'), now(), '10 SQM,10', 0);

ALTER TABLE egw_estimate_activity RENAME COLUMN sorrate to estimaterate;
--rollback ALTER TABLE egw_estimate_activity RENAME COLUMN estimaterate to sorrate;
--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='EXCEPTIONALSOR');
--rollback delete from eg_appconfig where key_name='EXCEPTIONALSOR';

