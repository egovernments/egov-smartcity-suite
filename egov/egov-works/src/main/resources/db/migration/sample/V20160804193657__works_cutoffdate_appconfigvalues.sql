-----AppConfig value for cutoff date----
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='CUTOFFDATEFORLEGACYDATAENTRY'), now(), '31-Jul-2016', 0);

---rollback delete from eg_appconfig_values where key_id = (select id from eg_appconfig where key_name='CUTOFFDATEFORLEGACYDATAENTRY');
