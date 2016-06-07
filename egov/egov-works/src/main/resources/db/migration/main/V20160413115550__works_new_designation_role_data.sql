---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Works Administrator', 'Works Administrator', now(), 1, 1, now(), 0);

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksCreateContractor' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSearchContractorSearchPage' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSearchContractorSearchResult' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSaveContractor' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksSearchContractor' and contextroot = 'egworks'));

--rollback delete from eg_role where name = 'Works Administrator';
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksCreateContractor' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksSearchContractorSearchPage' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksSearchContractorSearchResult' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksSaveContractor' and contextroot = 'egworks');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Administrator') and actionid = (select id from eg_action where name ='WorksSearchContractor' and contextroot = 'egworks');
