INSERT INTO egevntnotification_template_module (id, name, active, code) VALUES (nextval('seq_egevntnotification_template_module'), 'General', true, 'GN');

INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Free Message', true, 
(select id from egevntnotification_template_module where name = 'General'));
INSERT INTO egevntnotification_module_category (id, name, active, module) VALUES (nextval('seq_egevntnotification_module_category'), 'Public Notice Message', true, 
(select id from egevntnotification_template_module where name = 'General'));

INSERT INTO egevntnotification_category_parameters (id, name, active, category) VALUES (nextval('seq_egevntnotification_category_parameters'), 'userName', true, 
(select id from egevntnotification_module_category where name = 'Public Notice Message' and module IN (select id from egevntnotification_template_module where name = 'General')));
