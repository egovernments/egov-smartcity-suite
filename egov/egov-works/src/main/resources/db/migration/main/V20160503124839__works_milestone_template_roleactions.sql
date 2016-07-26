Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='Create Milestone Template' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksMilestoneTemplateSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksEstimateSubTypeAjax' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMilestoneTemplateSave','/masters/milestoneTemplate-save',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),1,'Save Milestone Template','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMilestoneTemplateSave' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksMilestoneTemplateSave' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMilestoneTemplateEdit','/masters/milestoneTemplate-edit',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),1,'Edit Milestone Template','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMilestoneTemplateSearchDetail','/masters/milestoneTemplate-searchDetails',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),2,'Search Milestone Template','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks'));

update eg_action set DISPLAYNAME='Modify Milestone Template' where name = 'WorksMilestoneTemplateSearch' and contextroot = 'egworks';
update eg_action set QUERYPARAMS='mode=edit' where name = 'WorksMilestoneTemplateSearch' and contextroot = 'egworks';

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksViewMilestoneTemplateSearch','/masters/milestoneTemplate-search.action','mode=view',(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),3,'View Milestone Template','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksMilestoneTemplateView','/masters/milestoneTemplate-view',null,(select id from EG_MODULE where name = 'WorksMilestoneTemplateMaster'),1,'View Milestone Template','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksMilestoneTemplateView' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksMilestoneTemplateView' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMilestoneTemplateSearchDetail' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateEdit' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMilestoneTemplateEdit' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateSave' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateSave' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMilestoneTemplateSave' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksViewMilestoneTemplateSearch' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksViewMilestoneTemplateSearch' and contextroot = 'egworks';

--rollback update eg_action set QUERYPARAMS='null' where name = 'WorksMilestoneTemplateSearch' and contextroot = 'egworks';
--rollback update eg_action set DISPLAYNAME='Search Milestone Template' where name = 'WorksMilestoneTemplateSearch' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksMilestoneTemplateView' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksMilestoneTemplateView' and contextroot = 'egworks';

--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Administrator') and actionid in(select id from eg_action where name in('WorksMilestoneTemplateSearch','Create Milestone Template','WorksEstimateSubTypeAjax') and contextroot = 'egworks');