INSERT INTO egevntnotification_template_module (id, name, active, code) VALUES (nextval('seq_egevntnotification_template_module'), 'Property', true, 'PT');

INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Due Notice', true, 
(select id from egevntnotification_template_module where name = 'Property'));
INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Renewal Notice', true, 
(select id from egevntnotification_template_module where name = 'Property'));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'userName', true, 
(select id from egevntnotification_module_category where name = 'Due Notice' and module IN (select id from egevntnotification_template_module where name = 'Property')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'dueAmount', true, 
(select id from egevntnotification_module_category where name = 'Due Notice' and module IN (select id from egevntnotification_template_module where name = 'Property')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'dueDate', true, 
(select id from egevntnotification_module_category where name = 'Due Notice' and module IN (select id from egevntnotification_template_module where name = 'Property')));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'propertyNumber', true, 
(select id from egevntnotification_module_category where name = 'Due Notice' and module IN (select id from egevntnotification_template_module where name = 'Property')));


