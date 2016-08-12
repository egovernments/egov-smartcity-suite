------------------------------------------------------------------------------
			--ADTAX eg_feature--
------------------------------------------------------------------------------

------------------------ Advertisement Collection Report -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Advertisement Collection Report','Advertisement DCB Report',(select id from eg_module  where name = 'AdvertisementTaxReports'));

------------------------ Agencywise Collection Report -----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Agencywise Collection Report','Agencywise Demand and Collection Report',(select id from eg_module  where name = 'AdvertisementTaxReports'));


------------------------------------------------------------------------------
			--ADTAX eg_feature_action--
------------------------------------------------------------------------------

------------------------ Advertisement Collection Report -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'dcbReportSearch') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'HoardingDcbReport') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

------------------------ Agencywise Collection Report -----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Agencywise Collection Report') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxSubCategories') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Agency Wise DCB Report') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Agency Hoardings') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));


------------------------------------------------------------------------------
			--ADTAX eg_feature_role--
------------------------------------------------------------------------------

------------------------ Advertisement Collection Report -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Advertisement Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

------------------------ Agencywise Collection Report -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Advertisement Tax Admin') ,(select id FROM eg_feature WHERE name = 'Agencywise Collection Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Advertisement Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Advertisement Collection Report'));

