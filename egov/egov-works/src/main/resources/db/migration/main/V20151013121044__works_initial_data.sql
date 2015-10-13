------------------START------------------
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Works Management','true','egworks',null,'Works Management', (select max(ordernumber)+1 from eg_module where contextroot is not null));
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksMasters','true',null,(select id from eg_module where name = 'Works Management'),'Masters', 1);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksContractorMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Contractors', 1);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksContractorGradeMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Contractor Grade', 2);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksMilestoneTemplateMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Milestone Template', 3);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksEstimateTemplateMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Estimate Template', 4);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksScheduleOfRateMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Schedule Of Rate', 5);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksScheduleCategoryMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Schedule Category', 6);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksOverheadMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Overhead', 7);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksDepositCodeMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Deposit Code', 8);
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksAbstractEstimate','true',null,(select id from eg_module where name = 'Works Management'),'Abstract Estimate', 2);
-------------------END-------------------


------------------START------------------
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateContractor','/masters/contractor-newform.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),1,'Create Contractor','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateContractor' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksContractorSearch','/masters/contractor-search.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),2,'View/Edit Contractor','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksContractorSearchResult','/masters/contractor-viewResult.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),null,'WorksContractorSearchResult','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchContractorSearchPage','/masters/contractor-searchPage.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),null,'WorksSearchContractorSearchPage','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchContractorSearchPage' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchContractorSearchResult','/masters/contractor-searchResult.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),null,'WorksSearchContractorSearchResult','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchContractorSearchResult' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Contractor Grade','/masters/contractorGrade-newform.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'Create Contractor Grade','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Contractor Grade' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Milestone Template','/masters/milestoneTemplate-newform.action',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),1,'Create Milestone Template','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Milestone Template' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Estimate Template','/estimate/estimateTemplate-newform.action',null,(select id from EG_MODULE where name = 'WorksEstimateTemplateMaster'),1,'Create Estimate Template','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Estimate Template' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Schedule Of Rate','/masters/scheduleOfRate-newform.action',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),1,'Create Schedule Of Rate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Schedule Of Rate' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Schedule Category','/masters/scheduleCategory-newform.action',null,(select id from EG_MODULE where name = 'WorksScheduleCategoryMaster'),1,'Create Schedule Category','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Schedule Category' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Overhead','/masters/overhead-newform.action',null,(select id from EG_MODULE where name = 'WorksOverheadMaster'),1,'Create Overhead','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Create Overhead' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Deposit Code','/masters/subledgerCode-newform.action',null,(select id from EG_MODULE where name = 'WorksDepositCodeMaster'),1,'Generate Deposit Code','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksContractorGradeSearch','/masters/contractorGrade-search.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),2,'View/Edit Contractor Grade','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksContractorGradeSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMilestoneTemplateSearch','/masters/milestoneTemplate-search.action',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),2,'Search Milestone Template','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMilestoneTemplateSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEstimateTemplateSearch','/estimate/estimateTemplate-search.action',null,(select id from EG_MODULE where name = 'WorksEstimateTemplateMaster'),2,'Search Estimate Template','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEstimateTemplateSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksScheduleOfRateSearch','/masters/scheduleOfRate-search.action',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),2,'View/Edit Schedule Of Rate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksScheduleOfRateSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateAbstractEstimateNewForm','/estimate/abstractEstimate-newform.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Create Abstract Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateAbstractEstimateNewForm' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateJurisdictionAjax','/estimate/wardSearch-searchAjax.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),null,'WorksAbstractEstimateJurisdictionAjax','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateJurisdictionAjax' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchEstimate','/estimate/searchEstimate.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),2,'Search Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchEstimate' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchEstimateSearch','/estimate/searchEstimate-search.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksSearchEstimateSearch','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchEstimateSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEstimateGISMapEdit','/estimate/abstractEstimate-maps.action','mapMode=edit',(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksEstimateGISMapEdit','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEstimateGISMapEdit' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEstimateGISMapView','/estimate/abstractEstimate-maps.action','mapMode=view',(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksEstimateGISMapView','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEstimateGISMapEdit' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEstimateSubTypeAjax','/estimate/ajaxEstimate-subcategories.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksEstimateSubTypeAjax','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEstimateSubTypeAjax' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSaveContractorGradeMaster','/masters/contractorGrade-save.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'WorksSaveContractorGradeMaster','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSaveContractorGradeMaster' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEditContractorGradeMaster','/masters/contractorGrade-edit.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'WorksEditContractorGradeMaster','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks'));

