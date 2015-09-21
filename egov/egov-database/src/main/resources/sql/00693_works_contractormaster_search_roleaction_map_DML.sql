
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksContractorSearch','/masters/contractor-search.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),2,'View/Edit Contractor','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksContractorSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksContractorSearchResult','/masters/contractor-viewResult.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),null,'WorksContractorSearchResult','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksContractorSearchResult' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksContractorSearch','WorksContractorSearchResult') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name in ('WorksContractorSearch','WorksContractorSearchResult') and contextroot = 'egworks';
