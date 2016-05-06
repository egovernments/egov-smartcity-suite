---------------map the action urls for create Contractor Grade to the role Works Administrator--------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='Create Contractor Grade' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSaveContractorGradeMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksContractorGradeViewEdit' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSearchContractorGradeMaster' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Administrator') and actionid in(select id from eg_action where name in('Create Contractor Grade','WorksContractorGradeViewEdit','WorksSaveContractorGradeMaster','WorksEditContractorGradeMaster','WorksSearchContractorGradeMaster') and contextroot = 'egworks');
