---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Works View Access', 'user has access to view masters, reports, view transactional data, etc', current_date, 1, 1, current_date, 0);

--View Line Estimate
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchLineEstimateForm' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search Line Estimate Numbers' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxLoadBudgetHeadByFunction' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Search LineEstimate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksViewLineEstimate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='LineEstimatePDF' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search Admin Sanction Numbers' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='DownloadLineEstimateDoc' and contextroot = 'egworks'));

--View LOA
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='File Download' and contextroot = 'egi'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchLetterOfAcceptanceForm' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search LOA NUMBER' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search Contractor For LOA' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchLetterOfAcceptance' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksViewLetterOfAcceptance' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search Estimate Numbers For LOA' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='LetterOfAcceptancePDF' and contextroot = 'egworks'));

--View Milestone
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksGetSubTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewMilestoneDetails' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewMilestone' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxWorkIdentificationNumbersToCancelLOA' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchAndViewMilestone' and contextroot = 'egworks'));

--View Track Milestone
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='TrackedMilestoneView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewTrackMilestone' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks'));

--View Contractor Bill
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchWorkIdentificationNumberForContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksViewContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchContractorNameForContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchContractorBill' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ContractorBillPDF' and contextroot = 'egworks'));

--reports
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxWinCodesToSearchWorkProgressRegister' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchWorkProgressRegister' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorkProgressRegisterPdf' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchWorkProgressRegister' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxGetBudgetHeadByFunction' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='EstimateAppropriationRegisterPdf' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='EstimateAppropriationRegisterReport' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxEstimateAppropriationRegisterReport' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksGetFinancialYear' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksGetSubScheme' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));

--View estimate photograph
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Get Work Order Number to View Estimate Photograph' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Get Estimate Number to View Estimate Photograph' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Get Contractors to View Estimate Photograph' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Ajax Search Estimate Photographs' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='View Estimate Photographs' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='View Estimate Photograph Form' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Get WIN to View Estimate Photograph' and contextroot = 'egworks'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('FunctionsByFundIdAndDepartmentId','WorksSearchLineEstimateForm','Ajax Search Line Estimate Numbers','AjaxLoadBudgetHeadByFunction','Search LineEstimate','WorksViewLineEstimate','LineEstimatePDF','Ajax Search Admin Sanction Numbers','File Download','WorksSearchLetterOfAcceptanceForm','Ajax Search LOA NUMBER','Ajax Search Contractor For LOA','AjaxSearchLetterOfAcceptance','WorksViewLetterOfAcceptance','Ajax Search Estimate Numbers For LOA','LetterOfAcceptancePDF','WorksGetSubTypeOfWork','ViewMilestoneDetails','ViewMilestone','AjaxWorkIdentificationNumbersToCancelLOA','AjaxSearchAndViewMilestone','TrackedMilestoneView','ViewTrackMilestone','AjaxViewTrackMilestoneDetails','AjaxSearchWINForTrackMilestone','AjaxSearchWorkIdentificationNumberForContractorBill','WorksSearchContractorBill','WorksViewContractorBill','AjaxSearchContractorNameForContractorBill','AjaxSearchContractorBill','ContractorBillPDF','AjaxWinCodesToSearchWorkProgressRegister','AjaxSearchWorkProgressRegister','WorkProgressRegisterPdf','WorksSearchWorkProgressRegister','AjaxGetBudgetHeadByFunction','EstimateAppropriationRegisterPdf','EstimateAppropriationRegisterReport','AjaxEstimateAppropriationRegisterReport','EstimateAbstractReportByDepartmentWisePdf','WorksSearchEstimateAbstractReportByDepartmentWise','WorksGetFinancialYear','AjaxSearchEstimateAbstractReportByDepartmentWise','WorksGetSubScheme','Get Work Order Number to View Estimate Photograph','Get Estimate Number to View Estimate Photograph','Get Contractors to View Estimate Photograph','Ajax Search Estimate Photographs','View Estimate Photographs','View Estimate Photograph Form','Get WIN to View Estimate Photograph') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Works View Access');

--rollback delete from eg_role where name = 'Works View Access';