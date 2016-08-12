------------------------------------ADDING FEATURE STARTS------------------------
--Masters
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Estimate Template','Create a Estimate Template',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Estimate Template','Modify an Estimate Template',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Estimate Template','View an Estimate Template',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Schedule Category','Create a Schedule Category',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Schedule Category','Modify an Schedule Category',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Schedule Category','View an Schedule Category',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Rate Master','Create a Rate Master',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Rate Master','Modify an Rate Master',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Rate Master','View an Rate Master',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Overhead','Create a Overhead',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Overhead','Modify an Overhead',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Overhead','View an Overhead',(select id from EG_MODULE where name = 'Works Management'));

--Abstract Estimate
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Abstract Estimate','Create a Abstract Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Abstract Estimate','Search Abstract Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Abstract Estimate','Update Abstract Estimate',(select id from EG_MODULE where name = 'Works Management'));

--Letter of Acceptance
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update LOA','Update LOA',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modfiy LOA','Modfiy an LOA',(select id from EG_MODULE where name = 'Works Management'));

--Offline Status
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Offline Status for Abstract Estimate','Set Offline status for Abstract Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Offline Status for Letter Of Acceptance','Set Offline Status for Letter Of Acceptance',(select id from EG_MODULE where name = 'Works Management'));

--MB
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Abstract MB','Create a Abstract MB',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Measurement Book','Search Measurement Book',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Measurement Book','Update Measurement Book',(select id from EG_MODULE where name = 'Works Management'));

--Cancel Screens
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Abstract Estimate','Cancel Abstract Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Abstract MB','Cancel Abstract MB',(select id from EG_MODULE where name = 'Works Management'));

------------------------------------ADDING FEATURE ENDS------------------------

------------------------------------ADDING FEATURE ACTION STARTS------------------------
--Masters
--estimate template
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Estimate Template') ,(select id FROM eg_feature WHERE name = 'Create Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'Create Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateSORSearchAjax') ,(select id FROM eg_feature WHERE name = 'Create Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateFindSORAjax') ,(select id FROM eg_feature WHERE name = 'Create Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveEstimateTemplate') ,(select id FROM eg_feature WHERE name = 'Create Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateTemplateSearch') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateTemplateSearchResult') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyEstimateTemplate') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveEstimateTemplate') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateSORSearchAjax') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateFindSORAjax') ,(select id FROM eg_feature WHERE name = 'Modify Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'View Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewEstimateTemplateSearchResult') ,(select id FROM eg_feature WHERE name = 'View Estimate Template'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewEstimateTemplateMaster') ,(select id FROM eg_feature WHERE name = 'View Estimate Template'));

--schedule category
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Schedule Category') ,(select id FROM eg_feature WHERE name = 'Create Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveScheduleCategory') ,(select id FROM eg_feature WHERE name = 'Create Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyScheduleCategoryMaster') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ScheduleCategorySearchResult') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchScheduleCategoryName') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksScheduleCategoryEdit') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveScheduleCategory') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleCategoryMaster') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchScheduleCategoryName') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ScheduleCategorySearchResult') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleCategory') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));

--Schedule of rate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Schedule Of Rate') ,(select id FROM eg_feature WHERE name = 'Create Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Create Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksScheduleOfRateSearch') ,(select id FROM eg_feature WHERE name = 'Modify Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Modify Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Modify Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Modify Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'Modify Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));

--Overhead
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Overhead') ,(select id FROM eg_feature WHERE name = 'Create Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SuccessOverhead') ,(select id FROM eg_feature WHERE name = 'Create Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveOverhead') ,(select id FROM eg_feature WHERE name = 'Create Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadToModify') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadName') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadForView') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ModifyOverhead') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveOverhead') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SuccessOverhead') ,(select id FROM eg_feature WHERE name = 'Modify Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadToView') ,(select id FROM eg_feature WHERE name = 'View Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadName') ,(select id FROM eg_feature WHERE name = 'View Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOverheadForView') ,(select id FROM eg_feature WHERE name = 'View Overhead'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewOverhead') ,(select id FROM eg_feature WHERE name = 'View Overhead'));


--cancel screens
--cancel abstract MB
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMBToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLoaNumbersToCancelMB') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchWorkIdNumbersToCancelMB') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchContractorsToCancelMB') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMBToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeasurementBookPDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelMB') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));

