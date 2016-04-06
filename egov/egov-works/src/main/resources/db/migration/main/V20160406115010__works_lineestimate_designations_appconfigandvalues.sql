INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DESIGNATION_EXE_ENGINEER', 'Designation for loading Executive Engineer incharge employees in Spill over Line Estimate screen', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DESIGNATION_SUPERINTENDING_ENGINEER', 'Designation for loading Superintending Engineer incharge employees in Spill over Line Estimate screen', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DESIGNATION_CHIEF_ENGINEER', 'Designation for loading Chief Engineer incharge employees in Spill over Line Estimate screen', (select id from eg_module where name='Works Management'));

INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='DESIGNATION_EXE_ENGINEER'), to_date('01-03-2016','DD-MM-YYYY'), 'Executive engineer', 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='DESIGNATION_SUPERINTENDING_ENGINEER'), to_date('01-03-2016','DD-MM-YYYY'), 'Superintending engineer', 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='DESIGNATION_CHIEF_ENGINEER'), to_date('01-03-2016','DD-MM-YYYY'), 'Chief Engineer', 0);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('DESIGNATION_EXE_ENGINEER'));
--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('DESIGNATION_SUPERINTENDING_ENGINEER'));
--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('DESIGNATION_CHIEF_ENGINEER'));

--rollback delete from eg_appconfig where key_name in('DESIGNATION_EXE_ENGINEER');
--rollback delete from eg_appconfig where key_name in('DESIGNATION_SUPERINTENDING_ENGINEER');
--rollback delete from eg_appconfig where key_name in('DESIGNATION_CHIEF_ENGINEER');