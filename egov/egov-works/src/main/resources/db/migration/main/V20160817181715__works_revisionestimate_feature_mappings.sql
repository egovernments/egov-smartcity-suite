------------------------------------ADDING FEATURE STARTS------------------------
--Revision Estimate
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Revision Estimate','Create a Revision Estimate',(select id from EG_MODULE where name = 'Works Management'));

------------------------------------ADDING FEATURE ENDS------------------------

------------------------------------ADDING FEATURE ACTION STARTS------------------------
--Revision Estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxLOANumbersToCreateRE') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxContractorsToCreateRE') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxSearchLOAToCreateRE') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksRevisionEstimateSuccessPage') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));

------------------------------------ADDING FEATURE ACTION ENDS------------------------

------------------------------------ADDING FEATURE ROLE STARTS------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));