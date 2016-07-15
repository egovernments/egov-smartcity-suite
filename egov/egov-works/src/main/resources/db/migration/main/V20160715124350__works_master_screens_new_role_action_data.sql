---------------Mapping Estimate Template actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Estimate Template' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SaveEstimateTemplate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ModifyEstimateTemplate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksEstimateTemplateSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksViewEstimateTemplateSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewEstimateTemplateMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksEstimateTemplateSearch' and contextroot = 'egworks'));

---------------Mapping Schedule Of Rate Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksScheduleOfRateSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Schedule Of Rate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveScheduleOfRate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksEditScheduleOfRate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSearchScheduleOfRate' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewScheduleOfRate' and contextroot = 'egworks'));

---------------Mapping Schedule Category Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Schedule Category' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveScheduleCategory' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ModifyScheduleCategoryMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ScheduleCategorySearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewScheduleCategory' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchScheduleCategoryName' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewScheduleCategoryMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksScheduleCategoryEdit' and contextroot = 'egworks'));

---------------Mapping Overhead Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Overhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SaveOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SuccessOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchOverheadToModify' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchOverheadName' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ModifyOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchOverheadToView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='SearchOverheadForView' and contextroot = 'egworks'));

---------------Mapping Deposit Code Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveDepositCodeMaster' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveDepositCodeMaster' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchOverheadForView' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewOverhead' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchOverheadToView' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ModifyOverhead' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchOverhead' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchOverheadName' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchOverheadToModify' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SuccessOverhead' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SaveOverhead' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Overhead' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksScheduleCategoryEdit' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewScheduleCategoryMaster' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SearchScheduleCategoryName' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewScheduleCategory' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ScheduleCategorySearchResult' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ModifyScheduleCategoryMaster' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveScheduleCategory' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Schedule Category' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewScheduleOfRate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSearchScheduleOfRate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksEditScheduleOfRate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveScheduleOfRate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Schedule Of Rate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksScheduleOfRateSearch' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksEstimateTemplateSearch' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewEstimateTemplateMaster' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksViewEstimateTemplateSearchResult' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksEstimateTemplateSearchResult' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ModifyEstimateTemplate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='SaveEstimateTemplate' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Estimate Template' and contextroot = 'egworks');