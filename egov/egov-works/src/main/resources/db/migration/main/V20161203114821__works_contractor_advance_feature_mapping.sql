--Create Contractor Advance eg_feature

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Contractor Advance','Create Contractor Advance',(select id from EG_MODULE where name = 'Works Management'));

--Create Contractor Advance eg_feature_action
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLOAToCreateCR') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchResultLOAToCreateCR') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetEstimateNumbersToCreateCR') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetContractorsToCreateCR') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetWorkOrderNumbersToCreateCR') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));

--Create Contractor Advance eg_feature_role
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Contractor Advance'));

--update feature name for line estimate
update eg_feature set name='Create Estimate' , description ='Create a Estimate' where name = 'Create Line Estimate' and module = (select id from EG_MODULE where name = 'Works Management');
update eg_feature set name='Search Estimate' , description ='Search Estimate' where name = 'Search Line Estimate' and module = (select id from EG_MODULE where name = 'Works Management');
update eg_feature set name='Update Estimate' , description ='Update an Estimate' where name = 'Update Line Estimate' and module = (select id from EG_MODULE where name = 'Works Management');
update eg_feature set name='Create Spillover Estimate' , description ='Create a Spillover Estimate' where name = 'Create Spillover Line Estimate' and module = (select id from EG_MODULE where name = 'Works Management');

--update feature name for Abstract/Detailed Estimate
update eg_feature set name='Create Abstract/Detailed Estimate' , description ='Create a Abstract/Detailed Estimate' where name = 'Create Abstract Estimate' and module = (select id from EG_MODULE where name = 'Works Management');
update eg_feature set name='Search Abstract/Detailed Estimate' , description ='Search Abstract/Detailed Estimate' where name = 'Search Abstract Estimate' and module = (select id from EG_MODULE where name = 'Works Management');
update eg_feature set name='Update Abstract/Detailed Estimate' , description ='Update an Abstract/Detailed Estimate' where name = 'Update Abstract Estimate' and module = (select id from EG_MODULE where name = 'Works Management');

--update feature name for Offline Status for Abstract/Detailed Estimate
update eg_feature set name='Offline Status for Abstract/Detailed Estimate' , description ='Offline Status for Abstract/Detailed Estimate' where name = 'Offline Status for Abstract Estimate' and module = (select id from EG_MODULE where name = 'Works Management');

--update feature name for Cancel Abstract/Detailed Estimate
update eg_feature set name='Cancel Abstract/Detailed Estimate' , description ='Cancel Abstract/Detailed Estimate' where name = 'Cancel Abstract Estimate' and module = (select id from EG_MODULE where name = 'Works Management');

--update feature name for Cancel Abstract/Detailed Estimate
update eg_feature set name='Cancel Abstract/Detailed Estimate' , description ='Cancel Estimate' where name = 'Cancel Estimate' and module = (select id from EG_MODULE where name = 'Works Management');

--feature action for Abstract MB
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'HistoryMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Update Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'HistoryMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));