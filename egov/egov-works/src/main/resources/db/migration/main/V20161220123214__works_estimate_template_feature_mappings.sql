INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchEstimateTemplateForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateTemplates') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchEstimateTemplateForm') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateTemplates') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));