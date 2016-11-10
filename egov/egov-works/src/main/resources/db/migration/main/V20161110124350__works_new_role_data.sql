--View Contractor
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewContractor' and contextroot = 'egworks'));

--View Contractor grade
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewContractorGrade' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchContractorGradeMaster' and contextroot = 'egworks'));

--View Milestone template
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksEstimateSubTypeAjax' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks'));

--View Estimate template
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksEstimateTemplateSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksViewEstimateTemplateSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewEstimateTemplateMaster' and contextroot = 'egworks'));

--VIew schedule category
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewScheduleCategoryMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ScheduleCategorySearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewScheduleCategory' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchScheduleCategoryName' and contextroot = 'egworks'));

--View rate master
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewScheduleOfRate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksSearchScheduleOfRate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='WorksEditScheduleOfRate' and contextroot = 'egworks'));


--View Overhead
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchOverheadToView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchOverheadName' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchOverheadForView' and contextroot = 'egworks'));


--View abstract/detailed estimate
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='GetAbstractEstimatesByNumber' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='View-Asset' and contextroot = 'egassets'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AbstractEstimateView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewBillOfQuatities' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AbstractEstimatePDF' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AbstractEstimateAjaxSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AbstractEstimateSearchForm' and contextroot = 'egworks'));

--View abstract mb
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchEstimateNumbers' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchContractors' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchMBHeader' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='MeasurementBookPDF' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchContractorMBHeaders' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='SearchMBHeaderSubmit' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxSearchWorkOrderNumbers' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='Contractor MB View' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));

--View Contractor wise report
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ContractorWiseAbstractSearchForm' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxContractorsForContractorWiseAbstract' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='AjaxContractorWiseAbstract' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='ContractorWiseAbstractPdfExcel' and contextroot = 'egworks'));

--View RE
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='GetRevisionAbstractEstimatesByNumber' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='RevisionEstimateAjaxSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='RevisionEstimateSearchForm' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='RevisionEstimateView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='RevisionAgreementPDF' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name ='HistoryMeasurementBook' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('Get Estimate Number to View Estimate Photograph','HistoryMeasurementBook','RevisionAgreementPDF','View Estimate Photographs','Get Contractors to View Estimate Photograph','Ajax Search Estimate Photographs','Get WIN to View Estimate Photograph','Get Work Order Number to View Estimate Photograph','View Estimate Photograph Form','WorksContractorSearchResult','WorksEditContractor','ViewContractor','WorksEditContractorGradeMaster','ViewContractorGrade','WorksSearchContractorGradeMaster','WorksViewMilestoneTemplateSearch','WorksEstimateSubTypeAjax','WorksMilestoneTemplateSearchDetail','WorksMilestoneTemplateEdit','WorksEstimateTemplateSearchResult','WorksViewEstimateTemplateSearchResult','ViewEstimateTemplateMaster','ViewScheduleCategoryMaster','ScheduleCategorySearchResult','ViewScheduleCategory','SearchScheduleCategoryName','ViewScheduleOfRate','WorksSearchScheduleOfRate','WorksEditScheduleOfRate','SearchOverheadToView','ViewOverhead','SearchOverheadName','SearchOverheadForView','GetAbstractEstimatesByNumber','View-Asset','AbstractEstimateView','ViewBillOfQuatities','AbstractEstimatePDF','AbstractEstimateAjaxSearch','AbstractEstimateSearchForm','AjaxSearchEstimateNumbers','AjaxSearchContractors','SearchMBHeader','MeasurementBookPDF','AjaxSearchContractorMBHeaders','SearchMBHeaderSubmit','AjaxSearchWorkOrderNumbers','Contractor MB View','ViewMeasurementBook','ContractorWiseAbstractSearchForm','AjaxContractorsForContractorWiseAbstract','AjaxContractorWiseAbstract','ContractorWiseAbstractPdfExcel','GetRevisionAbstractEstimatesByNumber','RevisionEstimateAjaxSearch','RevisionEstimateSearchForm','RevisionEstimateView') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Works View Access');