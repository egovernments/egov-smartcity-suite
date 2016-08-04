-----AppConfig for cutoff date----
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'CUTOFFDATEFORLEGACYDATAENTRY', 'Cut-off date for legacy data entry', (select id from eg_module where name='Works Management'), 0);

---rollback delete from eg_appconfig where key_name='CUTOFFDATEFORLEGACYDATAENTRY';