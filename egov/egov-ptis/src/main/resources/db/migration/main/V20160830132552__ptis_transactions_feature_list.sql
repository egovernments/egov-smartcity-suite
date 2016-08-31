-------------------------------------Property Tax Transactions Feature List------------------------------------------

--New Property
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create New Property','Create a new property', (select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View New Property','View a new property', (select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify New Property','Modify a new property', (select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve New Property','Approve a new property', (select id from eg_module  where name = 'Property Tax'));

--Digital Signature 
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Digital Signature','Provides access to do digital signature', (select id from eg_module  where name = 'Property Tax'));

--Addition/Alteration of Assessment
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Addition/Alteration of Assessment','Create an addition/alteration of assessment',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Addition/Alteration of Assessment','View an addition/alteration of assessment',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Addition/Alteration of Assessment','Modify an addition/alteration of assessment',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Addition/Alteration of Assessment','Approve an addition/alteration of assessment',
(select id from eg_module  where name = 'Property Tax'));

--Transfer Ownership
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Title Transfer','Create a title transfer',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Title Transfer','View a title transfer',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Title Transfer','Modify a title transfer',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Title Transfer','Approve a title transfer',
(select id from eg_module  where name = 'Property Tax'));

--Revision Petition
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Revision Petition','Create a revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Inspect Revision Petition','Inspect a revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Verify Revision Petition','Verify a revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Revision Petition','Approve a revision petition',
(select id from eg_module  where name = 'Property Tax'));

--General Revision Petition
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create General Revision Petition','Create a general revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View General Revision Petition','View a general revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify General Revision Petition','Modify a general revision petition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve General Revision Petition','Approve a general revision petition',
(select id from eg_module  where name = 'Property Tax'));

--Bifurcation
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Bifurcation','Create a bifurcation',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Bifurcation','View a bifurcation',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Bifurcation','Modify a bifurcation',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Bifurcation','Approve a bifurcation',
(select id from eg_module  where name = 'Property Tax'));

--Tax Exemption
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Tax Exemption','Create a tax exemption',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Tax Exemption','View a tax exemption',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Tax Exemption','Modify a tax exemption',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Tax Exemption','Approve a tax exemption',
(select id from eg_module  where name = 'Property Tax'));

--Property Demolition
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Property Demolition','Create a property demolition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Property Demolition','View a property demolition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Property Demolition','Modify a property demolition',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Property Demolition','Approve a property demolition',
(select id from eg_module  where name = 'Property Tax'));

--Vacancy Remission
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Vacancy Remission','Create a vacancy remission',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Vacancy Remission','View a vacancy remission',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Vacancy Remission','Modify a vacancy remission',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Vacancy Remission','Approve a vacancy remission',
(select id from eg_module  where name = 'Property Tax'));

--Data Entry for Property Tax
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Data Entry for Property Tax','Data entry screen for property tax', (select id from eg_module  where name = 'Property Tax'));

--Edit Demand for Property Tax Data Entry
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Demand for Property Tax Data Entry','Edit demand for properties created through data entry', (select id from eg_module  where name = 'Property Tax'));

--Edit Collection for Property Tax
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Collection for Property Tax','Edit collection for properties', (select id from eg_module  where name = 'Property Tax'));

--Search Notice
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Property Tax Notice','Search a notice',
(select id from eg_module  where name = 'Property Tax'));

--Demand Bill Generation Status
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Demand Bill Generation Status','Ward wise demand bill generation status',
(select id from eg_module  where name = 'Property Tax'));

--Generate Demand Bill
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Generate Demand Bill','Generate demand bills',
(select id from eg_module  where name = 'Property Tax'));

--Edit Owner Details
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Owner Details','Edit owner details',
(select id from eg_module  where name = 'Property Tax'));

--****Master Screens****
--Rebate Period

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Rebate Period','Master screen for creating rebate period',
(select id from eg_module  where name = 'Property Tax')); 

--Usage Type
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Usage Type','Master screen for creating usage type',
(select id from eg_module  where name = 'Property Tax'));

