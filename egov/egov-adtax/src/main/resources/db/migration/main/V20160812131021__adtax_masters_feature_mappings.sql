
------------------------------------------------------------------------------
			--ADTAX eg_feature--
------------------------------------------------------------------------------

------------------------ Unit Of Measurement -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create UOM','Create Unit Of Measurement',(select id from eg_module  where name = 'Advertisement Tax UnitOfMeasure'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search UOM','Search Unit Of Measurement',(select id from eg_module  where name = 'Advertisement Tax UnitOfMeasure'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify UOM','Modify Unit Of Measurement',(select id from eg_module  where name = 'Advertisement Tax UnitOfMeasure'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View UOM','View Unit Of Measurement',(select id from eg_module  where name = 'Advertisement Tax UnitOfMeasure'));

------------------------ Change Penalty Rates -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Change Penalty Rates','Change Penalty Rates',(select id from eg_module  where name = 'Advertisement Tax Penalty Rates'));

------------------------ Category -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement Create Category','Advertisement Create Category',(select id from eg_module  where name = 'Advertisement Tax Category'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement Search Category','Advertisement Search Category',(select id from eg_module  where name = 'Advertisement Tax Category'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement Modify Category','Advertisement Modify Category',(select id from eg_module  where name = 'Advertisement Tax Category'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement View Category','Advertisement View Category',(select id from eg_module  where name = 'Advertisement Tax Category'));

------------------------ Agency -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Agency','Create Agency',(select id from eg_module  where name = 'Advertisement Tax Agency'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Agency','Search Agency',(select id from eg_module  where name = 'Advertisement Tax Agency'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Agency','Modify Agency',(select id from eg_module  where name = 'Advertisement Tax Agency'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Agency','View Agency',(select id from eg_module  where name = 'Advertisement Tax Agency'));

------------------------ SubCategory -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Subcategory','Create Subcategory',(select id from eg_module  where name = 'Advertisement Tax Subcategory'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Subcategory','Search Subcategory',(select id from eg_module  where name = 'Advertisement Tax Subcategory'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Subcategory','Modify Subcategory',(select id from eg_module  where name = 'Advertisement Tax Subcategory'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Subcategory','View Subcategory',(select id from eg_module  where name = 'Advertisement Tax Subcategory'));

------------------------ TPBO -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create TPBO','Create TPBO',(select id from eg_module  where name = 'Advertisement Tax TPBO'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search TPBO','Search TPBO',(select id from eg_module  where name = 'Advertisement Tax TPBO'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify TPBO','Modify TPBO',(select id from eg_module  where name = 'Advertisement Tax TPBO'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View TPBO','View TPBO',(select id from eg_module  where name = 'Advertisement Tax TPBO'));

------------------------ Rate Class -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Rate Class','Create Rate Class',(select id from eg_module  where name = 'Advertisement Tax RateClass'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Rate Class','Search Rate Class',(select id from eg_module  where name = 'Advertisement Tax RateClass'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Rate Class','Modify Rate Class',(select id from eg_module  where name = 'Advertisement Tax RateClass'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Rate Class','View Rate Class',(select id from eg_module  where name = 'Advertisement Tax RateClass'));

------------------------ Schedule Of Rate -----------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Schedule Of Rate','Create Schedule Of Rate',(select id from eg_module  where name = 'Advertisement Tax ScheduleofRate'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Schedule Of Rate','Search Schedule Of Rate',(select id from eg_module  where name = 'Advertisement Tax ScheduleofRate'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Schedule Of Rate','Modify Schedule Of Rate',(select id from eg_module  where name = 'Advertisement Tax ScheduleofRate'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Schedule Of Rate','View Schedule Of Rate',(select id from eg_module  where name = 'Advertisement Tax ScheduleofRate'));


------------------------------------------------------------------------------
			--ADTAX eg_feature_action--
------------------------------------------------------------------------------

------------------------ Unit Of Measurement -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateUnitOfMeasure') ,(select id FROM eg_feature WHERE name = 'Create UOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UnitOfMeasureSuccess') ,(select id FROM eg_feature WHERE name = 'Create UOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchUnitOfMeasure') ,(select id FROM eg_feature WHERE name = 'Search UOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UnitOfMeasureUpdate') ,(select id FROM eg_feature WHERE name = 'Modify UOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UnitOfMeasureSuccess') ,(select id FROM eg_feature WHERE name = 'Modify UOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'UnitOfMeasureSuccess') ,(select id FROM eg_feature WHERE name = 'View UOM'));

