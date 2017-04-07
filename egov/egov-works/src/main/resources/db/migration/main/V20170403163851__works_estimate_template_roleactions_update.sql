alter table EGW_ESTIMATE_TEMPLATE alter column status drop default;
alter table EGW_ESTIMATE_TEMPLATE alter status type bool using CASE WHEN status=0 then FALSE ELSE TRUE end;

alter table EGW_ESTIMATE_TEMPLATE add column version bigint;
alter table EGW_EST_TEMPLATE_ACTIVITY add column version bigint;

alter table EGW_ESTIMATE_TEMPLATE RENAME column modified_by TO lastmodifiedby;
alter table EGW_ESTIMATE_TEMPLATE RENAME column modified_date TO lastmodifieddate;
alter table EGW_ESTIMATE_TEMPLATE RENAME column created_by TO createdby;
alter table EGW_ESTIMATE_TEMPLATE RENAME column created_date TO createddate;

alter table EGW_EST_TEMPLATE_ACTIVITY RENAME column modified_by TO lastmodifiedby;
alter table EGW_EST_TEMPLATE_ACTIVITY RENAME column modified_date TO lastmodifieddate;
alter table EGW_EST_TEMPLATE_ACTIVITY RENAME column created_by TO createdby;
alter table EGW_EST_TEMPLATE_ACTIVITY RENAME column created_date TO createddate;

alter table EGW_EST_TEMPLATE_ACTIVITY drop column template_activity_index;

update EG_ACTION set url = '/masters/estimatetemplate-newform' where name ='Create Estimate Template' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-search', queryparams = 'mode=edit' where name ='WorksEstimateTemplateSearch' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-searchdetails' where name ='WorksEstimateTemplateSearchResult' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-edit' where name ='ModifyEstimateTemplate' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-search' where name ='WorksViewEstimateTemplateSearchResult' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-view' where name ='ViewEstimateTemplateMaster' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-save' where name ='SaveEstimateTemplate' and contextroot='egworks';
update EG_ACTION set url = '/masters/estimatetemplate-search' where name ='WorksAddEstimateTemplateToEstimate' and contextroot='egworks';
update EG_ACTION set url = '/masters/ajaxestimatetemplate-searchajax' where name ='WorksEstimateTemplateSearchAjax' and contextroot='egworks';
update EG_ACTION set url = '/masters/ajaxestimatetemplate-findcodeajax' where name ='"WorksEstimateTemplateFindCodeAjax"' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessEstimateTemplate','/masters/estimatetemplate-success',null,(select id from EG_MODULE where name = 'WorksEstimateTemplateMaster'),1,'Success Estimate Template','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessEstimateTemplate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessEstimateTemplate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessEstimateTemplate' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessEstimateTemplate') ,(select id from eg_feature where name = 'Modify Estimate Template'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessEstimateTemplate') ,(select id from eg_feature where name = 'Create Estimate Template'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchSORsForEstimateTemplate','/masters/ajax-sorbyschedulecategories',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search Sors for Estimate Template','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchSORsForEstimateTemplate' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchSORsForEstimateTemplate' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'), (select id from eg_action where name = 'SearchSORsForEstimateTemplate' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SearchSORsForEstimateTemplate' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SearchSORsForEstimateTemplate') and contextroot = 'egworks';

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessEstimateTemplate');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessEstimateTemplate' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SuccessEstimateTemplate') and contextroot = 'egworks';

--rollback update EG_ACTION set url = '/estimate/ajaxEstimateTemplate-findCodeAjax.action' where name ='"WorksEstimateTemplateFindCodeAjax"' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/ajaxEstimateTemplate-searchAjax.action' where name ='WorksEstimateTemplateSearchAjax' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-search.action' where name ='WorksAddEstimateTemplateToEstimate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-save.action' where name ='SaveEstimateTemplate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-view.action' where name ='ViewEstimateTemplateMaster' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-search.action' where name ='WorksViewEstimateTemplateSearchResult' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-edit.action' where name ='ModifyEstimateTemplate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-searchDetails.action' where name ='WorksEstimateTemplateSearchResult' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-search.action', queryparams = null where name ='WorksEstimateTemplateSearch' and contextroot='egworks';
--rollback update EG_ACTION set url = '/estimate/estimateTemplate-newform.action' where name ='Create Estimate Template' and contextroot='egworks';

--rollback alter table EGW_EST_TEMPLATE_ACTIVITY rename column lastmodifiedby to modified_by;
--rollback alter table EGW_EST_TEMPLATE_ACTIVITY rename column lastmodifieddate to modified_date;
--rollback alter table EGW_EST_TEMPLATE_ACTIVITY rename column createdby to created_by;
--rollback alter table EGW_EST_TEMPLATE_ACTIVITY rename column createddate to created_date;

--rollback alter table EGW_ESTIMATE_TEMPLATE rename column lastmodifiedby to modified_by;
--rollback alter table EGW_ESTIMATE_TEMPLATE rename column lastmodifieddate to modified_date;
--rollback alter table EGW_ESTIMATE_TEMPLATE rename column createdby to created_by;
--rollback alter table EGW_ESTIMATE_TEMPLATE rename column createddate to created_date;

--rollback alter table EGW_EST_TEMPLATE_ACTIVITY add column template_activity_index bigint;

--rollback alter table EGW_EST_TEMPLATE_ACTIVITY drop column version;
--rollback alter table EGW_ESTIMATE_TEMPLATE drop column version;

--rollback alter table EGW_ESTIMATE_TEMPLATE alter column status drop default;
--rollback alter table EGW_ESTIMATE_TEMPLATE alter status type bigint using case when status=true then 1 else 0 end;