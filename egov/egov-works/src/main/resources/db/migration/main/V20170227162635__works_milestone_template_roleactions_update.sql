alter table EGW_MILESTONE_TEMPLATE alter column status drop default;
alter table EGW_MILESTONE_TEMPLATE alter column status type boolean using (status :: boolean);

update EG_ACTION set url = '/masters/milestonetemplate-search' where name ='WorksMilestoneTemplateSearch' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-searchdetails' where name ='WorksMilestoneTemplateSearchDetail' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-search' where name ='WorksViewMilestoneTemplateSearch' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-view' where name ='WorksMilestoneTemplateView' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-newform' where name = 'Create Milestone Template' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-save' where name ='WorksMilestoneTemplateSave' and contextroot='egworks';
update EG_ACTION set url = '/masters/milestonetemplate-edit' where name ='WorksMilestoneTemplateEdit' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessMilestoneTemplate','/masters/milestonetemplate-success',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),1,'Success Milestone Template','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessMilestoneTemplate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessMilestoneTemplate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessMilestoneTemplate' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessMilestoneTemplate') ,(select id from eg_feature where name = 'Modify Milestone Template'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessMilestoneTemplate') ,(select id from eg_feature where name = 'Create Milestone Template'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessContractorClass') ,(select id from eg_feature where name = 'Create Contractor Grade'));

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessContractorClass') and feature = (select id from eg_feature where name = 'Create Contractor Grade');
--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessMilestoneTemplate');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessMilestoneTemplate' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SuccessMilestoneTemplate') and contextroot = 'egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-search.action' where name ='WorksMilestoneTemplateSearch' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-searchDetails' where name ='WorksMilestoneTemplateSearchDetail' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-search.action' where name ='WorksViewMilestoneTemplateSearch' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-view' where name ='WorksMilestoneTemplateView' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-newform.action' where name = 'Create Milestone Template' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-save' where name ='WorksMilestoneTemplateSave' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/milestoneTemplate-edit' where name ='WorksMilestoneTemplateEdit' and contextroot='egworks';
--rollback alter table EGW_MILESTONE_TEMPLATE alter column status drop default;
--rollback alter table EGW_MILESTONE_TEMPLATE alter column status type integer using (status :: integer);