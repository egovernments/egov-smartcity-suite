-------Application config for PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE------
INSERT INTO eg_appconfig(id, key_name, description, module, version) VALUES (nextval('seq_eg_appconfig'), 'PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE', 'Either application has to read the estimated rate and apply the Tender finalised percentage for the total work value or percentage on each and every SOR and arrive at the Agreement value', (select id from eg_module where name='Works Management'), 0);
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE'), now(), 'Yes', 0);

ALTER TABLE egw_workorder_activity ADD COLUMN sorcategory varchar(15);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE');
--rollback delete from eg_appconfig where key_name='PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE';



