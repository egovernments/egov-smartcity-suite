update EG_ACTION set url = '/masters/contractor-search' where name ='WorksContractorSearch' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-searchpage' where name ='WorksSearchContractorSearchPage' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-searchdetails' where name ='WorksSearchContractorSearchResult' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-searchcontractor' where name ='WorksSearchContractor' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-search' where name ='ViewContractor' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-newform' where name ='WorksCreateContractor' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-view' where name ='WorksContractorSearchResult' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-save' where name ='WorksSaveContractor' and contextroot='egworks';
update EG_ACTION set url = '/masters/contractor-update' where name ='WorksEditContractor' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessContractor','/masters/contractor-success',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),1,'Success Contractor','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessContractor' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessContractor' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessContractor' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessContractor') ,(select id from eg_feature where name = 'Modify Contractor'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessContractor') ,(select id from eg_feature where name = 'Create Contractor'));

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessContractor');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessContractor' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SuccessContractor') and contextroot = 'egworks';

--rollback update EG_ACTION set url = '/masters/contractor-edit.action' where name ='WorksEditContractor' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-save.action' where name ='WorksSaveContractor' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-viewResult.action' where name ='WorksContractorSearchResult' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-newform.action' where name ='WorksCreateContractor' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-search.action' where name ='ViewContractor' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-searchContractor.action' where name ='WorksSearchContractor' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-searchResult.action' where name ='WorksSearchContractorSearchResult' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-searchPage.action' where name ='WorksSearchContractorSearchPage' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/contractor-search.action' where name ='"WorksContractorSearch"' and contextroot='egworks';
