------------------------------------ADDING FEATURE STARTS------------------------
--Masters
--Donation Master
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Sewerage Donation Master','Create Sewerage Tax Donation Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Sewerage Donation Master','View Sewerage Tax Donation Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Sewerage Donation Master','Modify Sewerage Tax Donation Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));

--Monthly Rate
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Sewerage Monthly Rates Master','Create Sewerage Tax Monthly Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Sewerage Monthly Rate','View Sewerage Tax Monthly Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Sewerage Monthly Rate','Modify Sewerage Tax Monthly Rates',(select id from eg_module  where name = 'Sewerage Tax Management'));

--Transactions
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Apply for New Sewerage Connection','Apply for New Sewerage Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Sewerage Connection','Search Sewerage Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify New Sewerage Connection','Modify Sewerage Tax Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Change in Closet','Create Change in Sewerage number of Closets',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Change in Closet','Modify Change in Sewerage number of Closets',(select id from eg_module  where name = 'Sewerage Tax Management'));

--Reports
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sewerage Daily Collection Report','Sewerage Tax Daily Collection Report',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sewerage Search Notice','Sewerage Tax Search Notice',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sewerage DCB Drill down report Ward wise','Sewerage Tax DCB report ward wise',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sewerage View DCB','Sewerage Tax view DCB Report',(select id from eg_module  where name = 'Sewerage Tax Management'));

--Collection
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bill Collection','Collection of Sewerage Charges',(select id from eg_module  where name = 'Sewerage Tax Management'));

------------------------------------ADDING FEATURE ENDS------------------------

------------------------------------ADDING FEATURE ACTION STARTS------------------------
--Masters
--Create Donation Master
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DonationMaster') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DonationMasterSuccess') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ValidateFromDateWithActiveRecord') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxExistingDonationMasterValidate') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));

--View Donation Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewDonationMaster') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'FromDateByPropertyType') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DonationMasterView') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Donation Master Search') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DonationMasterSuccess') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));

--Modify Donation Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyDonationMaster') ,(select id FROM eg_feature WHERE name = 'Modify Sewerage Donation Master'));

--Create Monthly Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateSewerageMonthlyRates') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxExistingSewerageValidate') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ValidateFromDateWithLatestActiveRecord') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetSewerageMonthlyRate') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));

--View Monthly Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageRateView') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage Rates Search') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'FromDateValuesByPropertyType') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewSewerageRatesMaster') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetSewerageMonthlyRate') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));

--Modify Monthly Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateSewerageRates') ,(select id FROM eg_feature WHERE name = 'Modify Sewerage Monthly Rate'));

--Transactions
-- Apply for new Connection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New Sewerage Connection') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Sewerage Connection') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckConnection') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSeerageClosetsCheck"') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckWaterTaxDue') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSewerageClosetsCheck') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));

--Search Connection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewSewerageConnection') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchSewerageConnection') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateSewerageApplicationDetails') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadFile') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'changeNumberOfClosets') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateSewerageChangeInClosets') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewSewerageConnectionDCBReport') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageChangeInClosets') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));

--Reports
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STDailyCollectionReport') ,(select id FROM eg_feature WHERE name = 'Sewerage Daily Collection Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchNotice') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STZipAndDownload') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STMergeAndDownload') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STNoticeSearchResult') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'STNoticeSearchResultCount') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ShowNotice') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage DCB Drill Down Report Wardwise') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DCBReportWardwiseList') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Sewerage DCB Report View Connections') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewSewerageConnectionDCBReport') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));


--Collection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchSewerageCharges') ,(select id FROM eg_feature WHERE name = 'Bill Collection'));

------------------------------------ADDING FEATURE ACTION ENDS------------------------


-------------------------------------ADDING FEATURE ROLE STARTS------------------------
--Masters
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'View Sewerage Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Sewerage Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Create Sewerage Monthly Rates Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'View Sewerage Monthly Rate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Sewerage Monthly Rate'));

--Transactions
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Apply for New Sewerage Connection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver') ,(select id FROM eg_feature WHERE name = 'Search Sewerage Connection'));

--Modify New Connection
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));

--Create Change in Closet
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Create Change in Closet'));

--Modify Change in Closet
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));

--Modify New Connection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'NewSewerageConnectionAck') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageTaxEstimationNotice') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageTaxWorkOrderNotice') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSewerageClosetsCheck') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageConnectionChangeInClosetsValidation') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateSewerageChangeInClosets') ,(select id FROM eg_feature WHERE name = 'Modify New Sewerage Connection'));

--Create Change in Closet
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageChangeInClosets') ,(select id FROM eg_feature WHERE name = 'Create Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageChangeInClosetsSuccess') ,(select id FROM eg_feature WHERE name = 'Create Change in Closet'));

--Modify Change in Closet
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'NewSewerageConnectionAck') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageTaxEstimationNotice') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageTaxWorkOrderNotice') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSewerageClosetsCheck') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageConnectionChangeInClosetsValidation') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateSewerageChangeInClosets') ,(select id FROM eg_feature WHERE name = 'Modify Change in Closet'));

--Reports
--DCR
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Sewerage Daily Collection Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Sewerage Daily Collection Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Sewerage Daily Collection Report'));

--Search Notice
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Sewerage Search Notice'));

--DCB Wardwise
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Sewerage DCB Drill down report Ward wise'));

--View DCB
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Sewerage View DCB'));

--Collection
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Bill Collection'));

-------------------------------------ADDING FEATURE ROLE ENDS------------------------
