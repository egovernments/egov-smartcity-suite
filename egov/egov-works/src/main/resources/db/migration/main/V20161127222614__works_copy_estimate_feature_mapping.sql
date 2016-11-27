INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEstimateNumbersToCopy') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxActivitiesToCopyEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchEstimateForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimates') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEstimateNumbersToCopy') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxActivitiesToCopyEstimate') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchEstimateForm') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimates') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));