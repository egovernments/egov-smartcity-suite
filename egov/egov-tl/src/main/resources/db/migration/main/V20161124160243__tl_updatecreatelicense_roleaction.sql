Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),
(select id from eg_action where name='Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Ajax-UnitOfMeasurementBySubCategory') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Ajax-UnitOfMeasurementBySubCategory') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Ajax-UnitOfMeasurementBySubCategory') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Ajax-UnitOfMeasurementBySubCategory') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
