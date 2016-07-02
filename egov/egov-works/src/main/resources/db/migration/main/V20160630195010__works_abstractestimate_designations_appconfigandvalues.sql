INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DESIGNATION_ADMINSANCTION_AUTHORITY', 'Designation for loading Admin Sanction Authorities in Spill over Abstract Estimate screen', (select id from eg_module where name='Works Management'));

INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='DESIGNATION_ADMINSANCTION_AUTHORITY'), to_date('01-06-2016','DD-MM-YYYY'), 'Commissioner', 0);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('DESIGNATION_ADMINSANCTION_AUTHORITY'));

--rollback delete from eg_appconfig where key_name in('DESIGNATION_ADMINSANCTION_AUTHORITY');