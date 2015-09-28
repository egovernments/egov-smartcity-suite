Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSaveContractorGradeMaster','/masters/contractorGrade-save.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'WorksSaveContractorGradeMaster','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSaveContractorGradeMaster' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEditContractorGradeMaster','/masters/contractorGrade-edit.action',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'WorksEditContractorGradeMaster','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEditContractorGradeMaster' and contextroot = 'egworks'));

update eg_action set url = '/masters/contractorGrade-searchGradeDetails.action' where name = 'WorksContractorGradeSearch';