--View Tax Rates
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Tax Rates','Master screen to view tax rates',
(select id from eg_module  where name = 'Property Tax'));

--Roof Type
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Roof Type','Create a roof type',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Roof Type','View a roof type',
(select id from eg_module  where name = 'Property Tax'));

--Wood Type
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Wood Type','Create a wood type',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Wood Type','View a wood type',
(select id from eg_module  where name = 'Property Tax'));

--Floor Type
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Floor Type','Create a floor type',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Floor Type','View a floor type',
(select id from eg_module  where name = 'Property Tax'));

--Wall Type
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Wall Type','Create a wall type',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Wall Type','View a wall type',
(select id from eg_module  where name = 'Property Tax'));

--Unit Rates
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Unit Rates','Create unit rates',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Unit Rates','View unit rates',
(select id from eg_module  where name = 'Property Tax'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Unit Rates','Update unit rates',
(select id from eg_module  where name = 'Property Tax'));

--Add Demand for Properties
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add Demand for Properties','Add demand for migrated and data entry properties',
(select id from eg_module  where name = 'Property Tax'));

--Search Property
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Property','Search a property',
(select id from eg_module  where name = 'Property Tax'));

--View Property
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Property','View a property', (select id from eg_module  where name = 'Property Tax'));

--View DCB
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View DCB','View DCB for a property', (select id from eg_module  where name = 'Property Tax'));

--Edit Data entry for Property Tax
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Data Entry for Property Tax','Edit Data Entry properties', (select id from eg_module  where name = 'Property Tax'));

---------------------------------------------Feature List Ends-------------------------------------------------------

---------------------------------------------Feature Action Starts---------------------------------------------------
--New Property
--Create New Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Property Submit') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Property') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'createPropertyAckPrint') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Wards') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Streets') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Create New Property'));

--View New Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'View New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'View New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'View New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View New Property'));

--Modify New Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Wards') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));

--Approve New Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Approve Property') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));

--Digital Signature

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'digitalSignature-PropertyTaxTransitionWorkflow') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'digitalSignature-downloadSignedNotice') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'digitalSignature-priveviewSignedNotice') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Pending Digital Signature') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));

--Addition Alteration
--Create Addition/Alteration of Assessment

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AlterAssessment-Form') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Form') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Action') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'modifyPropertyAckPrint') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));

--View Addition/Alteration of Assessment

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));

--Modify Addition/Alteration of Assessment

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));

--Approve Addition/Alteration of Assessment

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Approve Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));

--Title Transfer
--Create Title Transfer

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Form') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Save') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property View') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Forward') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Reject') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Notice Transfer Property') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transferee Delete') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Acknowledgement Transfer Property') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Calculate Mutation Fee') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Mutation Fee Payment Generate') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Mutation Fee Payment Search') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TransferOwnership-Form') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TitleTransfer-redirect') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateMutationFees') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));

--View Title Transfer

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property View') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Forward') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Reject') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));

--Modify Title Transfer

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property View') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Forward') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Reject') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transferee Delete') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Calculate Mutation Fee') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'calculateMutationFees') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));

--Approve Title Transfer

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property View') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Approve') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Forward') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Notice Transfer Property') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Transfer Property Reject') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Transfer Success Redirect') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));

--Bifurcation
--Create Bifurcation

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'BifurcateAssessment-Form') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Property Submit') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'createPropertyAckPrint') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Wards') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Streets') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));

--View Bifurcation

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));

--Modify Bifurcation

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Wards') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));

--Approve Bifurcation

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Approve Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Open inbox') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));

--General Revision Petition
--Create General Revision Petition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'GeneralRevisionPetition-Form') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Form') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Action') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'modifyPropertyAckPrint') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));

--View General Revision Petition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));

--Modify General Revision Petition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));

--Approve General Revision Petition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Approve Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));

--Tax Exemption
--Create Tax Exemption

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TaxExemption-Form') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Exemption Form') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PrintMeesevaReceiptForexemption') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));

--View Tax Exemption

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Exemption update') ,(select id FROM eg_feature WHERE name = 'View Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'View Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'View Tax Exemption'));

