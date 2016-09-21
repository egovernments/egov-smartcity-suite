
--View Roof Type
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Roof Type'),(select id FROM eg_feature  WHERE name = 'View Roof Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Roof Type'),(select id FROM eg_feature  WHERE name = 'View Roof Type'));

--View Wood Type
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wood Type'),(select id FROM eg_feature  WHERE name = 'View Wood Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wood Type'),(select id FROM eg_feature  WHERE name = 'View Wood Type'));

--View Floor Type
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Floor Type'),(select id FROM eg_feature  WHERE name = 'View Floor Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Floor Type'),(select id FROM eg_feature  WHERE name = 'View Floor Type'));

--View Wall Type
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Wall Type'),(select id FROM eg_feature  WHERE name = 'View Wall Type'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Wall Type'),(select id FROM eg_feature  WHERE name = 'View Wall Type'));

--Demolition
delete from eg_feature_action where action in (select id FROM eg_action  WHERE name = 'TaxExemption-Form') and feature in (select id from eg_feature where name='Create Property Demolition');

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Demolition') ,(select id FROM eg_feature WHERE name = 'Create Property Demolition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve Property Demolition'));

--Tax Exemption
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve Tax Exemption'));

--Unit Rate
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Search Unit rate Submit'),(select id FROM eg_feature  WHERE name = 'Create Unit Rates'));

--Data Entry for Property Tax
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature  WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Populate Categories by Property Type') ,(select id FROM eg_feature WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Usage by type') ,(select id FROM eg_feature WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'User By Mobile number') ,(select id FROM eg_feature WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Locality') ,(select id FROM eg_feature WHERE name = 'Data Entry for Property Tax'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Load Block By Ward') ,(select id FROM eg_feature WHERE name = 'Data Entry for Property Tax'));

--Generate Demand Bill
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Generate Property Tax bill'),(select id FROM eg_feature  WHERE name = 'Generate Demand Bill'));

--Revision Petition
--Create Revision Petition
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'RevisionPetition-Form'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition New'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition Action'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition View'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition print hearingnotice'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition generate hearingnotice'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition Special notice'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown'),(select id FROM eg_feature  WHERE name = 'Create Revision Petition'));

--Inspect Revision Petition
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition View'),(select id FROM eg_feature  WHERE name = 'Inspect Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition record inspection'),(select id FROM eg_feature  WHERE name = 'Inspect Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown'),(select id FROM eg_feature  WHERE name = 'Inspect Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown'),(select id FROM eg_feature  WHERE name = 'Inspect Revision Petition'));

--Verify Revision Petition
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition View'),(select id FROM eg_feature  WHERE name = 'Verify Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition reject inspection'),(select id FROM eg_feature  WHERE name = 'Verify Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition verify inspection'),(select id FROM eg_feature  WHERE name = 'Verify Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown'),(select id FROM eg_feature  WHERE name = 'Verify Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown'),(select id FROM eg_feature  WHERE name = 'Verify Revision Petition'));

--Approve Revision Petition
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Property Tax Rev Petition View'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition Add hearing'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition reject inspection'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition Outcome'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition endoresement notice'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'PropTax Rev Petition Special notice'),(select id FROM eg_feature  WHERE name = 'Approve Revision Petition'));

--Bifurcation
delete from eg_feature_action where feature in (select id from eg_feature where name='Create Bifurcation');
delete from eg_feature_action where feature in (select id from eg_feature where name='View Bifurcation');
delete from eg_feature_action where feature in (select id from eg_feature where name='Modify Bifurcation');
delete from eg_feature_action where feature in (select id from eg_feature where name='Approve Bifurcation');

--Create Bifurcation
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'BifurcateAssessment-Form') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Form') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Modify Property Action') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'modifyPropertyAckPrint') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
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
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AadhaarInfo') ,(select id FROM eg_feature WHERE name = 'Create Bifurcation'));

--View Bifurcation
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'View Bifurcation'));

--Modify Bifurcation
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
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
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Modify Bifurcation'));

--Approve Bifurcation
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Approve Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Forward View Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'Reject Modify Property') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxDesignationDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'AjaxApproverDropdown') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'generateSpecialNotice') ,(select id FROM eg_feature WHERE name = 'Approve Bifurcation'));
