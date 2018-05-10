INSERT INTO egevntnotification_module (id, name, active) VALUES (nextval('seq_egevntnotification_module'), 'General', true);
INSERT INTO egevntnotification_module (id, name, active) VALUES (nextval('seq_egevntnotification_module'), 'Property', true);
INSERT INTO egevntnotification_module (id, name, active) VALUES (nextval('seq_egevntnotification_module'), 'Water Charges', true);
INSERT INTO egevntnotification_module (id, name, active) VALUES (nextval('seq_egevntnotification_module'), 'Asset', true);
INSERT INTO egevntnotification_module (id, name, active) VALUES (nextval('seq_egevntnotification_module'), 'Legal Case', true);

INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Free Message', true, 
(select id from egevntnotification_module where name = 'General'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Public Notice Message', true, 
(select id from egevntnotification_module where name = 'General'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Due Notice', true, 
(select id from egevntnotification_module where name = 'Property'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Renewal Notice', true, 
(select id from egevntnotification_module where name = 'Property'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Bill Payment Notice', true, 
(select id from egevntnotification_module where name = 'Water Charges'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Disruption of Services Notice', true, 
(select id from egevntnotification_module where name = 'Water Charges'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Asset Renewal Notice', true,
(select id from egevntnotification_module where name = 'Asset'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Agreement Expiry Notice', true, 
(select id from egevntnotification_module where name = 'Asset'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Case Hearing Date Notice', true, 
(select id from egevntnotification_module where name = 'Legal Case'));
INSERT INTO egevntnotification_category (id, name, active, module_id) VALUES (nextval('seq_egevntnotification_category'), 'Case Closure Notice', true, 
(select id from egevntnotification_module where name = 'Legal Case'));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'userName', true, 
(select id from egevntnotification_category where name = 'Public Notice Message' and module_id IN (select id from egevntnotification_module where name = 'General')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'userName', true, 
(select id from egevntnotification_category where name = 'Due Notice' and module_id IN (select id from egevntnotification_module where name = 'Property')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'userName', true, 
(select id from egevntnotification_category where name = 'Due Notice' and module_id IN (select id from egevntnotification_module where name = 'Property')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'dueAmount', true, 
(select id from egevntnotification_category where name = 'Due Notice' and module_id IN (select id from egevntnotification_module where name = 'Property')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'dueDate', true, 
(select id from egevntnotification_category where name = 'Due Notice' and module_id IN (select id from egevntnotification_module where name = 'Property')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'properyNumber', true, 
(select id from egevntnotification_category where name = 'Due Notice' and module_id IN (select id from egevntnotification_module where name = 'Property')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'consumerNumber', true, 
(select id from egevntnotification_category where name = 'Bill Payment Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'billNumber', true, 
(select id from egevntnotification_category where name = 'Bill Payment Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'billAmount', true, 
(select id from egevntnotification_category where name = 'Bill Payment Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'userName', true, 
(select id from egevntnotification_category where name = 'Bill Payment Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'userName', true, 
(select id from egevntnotification_category where name = 'Disruption of Services Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));

INSERT INTO egevntnotification_parameters (id, name, active, category_id) VALUES (nextval('seq_egevntnotification_parameters'), 'disruptionDate', true, 
(select id from egevntnotification_category where name = 'Disruption of Services Notice' and module_id IN (select id from egevntnotification_module where name = 'Water Charges')));