--Modify Tax Exemption

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Exemption update') ,(select id FROM eg_feature WHERE name = 'Modify Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Tax Exemption'));

--Approve Tax Exemption

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Exemption update') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));

--Property Demolition
--Create Property Demolition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'TaxExemption-Form') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Demolition Form') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));

--View Property Demolition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Demolition update') ,(select id FROM eg_feature WHERE name = 'View Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'View Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'View Property Demolition'));

--Modify Property Demolition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Demolition update') ,(select id FROM eg_feature WHERE name = 'Modify Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Property Demolition'));

--Approve Property Demolition

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Demolition update') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));

--Vacancy Remission
--Create Vacancy Remission

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'VacancyRemission-Form') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'vacancyRemissionCreate') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PrintMeesevaReceiptForVacancyRem') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'vacancyRemissionRejectionAck') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));

--View Vacancy Remission

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'vacancyRemissionUpdate') ,(select id FROM eg_feature WHERE name = 'View Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'View Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'View Vacancy Remission'));

--Modify Vacancy Remission

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'vacancyRemissionUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Modify Vacancy Remission'));

--Approve Vacancy Remission

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'vacancyRemissionUpdate') ,(select id FROM eg_feature WHERE name = 'Approve Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Vacancy Remission'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Approve Vacancy Remission'));

--Data Entry for Property Tax

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Data Entry Screen') ,(select id FROM eg_feature  WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Modify Data Entry Screen'),(select id FROM eg_feature  WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Modify Data Entry Submit'),(select id FROM eg_feature  WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Create Data Entry Screen'),(select id FROM eg_feature  WHERE name = 'Data Entry for Property Tax'));

--Edit Demand for Property Tax Data Entry

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Demand for Data Entry form'),(select id FROM eg_feature  WHERE name = 'Edit Demand for Property Tax Data Entry'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Demand Form'),(select id FROM eg_feature  WHERE name = 'Edit Demand for Property Tax Data Entry'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Demand Update'),(select id FROM eg_feature  WHERE name = 'Edit Demand for Property Tax Data Entry'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Demand submit'),(select id FROM eg_feature  WHERE name = 'Edit Demand for Property Tax Data Entry'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Demand Update Form'),(select id FROM eg_feature  WHERE name = 'Edit Demand for Property Tax Data Entry'));

--Edit Collection for Property Tax

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EditCollection-Form'),(select id FROM eg_feature  WHERE name = 'Edit Collection for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EditCollection-update'),(select id FROM eg_feature  WHERE name = 'Edit Collection for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EditCollection-editForm'),(select id FROM eg_feature  WHERE name = 'Edit Collection for Property Tax'));

--Search Property Tax Notice

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Notice'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchNoticeSearchResult'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchNoticeMergeAndDownload'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchNoticeZipAndDownload'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchNoticeReset'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'SearchNoticeShowNotice'),(select id FROM eg_feature  WHERE name = 'Search Property Tax Notice'));

--Demand Bill Generation Status

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Bill generation'),(select id FROM eg_feature  WHERE name = 'Demand Bill Generation Status'));

--Generate Demand Bill

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'GenerateDemandBill-Form'),(select id FROM eg_feature  WHERE name = 'Generate Demand Bill'));

--Edit Owner Details

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Owner Details'),(select id FROM eg_feature  WHERE name = 'Edit Owner Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Owner'),(select id FROM eg_feature  WHERE name = 'Edit Owner Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Owner submit'),(select id FROM eg_feature  WHERE name = 'Edit Owner Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Edit Owner Details Form'),(select id FROM eg_feature  WHERE name = 'Edit Owner Details'));

