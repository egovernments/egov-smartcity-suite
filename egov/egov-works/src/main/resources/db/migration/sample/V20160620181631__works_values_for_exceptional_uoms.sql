INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='EXCEPTIONALUOMS'), now(), '30 MTR,30', 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='EXCEPTIONALUOMS'), now(), '10 SQM,10', 0);


--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='EXCEPTIONALUOMS');


