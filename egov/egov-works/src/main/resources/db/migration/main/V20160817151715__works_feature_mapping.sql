------------------------------------ADDING FEATURE STARTS------------------------
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Cancel Contractor Bill');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Cancel LOA');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone');
delete from eg_feature  where description = 'Modfiy an LOA' and name='Modfiy LOA';
update eg_action set url='/abstractestimate/ajaxsearchabstractestimatesforofflinestatus' where name = 'AbsractEstimateSearchResultToSetOfflineStattus';
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'View Schedule Category');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Modify LOA');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Create Milestone');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Search Milestone');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Track Milestone');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Search Tracked Milestone');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Create Contractor Bill');
delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Search Contractor Bill');


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Upload Schedule Of Rate','Upload sor rates',(select id from EG_MODULE where name = 'Works Management'));

--modify LOA
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksModifyLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLetterOfAcceptanceModifyForm') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Estimate Numbers For LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Contractor For LOA') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search LOA NUMBER') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLetterOfAcceptanceToModify') ,(select id FROM eg_feature WHERE name = 'Modify LOA'));

--loa
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchLOAToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchLOAsToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel LOA'));

--contractor bill
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchContractorBillToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxBillNumbersToCancelContractorBill') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorBillsToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckIfBillsCreatedToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Bill'));

--milestone
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMilestoneToCancelForm') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxLOANumbersToCancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsToCancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchMilestonesToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelMilestone') ,(select id FROM eg_feature WHERE name = 'Cancel Milestone/Track Milestone'));

--spillover line estimate test once 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksCreateSpillOverLineEstimateSuccess') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));

--save modify milestone template 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksMilestoneTemplateSave') ,(select id FROM eg_feature WHERE name = 'Modify Milestone Template'));

--schedule category
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleCategoryMaster') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchScheduleCategoryName') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ScheduleCategorySearchResult') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleCategory') ,(select id FROM eg_feature WHERE name = 'View Schedule Category'));

--rate master
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'View Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'View Rate Master'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksEditScheduleOfRate') ,(select id FROM eg_feature WHERE name = 'View Rate Master'));

--upload sor
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Upload Schedule Of Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UploadSORRatesForm') ,(select id FROM eg_feature WHERE name = 'Upload Schedule Of Rate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UploadSORRatesExistingForm') ,(select id FROM eg_feature WHERE name = 'Upload Schedule Of Rate'));

--abstract estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ajax Search Work Identification Numbers For Estimate') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search Line Estimate For Create AE') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search Admin Sanction Number For AE') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search Estimate Number For AE') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));

--line estimate
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveSpilloverLineEstimate') ,(select id FROM eg_feature WHERE name = 'Create Spillover Line Estimate'));

--Milestone
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Create Milestone'));
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
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksGetSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestoneDetails') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewMilestone') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxWorkIdentificationNumbersToCancelLOA') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchAndViewMilestone') ,(select id FROM eg_feature WHERE name = 'Search Milestone'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Track Milestone'));
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
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search Tracked Milestone'));
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
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorNameForContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchWorkIdentificationNumberForContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewContractorBill') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorBillPDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
