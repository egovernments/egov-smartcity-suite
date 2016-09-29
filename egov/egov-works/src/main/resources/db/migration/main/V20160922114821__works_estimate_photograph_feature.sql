INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Upload Estimate Photograph','Upload Estimate Photograph',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Estimate Photograph','View Estimate Photograph',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UploadEstimatePhotographs') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get Estimate Number to Upload Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get WIN to Upload Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Line Estimate For Estimate Photographs') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Upload Estimate Photographs Form') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Save Estimate Photographs') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Estimate Photographs') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Photographs') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get Estimate Number to View Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get WIN to View Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Estimate Photographs') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Estimate Photograph Form') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get Work Order Number to View Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Get Contractors to View Estimate Photograph') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'View Estimate Photograph'));

--rollback delete from eg_feature_role  where feature  = (select id FROM eg_feature WHERE name = 'Upload Estimate Photograph');
--rollback delete from eg_feature_action  where feature  = (select id FROM eg_feature WHERE name = 'View Estimate Photograph');
--rollback delete from eg_feature  where id=(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph');
--rollback delete from eg_feature  where id=(select id FROM eg_feature WHERE name = 'View Estimate Photograph');