--Master Screens

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Rebate Master'),(select id FROM eg_feature  WHERE name = 'Create Rebate Period'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Usage Master'),(select id FROM eg_feature  WHERE name = 'Create Usage Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Usage Master List'),(select id FROM eg_feature  WHERE name = 'Create Usage Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'ViewTaxRates'),(select id FROM eg_feature  WHERE name = 'View Tax Rates'));

--Roof Type
--Create

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Roof Type'),(select id FROM eg_feature  WHERE name = 'Create Roof Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Roof Type'),(select id FROM eg_feature  WHERE name = 'Create Roof Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Roof Type'),(select id FROM eg_feature  WHERE name = 'Create Roof Type'));

--View

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Roof Type'),(select id FROM eg_feature  WHERE name = 'Create Roof Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Roof Type'),(select id FROM eg_feature  WHERE name = 'Create Roof Type'));

--Wood Type
--Create

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Wood Type'),(select id FROM eg_feature  WHERE name = 'Create Wood Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wood Type'),(select id FROM eg_feature  WHERE name = 'Create Wood Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wood Type'),(select id FROM eg_feature  WHERE name = 'Create Wood Type'));

--View

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wood Type'),(select id FROM eg_feature  WHERE name = 'Create Wood Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wood Type'),(select id FROM eg_feature  WHERE name = 'Create Wood Type'));

--Floor Type
--Create

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Floor Type'),(select id FROM eg_feature  WHERE name = 'Create Floor Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Floor Type'),(select id FROM eg_feature  WHERE name = 'Create Floor Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Floor Type'),(select id FROM eg_feature  WHERE name = 'Create Floor Type'));

--View

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Floor Type'),(select id FROM eg_feature  WHERE name = 'Create Floor Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Floor Type'),(select id FROM eg_feature  WHERE name = 'Create Floor Type'));

--Wall Type
--Create

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Create Wall Type'),(select id FROM eg_feature  WHERE name = 'Create Wall Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wall Type'),(select id FROM eg_feature  WHERE name = 'Create Wall Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wall Type'),(select id FROM eg_feature  WHERE name = 'Create Wall Type'));

--View

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wall Type'),(select id FROM eg_feature  WHERE name = 'Create Wall Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wall Type'),(select id FROM eg_feature  WHERE name = 'Create Wall Type'));

--Unit Rates
--View

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Unit Rate View'),(select id FROM eg_feature  WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Unit rate Submit'),(select id FROM eg_feature  WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Unit rate'),(select id FROM eg_feature  WHERE name = 'View Unit Rates'));

--Create

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Unit Rate Master'),(select id FROM eg_feature  WHERE name = 'Create Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Unit Rate Create'),(select id FROM eg_feature  WHERE name = 'Create Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search owner'),(select id FROM eg_feature  WHERE name = 'Create Unit Rates'));

--Update

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search unit rate edit'),(select id FROM eg_feature  WHERE name = 'Update Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Unit rate Submit'),(select id FROM eg_feature  WHERE name = 'Update Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Deactivate Unit rate'),(select id FROM eg_feature  WHERE name = 'Update Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Unit Rate Master'),(select id FROM eg_feature  WHERE name = 'Update Unit Rates'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Update Unit rate'),(select id FROM eg_feature  WHERE name = 'Update Unit Rates'));

--Add Demand for Properties

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Add/Edit DCB'),(select id FROM eg_feature  WHERE name = 'Add Demand for Properties'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Add/Edit DCB Form'),(select id FROM eg_feature  WHERE name = 'Add Demand for Properties'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Add/Edit DCB Update Form'),(select id FROM eg_feature  WHERE name = 'Add Demand for Properties'));

--Search Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Index') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Bndry') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Area') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Location') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Demand') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Assessment') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Assessment-commonSearch') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Door No') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Property By Mobile No') ,(select id FROM eg_feature WHERE name = 'Search Property'));

--View Property

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Property') ,(select id FROM eg_feature WHERE name = 'View Property'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTISVIEWDOC') ,(select id FROM eg_feature WHERE name = 'View Property'));

--View DCB

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View DCB Property') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View DCB Property Display') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Headwise DCB') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View MigData on DCB') ,(select id FROM eg_feature WHERE name = 'View DCB'));

--Edit Data Entry for Property Tax

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Edit Data Entry Screen'),(select id FROM eg_feature  WHERE name = 'Edit Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'EditDataEntry-Form'),(select id FROM eg_feature  WHERE name = 'Edit Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PTIS-Save Edit Data Entry Screen'),(select id FROM eg_feature  WHERE name = 'Edit Data Entry for Property Tax'));
----------------------------------------------Feature Action Ends----------------------------------------------------

