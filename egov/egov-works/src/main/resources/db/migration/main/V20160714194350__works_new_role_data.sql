---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Works Masters Creator', 'One who can create, view and modify works master screens', current_date, 1, 1, current_date, 0);

---------------Mapping Contractor Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksCreateContractor' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSearchContractorSearchPage' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSearchContractorSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveContractor' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSearchContractor' and contextroot = 'egworks'));

---------------Mapping Contractor Grade Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Contractor Grade' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveContractorGradeMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksContractorGradeViewEdit' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSearchContractorGradeMaster' and contextroot = 'egworks'));

---------------Mapping Milestone Template Master actions to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Create Milestone Template' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksMilestoneTemplateSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksMilestoneTemplateSave' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksMilestoneTemplateView' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Milestone Template' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksMilestoneTemplateSearch' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksMilestoneTemplateSave' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksMilestoneTemplateView' and contextroot = 'egworks');


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Create Contractor Grade' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveContractorGradeMaster' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksContractorGradeViewEdit' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSearchContractorGradeMaster' and contextroot = 'egworks');


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksCreateContractor' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSearchContractorSearchPage' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSearchContractorSearchResult' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveContractor' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSearchContractor' and contextroot = 'egworks');

--rollback delete from eg_role where name = 'Works Masters Creator';