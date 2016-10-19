INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorMBHeaders') ,
(select id FROM eg_feature WHERE name = 'Create Abstract MB'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Contractor MB View') ,
(select id FROM eg_feature WHERE name = 'Create Abstract MB'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorMBHeaders') ,
(select id FROM eg_feature WHERE name = 'Search Measurement Book'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Contractor MB View') ,
(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
