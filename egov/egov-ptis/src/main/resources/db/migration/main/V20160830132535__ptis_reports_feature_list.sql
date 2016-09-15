--------------------------------------Property Tax Reports Feature List-----------------------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Property Tax Reports','View property tax reports',
(select id from eg_module  where name = 'Property Tax'));

----------------------------------------------Feature Action Starts---------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Zone Wise Collection Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ward Wise Collection Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Block Wise Collection Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Locality Wise Collection Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Collection Summary Report Result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'DCBReport') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchDCBReport') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Arrears Register Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'GenerateArrearRegisterReport') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxLoadBoundaryWard') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ajaxLoadBoundaryBlock') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Title Transfer Register Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TitleTransferRegReportResult') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Usage Wise Collection Summary Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Base Register Report result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'NatureOfUsageReport-form') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'NatureOfUsageReport-result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Daily collection report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Daily collection report result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'defaultersReport') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'defaultersReportResult') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Current Instalment DCB Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Current Instalment DCB Report result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'dailyBCCollection-form') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'dailyBCCollection-result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Base Register Report VLT') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Base Register Report VLT result') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Base Register Report') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
----------------------------------------------Feature Action Ends---------------------------------------------------

----------------------------------------------Feature Role Mapping Starts---------------------------------------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
----------------------------------------------Feature Role Mapping Ends---------------------------------------------
