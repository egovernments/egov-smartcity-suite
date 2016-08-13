------------------------------------ADDING FEATURE STARTS------------------------
--Masters
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Contractor','Create a Contractor',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Contractor','Modify an Contractor',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Contractor','View an Contractor',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Contractor Grade','Create a Contractor Grade',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Contractor Grade','Modify an Contractor Grade',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Contractor Grade','View an Contractor Grade',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Milestone Template','Create a Milestone Template',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Milestone Template','Modify an Milestone Template',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Milestone Template','View an Milestone Template',(select id from EG_MODULE where name = 'Works Management'));

--Line Estimate
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Line Estimate','Create a Line Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Line Estimate','Search Line Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Spillover Line Estimate','Create a Spillover Line Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Line Estimate','Update an Line Estimate',(select id from EG_MODULE where name = 'Works Management'));

--Letter of Acceptance
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Letter of Acceptance','Create a Letter of Acceptance',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search LOA','Search LOA',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify LOA','Modify LOA',(select id from EG_MODULE where name = 'Works Management'));

--Milestone
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Milestone','Create a Milestone',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Milestone','Search Milestone',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Track Milestone','Track a Milestone',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Tracked Milestone','Search Tracked Milestone',(select id from EG_MODULE where name = 'Works Management'));

--Contractor Bill
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Contractor Bill','Create a Contractor Bill',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Contractor Bill','Search Contractor Bill',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Contractor Bill','Update Contractor Bill',(select id from EG_MODULE where name = 'Works Management'));

--Reports
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Work Progress Register','Work Progress Register',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Estimate Appropriation Register Report','Estimate Appropriation Register Report',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Estimate Abstract Report By Department','Estimate Abstract Report By Department',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Estimate Abstract Report By Type Of Work','Estimate Abstract Report By Type Of Work',(select id from EG_MODULE where name = 'Works Management'));

--Cancel Screens
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Line Estimate','Cancel Line Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel LOA','Cancel LOA',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Milestone/Track Milestone','Cancel Milestone/Track Milestone',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Contractor Bill','Cancel Contractor Bill',(select id from EG_MODULE where name = 'Works Management'));

------------------------------------ADDING FEATURE ENDS------------------------

------------------------------------ADDING FEATURE ACTION STARTS------------------------
--Masters
--contractor
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateContractor') ,(select id FROM eg_feature WHERE name = 'Create Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractor') ,(select id FROM eg_feature WHERE name = 'Create Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksContractorSearch') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksContractorSearchResult') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditContractor') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractor') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewContractor') ,(select id FROM eg_feature WHERE name = 'View Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksContractorSearchResult') ,(select id FROM eg_feature WHERE name = 'View Contractor'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditContractor') ,(select id FROM eg_feature WHERE name = 'View Contractor'));

--contractor grade
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Contractor Grade') ,(select id FROM eg_feature WHERE name = 'Create Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'Create Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksContractorGradeViewEdit') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewContractorGrade') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditContractorGradeMaster') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));

--milestone template
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Milestone Template') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateSave') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateSearch') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateEdit') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateSearchDetail') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateView') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewMilestoneTemplateSearch') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateEdit') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateSearchDetail') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));


--Line estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateLineEstimateNewForm') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxGetWard') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'FunctionsByFundIdAndDepartmentId') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLoadBudgetHeadByFunction') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubScheme') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksLineEstimateSuccess') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateNewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLineEstimateForm') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Admin Sanction Numbers') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Line Estimate Numbers') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search LineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'FunctionsByFundIdAndDepartmentId') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLoadBudgetHeadByFunction') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxGetWard') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateSpillOverLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'FunctionsByFundIdAndDepartmentId') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLoadBudgetHeadByFunction') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubScheme') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSpillOverLineEstimateForm') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSpillOverLineEstimateAuthorityNames') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateSpillOverLineEstimateSuccess') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubScheme') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateLineEstimate') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksLineEstimateSuccess') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));



--Letter of acceptance
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLineEstimatesToCreateLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Admin Sanction Numbers For Loa') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Line Estimate Numbers For Loa') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Work Identification Numbers For Loa') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLineEstimateForLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateLetterOfAcceptanceNewForm') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLOAContractor') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveLOASuccess') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorCodesForLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxContractorForLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsForLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLetterOfAcceptanceForm') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search LOA NUMBER') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractor For LOA') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For LOA') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search LOA'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksModifyLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLetterOfAcceptanceModifyForm') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractor For LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search LOA NUMBER') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLetterOfAcceptanceToModify') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));


--Milestone
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateTrackMilestoneSearch') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumberForMilestone') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For LOA') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkOrderNumberForMilestone') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLOAForMilestone') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateMilestoneForm') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMilestoneTemplateDetails') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneTemplateDetails') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMilestoneTemplate') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMilestoneTemplateCode') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveMilestone') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AddMilestoneTemplateActivity') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestone') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchAndViewMilestone') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMilestoneToTrack') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMilestone') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'TrackMilestone') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewTrackMilestone') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'TrackedMilestoneView') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchWINForTrackMilestone') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxViewTrackMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));

--contractor bill
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLOAToCreateContractorBillForLOA') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateContractorBillNewForm') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLOANumberForContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorNameforContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateNumbersForContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLetterOfAcceptanceToCreateContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractorBillSuccess') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorNameForContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchWorkIdentificationNumberForContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateContractorBillRegister') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveContractorBillSuccess') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));



--reports
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchWorkProgressRegister') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWinCodesToSearchWorkProgressRegister') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchWorkProgressRegister') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorkProgressRegisterPdf') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EstimateAppropriationRegisterReport') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxGetBudgetHeadByFunction') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEstimateAppropriationRegisterReport') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EstimateAppropriationRegisterPdf') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubScheme') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchEstimateAbstractReportByDepartmentWise') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetFinancialYear') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateAbstractReportByDepartmentWise') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EstimateAbstractReportByDepartmentWisePdf') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubScheme') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetFinancialYear') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EstimateAbstractReportByTypeOfWorkWisePdf') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));

--cancel screens
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheackCreatedLOA') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLineEstimateToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Line Estimate Numbers') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLoadCreatedBy') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelLineEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLineEstimateToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchContractorBillToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxBillNumbersToCancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorBillsToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckIfBillsCreatedToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMilestoneToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMilestonesToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLOANumbersToCancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsToCancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLOAToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLOAsToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));

------------------------------------ADDING FEATURE ACTION ENDS------------------------

------------------------------------ADDING FEATURE ROLE STARTS------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Create Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Create Contractor'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Modify Contractor'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'View Contractor'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'View Contractor'));

--contractor grade
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Create Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Create Contractor Grade'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Modify Contractor Grade'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'View Contractor Grade'));

--milestone template
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Create Milestone Template'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'View Milestone Template'));

--letter of acceptance
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search LOA'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));

--milestone
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));

--lineestimate
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Update Line Estimate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));

--contractor bill
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));

--Reports
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Work Progress Register'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Estimate Appropriation Register Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Department'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Estimate Abstract Report By Type Of Work'));

--cancel screens
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
