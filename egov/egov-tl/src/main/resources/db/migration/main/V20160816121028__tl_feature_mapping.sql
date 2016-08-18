
--------------------------------------------------------Start eg_feature-----------------------------------------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create New License','Creating a New License',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Legacy License','Create Legacy License',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Manual Demand Generation','Manual License Demand Generation',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Trade License','Search an existing License',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create License Category','Create a License Category',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View License Category','View an existing License Category',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify License Category','Modify an existing License Category',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create License Sub Category','Create a License Sub Category',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify License Sub Category','Modify an existing License Sub Category',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View License Sub Category','View an existing License Sub Category',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Unit of Measurement','Modify an existing Unit of Measurement',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Unit of Measurement','Create Unit of Measurement',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Unit of Measurement','View an existing Unit of Measurement',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create License Fee Matrix','Create License Fee Structure',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View License Fee Matrix','View an existing License Fee Structure',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Document Type','Create License Document Type',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Document Type','Search License Document Type',
(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Penalty Rate','Create LIcense Penalty Rates',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create License Validity','Create License Validity',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify License Validity','Modify an existing License Validity',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View License Validity','View an existing License Validity',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'DCB Report By Trade','DCB Report By Trade',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Trade','View an existing Trade',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Legacy License','Modify an existing Legacy License',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Renew License','Renewal License',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collect Fees','Collect License Fee',(select id from eg_module  where name = 'Trade License'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Print Certificate','Print License Certificate',(select id from eg_module  where name = 'Trade License'));

--------------------------------------------------------End eg_feature---------------------------------------------------------------------



--------------------------------------------------------Start eg_feature_action-----------------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create New License') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-loadUomName') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'newTradeLicense-create') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Enter Trade License Action') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-loadUomName') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save Trade License') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewTradeLicense-view') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'demand-generate-create') ,(select id FROM eg_feature WHERE name = 'Manual Demand Generation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'save-demand-generate') ,(select id FROM eg_feature WHERE name = 'Manual Demand Generation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'demand-regenerate') ,(select id FROM eg_feature WHERE name = 'Manual Demand Generation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchTradeLicense') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'searchTrade-search') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewTradeLicense-view') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'viewTradeLicense-view') ,(select id FROM eg_feature WHERE name = 'View Trade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify-Legacy-License') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-loadUomName') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save-Modified-Legacy-License') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'New Trade License Before Renew') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-loadUomName') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'NewTradeLicense-renewal') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Trade License Bill Collect') ,(select id FROM eg_feature WHERE name = 'Collect Fees'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Trade License Generate Certificate') ,(select id FROM eg_feature WHERE name ='Print Certificate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create License Category') ,(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save License Category') ,(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify License Category') ,(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'saveEditedCategory') ,(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save License Category') ,(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View License Category') ,(select id FROM eg_feature WHERE name = 'View License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create License Category') ,(select id FROM eg_feature WHERE name = 'View License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create License SubCategory') ,(select id FROM eg_feature WHERE name = 'Create License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseAjaxMaster') ,(select id FROM eg_feature WHERE name = 'Create License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save License SubCategory') ,(select id FROM eg_feature WHERE name = 'Create License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify License SubCategory') ,(select id FROM eg_feature WHERE name = 'Modify License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'Modify License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save License SubCategory') ,(select id FROM eg_feature WHERE name = 'Modify License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View License SubCategory') ,(select id FROM eg_feature WHERE name = 'View License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseSubCategoryAjax') ,(select id FROM eg_feature WHERE name = 'View License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create License SubCategory') ,(select id FROM eg_feature WHERE name = 'View License Sub Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Unit Of Measurement') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'tradeLicenseAjaxMaster') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save UOM') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify Unit Of Measurement') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'editUnitOfMeasurement') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Save UOM') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Unit Of Measurement') ,(select id FROM eg_feature WHERE name = 'View Unit of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create-FeeMatrix') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-SubCategoryByParent') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-UnitOfMeasurementBySubCategory') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-FeeTypeBySubCategory') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search-FeeMatrix') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Delete Fee Matrix') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'feematrix-view') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-SubCategoryByParent') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Ajax-FeeTypeBySubCategory') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'feematrix-resultview') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create License Document Type') ,(select id FROM eg_feature WHERE name = 'Create Document Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Document Type') ,(select id FROM eg_feature WHERE name = 'Create Document Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search License Document Type') ,(select id FROM eg_feature WHERE name = 'Search Document Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Document Type') ,(select id FROM eg_feature WHERE name = 'Search Document Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Document Type') ,(select id FROM eg_feature WHERE name = 'Search Document Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'penaltyratescreate') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'penaltyratessearch') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'penaltyratessearcheditresult') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'deletepenaltyrate') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'New-Validity') ,(select id FROM eg_feature WHERE name = 'Create License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create-Validity') ,(select id FROM eg_feature WHERE name = 'Create License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View-Validity') ,(select id FROM eg_feature WHERE name = 'Create License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search and Edit-Validity') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search and Edit Result-Validity') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Edit-Validity') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Update-Validity') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Result-Validity') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Search and View-Validity') ,(select id FROM eg_feature WHERE name = 'View License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-Validity') ,(select id FROM eg_feature WHERE name = 'View License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View-Validity') ,(select id FROM eg_feature WHERE name = 'View License Validity'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TradeLicenseDCBReportLocalityWise') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));
 INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TLDCBReportList') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));


-----------------------------------------------------End eg_feature_action-------------------------------------------------------------

----------------------------------------------------Start eg_feature_role------------------------------------------------------------------

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create New License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create Legacy License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Manual Demand Generation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLApprover') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Collection Operator') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Search Trade License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'View Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify Legacy License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Renew License'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Collect Fees'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Collection Operator') ,(select id FROM eg_feature WHERE name = 'Collect Fees'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Print Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Print Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User'),(select id FROM eg_feature WHERE name = 'View License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin'),(select id FROM eg_feature WHERE name = 'View License Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User'),(select id FROM eg_feature WHERE name = 'Create License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin'),(select id FROM eg_feature WHERE name = 'Create License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View License Sub Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View Unit of Measurement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create License Fee Matrix'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View License Fee Matrix'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Document Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Search Document Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create Penalty Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Create License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Create License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'Modify License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'View License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'View License Validity'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='Super User') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLCreator') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLApprover') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='TLAdmin') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES ((select id from eg_role where name='ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'DCB Report By Trade'));

----------------------------------------------------End eg_feature_role---------------------------------------------------------------------

