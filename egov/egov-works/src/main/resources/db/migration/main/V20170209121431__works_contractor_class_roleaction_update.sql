update eg_action set url = '/masters/contractorclass-newform' where name ='Create Contractor Grade' and contextroot='egworks';
update eg_action set url = '/masters/contractorclass-viewcontractorclass' where name ='ViewContractorGrade' and contextroot='egworks';
update eg_action set url = '/masters/contractorclass-viewcontractorclass' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
update eg_action set url = '/masters/contractorclass-save' where name ='WorksSaveContractorGradeMaster' and contextroot='egworks';
update eg_action set url = '/masters/contractorclass-edit' where name ='WorksEditContractorGradeMaster' and contextroot='egworks';
update eg_action set url = '/masters/ajaxsearch-contractorclass' where name ='WorksSearchContractorGradeMaster' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessContractorClass','/masters/contractorclass-success',null,(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),1,'Success Contractor Class','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessContractorClass' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessContractorClass' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessContractorClass' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessContractorClass') ,(select id from eg_feature where name = 'Modify Contractor Grade'));

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessContractorClass');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessContractorClass' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SuccessContractorClass') and contextroot = 'egworks';
--rollback update eg_action set url = '/masters/contractorGrade-searchGradeDetails.action' where name ='WorksSearchContractorGradeMaster' and contextroot='egworks';
--rollback update eg_action set url = '/masters/contractorGrade-edit.action' where name ='WorksEditContractorGradeMaster' and contextroot='egworks';
--rollback update eg_action set url = '/masters/contractorGrade-save.action' where name ='WorksSaveContractorGradeMaster' and contextroot='egworks';
--rollback update eg_action set url = '/masters/contractorGrade-viewContractorGrade.action' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
--rollback update eg_action set url = '/masters/contractorGrade-viewContractorGrade.action' where name ='ViewContractorGrade' and contextroot='egworks';
--rollback update eg_action set url = '/masters/contractorGrade-newform.action' where name ='Create Contractor Grade' and contextroot='egworks';