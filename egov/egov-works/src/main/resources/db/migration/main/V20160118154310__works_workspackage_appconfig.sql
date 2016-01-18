INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ESTIMATE_OR_WP_SEARCH_REQ', 'Estimate or Works Package search required to create Work Order', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='ESTIMATE_OR_WP_SEARCH_REQ'), to_date('01-01-2016','DD-MM-YYYY'), 'workspackage', 0);

INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'WP_OFFLINE_STATUS', 'List of offline status for Works Package', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='WP_OFFLINE_STATUS'), to_date('01-01-2016','DD-MM-YYYY'), 'Noticeinvitingtenderreleased,Tender document released,Tender opened,Technical Evaluation done,Commercial Evaluation done,L1 tender finalised', 0);

INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'WP_LAST_STATUS', 'The last status of Works Package', (select id from eg_module where name='Works Management'));
INSERT INTO eg_appconfig_values(id, key_id, effective_from, value, version) VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where key_name='WP_LAST_STATUS'), to_date('01-01-2016','DD-MM-YYYY'), 'L1 tender finalised', 0);


--rollback delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name in('ESTIMATE_OR_WP_SEARCH_REQ','WP_OFFLINE_STATUS','WP_LAST_STATUS'));
--rollback delete from eg_appconfig where key_name in('ESTIMATE_OR_WP_SEARCH_REQ','WP_OFFLINE_STATUS','WP_LAST_STATUS');