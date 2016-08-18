------------------------------------------------------------------------------
			--ADTAX eg_feature--
------------------------------------------------------------------------------

------------------------ Legacy -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Legacy Advertisement','Create Legacy Advertisement',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Deactivate -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Deactivate Advertisement','Deactivate Advertisement',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Search Advertisement -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Advertisement','Search Advertisement',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Generate Permit Order -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Generate Permit Order','Generate Permit Order',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Generate Demand Notice -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Generate Demand Notice','Generate Demand Notice',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Update Legacy Advertisements -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Legacy Advertisements','Update Legacy Advertisements',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Collect Advertisement Tax -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collect Advertisement Tax','Collect Advertisement Tax',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Auto Generate Demand for Advertisements -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Auto Generate Demand','Auto Generate Demand',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Advertisement Renewal -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement Renewal','Advertisement Renewal',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Advertisement Creation -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Advertisement','Create Advertisement',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));

------------------------ Modify Advertisement -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Advertisement','Modify Advertisement',(select id from eg_module  where name = 'AdvertisementTaxTransactions'));


------------------------------------------------------------------------------
			--ADTAX eg_feature_action--
------------------------------------------------------------------------------

------------------------ Legacy -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'dcbReportSearch') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateTaxAmount') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSuccess') ,(select id FROM eg_feature WHERE name = 'Create Legacy Advertisement'));

------------------------ Deactivate -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'deactivate advertisement') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Active Records') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Status Change Of Records') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Deactivate The Record') ,(select id FROM eg_feature WHERE name = 'Deactivate Advertisement'));


------------------------ Search Advertisement -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'searchadvertisement') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'searchadvertisementResult') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingView') ,(select id FROM eg_feature WHERE name = 'Search Advertisement'));


------------------------ Generate Permit Order -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'permitorderreport') ,(select id FROM eg_feature WHERE name = 'Generate Permit Order'));


------------------------ Generate Demand Notice -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'demandnoticereport') ,(select id FROM eg_feature WHERE name = 'Generate Demand Notice'));

------------------------ Update Legacy Advertisements -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSearchUpdate') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingView') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingLegacyUpdate') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateTaxAmount') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSuccess') ,(select id FROM eg_feature WHERE name = 'Update Legacy Advertisements'));


------------------------ Collect Advertisement Tax -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CollectAdvertisementTax') ,(select id FROM eg_feature WHERE name = 'Collect Advertisement Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Collect Advertisement Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Collect Advertisement Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Collect Advertisement Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'searchHoardingResult') ,(select id FROM eg_feature WHERE name = 'Collect Advertisement Tax'));


------------------------ Auto Generate Demand for Advertisements -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateDemandAdvertisementTax') ,(select id FROM eg_feature WHERE name = 'Auto Generate Demand'));

------------------------ Advertisement Renewal -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'renewalsearch') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'renewalsearchresult') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'advertisementrenewalcreate') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateTaxAmount') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSuccess') ,(select id FROM eg_feature WHERE name = 'Advertisement Renewal'));

------------------------ Advertisement Creation -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateHoarding') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateTaxAmount') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSuccess') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

------------------------ Modify Advertisement -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SubcategoryAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ActiveAgencyAjaxDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateTaxAmount') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingSuccess') ,(select id FROM eg_feature WHERE name = 'Modify Advertisement'));


------------------------------------------------------------------------------
			--ADTAX eg_feature_role--
------------------------------------------------------------------------------

------------------------ Legacy -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create Legacy Advertisement'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create Legacy Advertisement'));

------------------------ Deactivate -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Deactivate Advertisement'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Deactivate Advertisement'));

------------------------ Search Advertisement -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search Advertisement'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search Advertisement'));

------------------------ Generate Permit Order -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Generate Permit Order'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Generate Permit Order'));

------------------------ Generate Demand Notice -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Generate Demand Notice'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Generate Demand Notice'));

------------------------ Update Legacy Advertisements -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Update Legacy Advertisements'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Update Legacy Advertisements'));

------------------------ Collect Advertisement Tax -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Collect Advertisement Tax'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Collect Advertisement Tax'));

------------------------ Auto Generate Demand for Advertisements -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Auto Generate Demand'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Auto Generate Demand'));

------------------------ Advertisement Renewal -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Creator') ,(select id FROM eg_feature WHERE name ='Advertisement Renewal'));

------------------------ Advertisement Creation -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Creator') ,(select id FROM eg_feature WHERE name ='Create Advertisement'));

------------------------ Modify Advertisement -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Approver') ,(select id FROM eg_feature WHERE name ='Modify Advertisement'));

