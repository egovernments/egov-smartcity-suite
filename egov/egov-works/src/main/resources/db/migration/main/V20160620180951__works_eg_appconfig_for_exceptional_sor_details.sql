-------Application config for Exceptional SOR details------
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'EXCEPTIONALUOMS', 'This is to calculate multiplication factor for exceptional uoms', (select id from eg_module where name='Works Management'), 0);


ALTER TABLE egw_estimate_activity RENAME COLUMN sorrate to estimaterate;


--rollback ALTER TABLE egw_estimate_activity RENAME COLUMN estimaterate to sorrate;
--rollback delete from eg_appconfig where key_name='EXCEPTIONALUOMS';


