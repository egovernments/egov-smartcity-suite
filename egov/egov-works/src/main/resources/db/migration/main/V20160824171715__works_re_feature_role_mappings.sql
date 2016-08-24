------------------------------------ADDING FEATURE ACTION STARTS------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksRevisionEstimateSearchActivities') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksRevisionEstimateSearchActivitiesForm') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RevisionEstimateView') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
------------------------------------ADDING FEATURE ACTION ENDS------------------------