update eg_action set url = '/masters/contractorGrade-searchGradeDetails.action' where name = 'WorksContractorGradeSearch';


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateSave','/estimate/abstractEstimate-save.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimateSave','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateSave' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateBOQ','/estimate/abstractEstimate-viewBillOfQuantitiesXls.action','sourcepage=boqPDF',(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimateBOQ','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateBOQ' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimatePDF','/estimate/abstractEstimatePDF.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimatePDF','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimatePDF' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateSORSearchAjax','/masters/scheduleOfRateSearch-searchAjax.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimateSORSearchAjax','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateSORSearchAjax' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateFindSORAjax','/masters/scheduleOfRateSearch-findSORAjax.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimateFindSORAjax','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateFindSORAjax' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAbstractEstimateGetUOMFactor','/estimate/ajaxEstimate-getUOMFactor.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),0,'WorksAbstractEstimateGetUOMFactor','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAbstractEstimateGetUOMFactor' and contextroot = 'egworks'));
-------------------END-------------------

------------------START------------------
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Active',now(),'Active',1);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Inactive',now(),'Inactive',2);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Black-listed',now(),'Black-listed',3);
Insert into EGW_STATUS (id,moduletype,description,lastmodifieddate,code,order_id) values (nextval('seq_egw_status'),'Contractor','Debarred',now(),'Debarred',4);
-------------------END-------------------


------------------START------------------
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','New',now(),'NEW',0);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Created',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Technical Sanctioned',now(),'TECH_SANCTIONED',2);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Budgetary appropriation Done',now(),'BUDGETARY_APPROPRIATION_DONE',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Deposit Code Appropriation Done',now(),'DEPOSIT_CODE_APPR_DONE',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Admin Sanctioned',now(),'ADMIN_SANCTIONED',5);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Rejected',now(),'REJECTED',6);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Re-Submitted',now(),'RESUBMITTED',7);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'AbstractEstimate','Cancelled',now(),'CANCELLED',8);
-------------------END-------------------

------------------START------------------
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Capital Works - New Asset','CAPITAL','Capital Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Capital Works - Improvement Works','CAPITAL','Improvement Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Capital Works - Additional Works','CAPITAL','Improvement Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Capital Works - Special Works','CAPITAL','Improvement Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Deposit Works - Own Asset','CAPITAL','Deposit Works Own Asset');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Deposit Works - Third Party Asset','OTHERS','Deposit Works Third Party Asset');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Deposit Works - No Asset Created','OTHERS','Deposit Works No Asset Created');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Deposit Works - Road Cut Restoration','OTHERS','Deposit Works No Asset Created');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Repairs and maintenance Works','REVENUE','Repairs and maintenance');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MLACDS - New Asset','OTHERES','MLACDS  New Asset');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MLACDS - Additional Works','OTHERES','MLACDS  Additional Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MLACDS - Improvement Works','OTHERES','MLACDS  Improvement Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MPLADS - New Asset','OTHERES','MPLADS  New Asset');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MPLADS - Additional Works','OTHERES','MPLADS  Additional Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'MPLADS - Improvement Works','OTHERES','MPLADS  Improvement Works');
-------------------END-------------------


------------------START------------------
INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (nextval('seq_eg_script'), 'works.estimatenumber.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getEstimateNo():  
	finYearRange = finYear.getFinYearRange().split(''-'')
	sequenceName = ''ABSTRACTESTIMATE_''+finYearRange[0]+''_''+finYearRange[1]  
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4)  
	return	estimate.getExecutingDepartment().getCode()+"/"+finYear.getFinYearRange()+"/"+runningNumber
result=getEstimateNo()', '1900-01-01 00:00:00', '2100-01-01 00:00:00',0);
-------------------END-------------------

--rollback delete from eg_script where name='works.estimatenumber.generator';

--rollback delete from EGW_NATUREOFWORK;

--rollback delete from EGW_STATUS where MODULETYPE='AbstractEstimate';
--rollback delete from EGW_STATUS where moduletype='Contractor';

--rollback delete from eg_roleaction where actionid in (select id from eg_action where contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Super User');
--rollback delete from eg_action where contextroot = 'egworks';

--rollback delete from eg_module where name in ('WorksAbstractEstimate');
--rollback delete from eg_module where name in ('WorksContractorMaster','WorksContractorGradeMaster','WorksMilestoneTemplateMaster','WorksEstimateTemplateMaster','WorksScheduleOfRateMaster','WorksScheduleCategoryMaster','WorksOverheadMaster');
--rollback delete from eg_module where name in ('WorksMasters','Works Management');