--cancel abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='AjaxSearchEstimateToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='SearchEstimateToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='SearchEstimateNumbersToCancelEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='CancelEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract Estimate'));


--line Estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SchemesByFundId') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SchemesByFundId') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search Line Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Line Estimate'));

--abstract estimate
--create abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateSubTypeAjax') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLineEstimatesToCreateAE') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SchemesByFundId') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateAbstractEstimateForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEstimateTemplateSearchResult') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxGetEstimateTemplateById') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEstimateTemplateByCode') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchSORsForEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Asset-Show-Search-Page') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateAbstractEstimateForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateAbstractEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateSuccessPage') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateGetDesignationByAuthority') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateAbstractEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));

--search abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateSearchForm') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='AbstractEstimateAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetAbstractEstimatesByNumber') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Search Abstract Estimate'));


--update abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateAbstractEstimate') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAbstractEstimateSuccessPage') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));

--letter of acceptance
--create LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchAbstractEstimatesToCreateLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For Create LOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Admin Sanction Number To Create LOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Work Identification Numbers To Create LOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchAbstractEstimateForLOA') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksLetterOfAcceptanceSuccessPage') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Create Letter of Acceptance'));

--update LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Update LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksLetterOfAcceptanceSuccessPage') ,(select id FROM eg_feature WHERE name = 'Update LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Update LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Update LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Update LOA'));

--Search LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search LOA'));

--Modify LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSaveLOASuccess') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));

--offline status
--OS for abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchAbstractEstimateToSetOfflineStattus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxAbstractEstimateNumberForOfflineStatus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbsractEstimateSearchResultToSetOfflineStattus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SetOfflineStattusForAEForm') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveOfflineStattusForAbstractEstimate') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Offline Status for Abstract Estimate'));

--OS for LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='Ajax Search Estimate Numbers For LOA Offline Status') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveLOAOFFLINEStatus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LOASearchResultToSetOfflineStattus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLOAToSetOfflineStattus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search LOA NUMBER To LOA Offline Status') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractors To LOA Offline Status') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SetLOAOFFLINEStatus') ,(select id FROM eg_feature WHERE name = 'Offline Status for Letter Of Acceptance'));


--create MB
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchWorkOrderForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkOrderNumberSearch') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For LOA') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractor For LOA') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumberForMilestone') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkOrderSearch') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMBSuccessPage') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMeasurementBookSearchActivitiesForm') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMeasurementBookSearchActivities') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeasurementBookPDF') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ValidateMB') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMeasurementBookSuccessPage') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Create Abstract MB'));

--Update Measurement Book
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Update Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMBSuccessPage') ,(select id FROM eg_feature WHERE name = 'Update Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeasurementBookPDF') ,(select id FROM eg_feature WHERE name = 'Update Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMeasurementBookSuccessPage') ,(select id FROM eg_feature WHERE name = 'Update Measurement Book'));

--Search MB
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMBHeader') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchWorkOrderNumbers') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchEstimateNumbers') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMBHeaderSubmit') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeasurementBookPDF') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractors') ,(select id FROM eg_feature WHERE name = 'Search Measurement Book'));

--create contractor bill
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCreateContractorBillValidate') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractor For LOA') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksAjaxOtherDeductionCOAForContractorBill') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateContractorBillRegister') ,(select id FROM eg_feature WHERE name = 'Create Contractor Bill'));

--update contractor bill
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateContractorBillRegister') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));

--search CB
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeasurementBookPDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMeasurementBook') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));

--Modify LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search LOA NUMBER To Modify LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractors For To Modify LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For Modify LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));

--cancel line estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheackDependantObjectsCreatedForLineEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Line Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ValidateIsLatestMB') ,(select id FROM eg_feature WHERE name = 'Cancel Abstract MB'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckIfDependantObjectsCreatedToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