------------------------ Change Penalty Rates -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Change Penalty Rates') ,(select id FROM eg_feature WHERE name = 'Change Penalty Rates'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Penalty Rates') ,(select id FROM eg_feature WHERE name = 'Change Penalty Rates'));

------------------------ Category -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Createcategory') ,(select id FROM eg_feature WHERE name = 'Advertisement Create Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'categorySuccess') ,(select id FROM eg_feature WHERE name = 'Advertisement Create Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Searchcategory') ,(select id FROM eg_feature WHERE name = 'Advertisement Search Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'categoryUpdate') ,(select id FROM eg_feature WHERE name = 'Advertisement Modify Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'categorySuccess') ,(select id FROM eg_feature WHERE name = 'Advertisement Modify Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'categorySuccess') ,(select id FROM eg_feature WHERE name = 'Advertisement View Category'));

------------------------ Agency -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateAgency') ,(select id FROM eg_feature WHERE name = 'Create Agency'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AgencySuccess') ,(select id FROM eg_feature WHERE name = 'Create Agency'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchAgency') ,(select id FROM eg_feature WHERE name = 'Search Agency'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AgencyUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Agency'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AgencySuccess') ,(select id FROM eg_feature WHERE name = 'Modify Agency'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AgencyView') ,(select id FROM eg_feature WHERE name = 'View Agency'));

------------------------ SubCategory -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'CreateSubcategory') ,(select id FROM eg_feature WHERE name = 'Create Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'subcategorySuccess') ,(select id FROM eg_feature WHERE name = 'Create Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Searchsubcategory') ,(select id FROM eg_feature WHERE name = 'Search Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Search Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SubCategory search') ,(select id FROM eg_feature WHERE name = 'Search Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'subcategoryUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'subcategorySuccess') ,(select id FROM eg_feature WHERE name = 'Modify Subcategory'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'subcategorySuccess') ,(select id FROM eg_feature WHERE name = 'View Subcategory'));

------------------------ TPBO -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create TPBO') ,(select id FROM eg_feature WHERE name = 'Create TPBO'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Saved Revenue Inspectors') ,(select id FROM eg_feature WHERE name = 'Create TPBO'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search TPBO') ,(select id FROM eg_feature WHERE name = 'Search TPBO'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Saved Revenue Inspectors') ,(select id FROM eg_feature WHERE name = 'View TPBO'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Revenue Inspectors') ,(select id FROM eg_feature WHERE name = 'Modify TPBO'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Saved Revenue Inspectors') ,(select id FROM eg_feature WHERE name = 'Modify TPBO'));


------------------------ Rate Class -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'createRateClass') ,(select id FROM eg_feature WHERE name = 'Create Rate Class'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'savedRateClass') ,(select id FROM eg_feature WHERE name = 'Create Rate Class'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchRateClass') ,(select id FROM eg_feature WHERE name = 'Search Rate Class'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'savedRateClass') ,(select id FROM eg_feature WHERE name = 'View Rate Class'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RateClassUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Rate Class'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'savedRateClass') ,(select id FROM eg_feature WHERE name = 'Modify Rate Class')); 


------------------------ Schedule Of Rate -----------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'scheduleOfRateOnLoad') ,(select id FROM eg_feature WHERE name = 'Create Schedule Of Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Create Schedule Of Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'savedScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Create Schedule Of Rate'));


INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search ScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Search Schedule Of Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Search Schedule Of Rate'));


INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'scheduleOfRateOnLoad') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Of Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Of Rate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'savedScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Of Rate')); 


------------------------------------------------------------------------------
			--ADTAX eg_feature_role--
------------------------------------------------------------------------------

------------------------ Unit Of Measurement -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify UOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View UOM'));

------------------------ Change Penalty Rates -----------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Change Penalty Rates'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Change Penalty Rates'));

------------------------ Category -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Advertisement Create Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Advertisement Search Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Advertisement Modify Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Advertisement View Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Advertisement Create Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Advertisement Search Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Advertisement Modify Category'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Advertisement View Category'));

------------------------ Agency -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify Agency'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View Agency'));

------------------------ SubCategory -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify Subcategory'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View Subcategory'));

------------------------ TPBO -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify TPBO'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View TPBO'));

------------------------ Rate Class -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify Rate Class'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View Rate Class'));

------------------------ Schedule Of Rate -----------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Create Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Search Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='Modify Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name ='View Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Create Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Search Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='Modify Schedule Of Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name ='View Schedule Of Rate'));

