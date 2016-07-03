INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'SECOND_LEVEL_EDIT_MB', 'To provide functionality for the second level user to edit Measurement Book', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='SECOND_LEVEL_EDIT_MB'), now(), 'No', 0);

INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'MB_QUANTITY_TOLERANCE_LEVEL', 'A tolerance level within which the actual used quantity can be more than the approved work order qty', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='MB_QUANTITY_TOLERANCE_LEVEL'), now(), '100', 0);

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='MB_QUANTITY_TOLERANCE_LEVEL');
--rollback delete from eg_appconfig where key_name='MB_QUANTITY_TOLERANCE_LEVEL';

--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='SECOND_LEVEL_EDIT_MB');
--rollback delete from eg_appconfig where key_name='SECOND_LEVEL_EDIT_MB';