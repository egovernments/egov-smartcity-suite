
--feematrix featureaction mapping

delete from eg_feature_action where action=(select id from eg_action where name ='Update-FeeMatrix') and 
feature=(select id FROM EG_FEATURE WHERE name  ='Create License Fee Matrix');

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search-FeeMatrix'),
(select id FROM EG_FEATURE WHERE name  ='View License Fee Matrix'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='LicenseSubcategoryDetailByFeeType'),
(select id FROM EG_FEATURE WHERE name  ='View License Fee Matrix'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='LicenseSubcategoryDetailByFeeType'),
(select id FROM EG_FEATURE WHERE name  ='Modify License FeeMatrix'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='LicenseSubcategoryDetailBySubcatgoryId'),
(select id FROM EG_FEATURE WHERE name  ='Modify License FeeMatrix'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='LicenseSubcategoryByCategory'),
(select id FROM EG_FEATURE WHERE name  ='Modify License FeeMatrix'));