----------------------------------------------Feature Role Mapping Starts--------------------------------------------

--New Property
--Create New Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create New Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create New Property'));

--View New Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View New Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View New Property'));

--Modify New Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify New Property'));

--Approve New Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve New Property'));

-- Digital Signature

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Digital Signature'));

--Addition/Alteration
--Create Addition/Alteration of Assessment

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Addition/Alteration of Assessment'));

--View Addition/Alteration of Assessment

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Addition/Alteration of Assessment'));

--Modify Addition/Alteration of Assessment

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Addition/Alteration of Assessment'));

--Approve Addition/Alteration of Assessment

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Addition/Alteration of Assessment'));

--Title Transfer
--Create Title Transfer

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Title Transfer'));

--View Title Transfer

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Title Transfer'));

--Modify Title Transfer

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Title Transfer'));

--Approve Title Transfer

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Title Transfer'));

--Bifurcation
--Create Bifurcation

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));

--View Bifurcation

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));

--Modify Bifurcation

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));

--Approve Bifurcation

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));

--General Revision Petition
--Create General Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create General Revision Petition'));

--View General Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View General Revision Petition'));

--Modify General Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify General Revision Petition'));

--Approve General Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve General Revision Petition'));

--Tax Exemption
--Create Tax Exemption

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Tax Exemption'));

--View Tax Exemption

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Tax Exemption'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Tax Exemption'));

--Modify Tax Exemption

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Tax Exemption'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Tax Exemption'));

--Approve Tax Exemption

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));

--Property Demolition
--Create Property Demolition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));

--View Property Demolition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Property Demolition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Property Demolition'));

--Modify Property Demolition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Property Demolition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Property Demolition'));

--Approve Property Demolition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));

--Vacancy Remission
--Create Vacancy Remission

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Vacancy Remission'));

--View Vacancy Remission

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Vacancy Remission'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Vacancy Remission'));

--Modify Vacancy Remission

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Vacancy Remission'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Modify Vacancy Remission'));

--Approve Vacancy Remission

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Vacancy Remission'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Vacancy Remission'));


--Search Property Tax Notice

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Search Property Tax Notice'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Property Tax Notice'));

--Demand Bill Generation Status

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Demand Bill Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Demand Bill Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Demand Bill Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Demand Bill Generation Status'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Demand Bill Generation Status'));

--Generate Demand Bill

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Generate Demand Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Generate Demand Bill'));

--Master Screens
--Rebate Master

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Rebate Period'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Rebate Period'));

--Usage Master

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Usage Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Usage Type'));

--View Tax Rates

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Tax Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Tax Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Tax Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Tax Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Tax Rates'));

--Roof Type
--Create

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Roof Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Roof Type'));

--View

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Roof Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Roof Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Roof Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Roof Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Roof Type'));

--Wood Type
--Create

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Wood Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Wood Type'));

--View

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Wood Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Wood Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Wood Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Wood Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Wood Type'));

--Wall Type
--Create

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Wall Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Wall Type'));

--View

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Wall Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Wall Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Wall Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Wall Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Wall Type'));

--Floor Type
--Create

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Floor Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Floor Type'));

--View

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Floor Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Floor Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Floor Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Floor Type'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Floor Type'));

--Unit Rate Master
--Create

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Unit Rates'));

--Update

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Update Unit Rates'));

--View

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Unit Rates'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Unit Rates'));

--Revision Petition
--Create Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Revision Petition'));

--Inspect Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Inspect Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Inspect Revision Petition'));

--Verify Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Verify Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Verify Revision Petition'));

--Approve Revision Petition

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Approve Revision Petition'));

--Search Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'Search Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Property'));

--View Property

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View Property'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View Property'));

--View DCB

INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Approver') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'Property Verifier') ,(select id FROM eg_feature WHERE name = 'View DCB'));
INSERT INTO eg_feature_role (ROLE, FEATURE) 
VALUES 
((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'View DCB'));

--------------------------------------------Feature Role Mapping Ends------------------------------------------------
