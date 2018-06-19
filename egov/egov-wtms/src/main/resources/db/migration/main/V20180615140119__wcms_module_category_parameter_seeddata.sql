INSERT INTO egevntnotification_template_module (id, name, active, code) VALUES (nextval('seq_egevntnotification_template_module'), 'Water Charges', true, 'WC');

INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Bill Payment Notice', true, 
(select id from egevntnotification_template_module where name = 'Water Charges'));
INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Disruption of Services Notice', true, 
(select id from egevntnotification_template_module where name = 'Water Charges'));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'consumerNumber', true, 
(select id from egevntnotification_module_category where name = 'Bill Payment Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'billNumber', true, 
(select id from egevntnotification_module_category where name = 'Bill Payment Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'billAmount', true, 
(select id from egevntnotification_module_category where name = 'Bill Payment Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'userName', true, 
(select id from egevntnotification_module_category where name = 'Bill Payment Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'userName', true, 
(select id from egevntnotification_module_category where name = 'Disruption of Services Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'disruptionDate', true, 
(select id from egevntnotification_module_category where name = 'Disruption of Services Notice' and module IN (select id from egevntnotification_template_module where name = 'Water Charges')));
