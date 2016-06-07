update eg_action set url='/searchletterofacceptance/searchloa-milestone' where name ='WorksCreateTrackMilestoneSearch' and contextroot = 'egworks';
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksCreateTrackMilestoneSearch'and contextroot = 'egworks'));

-----------------Role action mappings to get Ajax WorkOrderNumber Search for LOA----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxWorkOrderNumberForMilestone','/letterofacceptance/ajaxloanumber-milestone',null,(select id from EG_MODULE where name = 'WorksMilestoneSearchLoa'),1,'search WorkOrder For Create Milestome','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxWorkOrderNumberForMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxWorkOrderNumberForMilestone' and contextroot = 'egworks'));

-----------------Role action mappings to get Ajax WorkIdentificationNumber for milestone----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxWorkIdentificationNumberForMilestone','/letterofacceptance/ajaxworkidentificationnumber-milestone',null,(select id from EG_MODULE where name = 'WorksMilestoneSearchLoa'),1,'search Work Identification Number For Create Milestome','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxWorkIdentificationNumberForMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxWorkIdentificationNumberForMilestone' and contextroot = 'egworks'));

-----------------Role action mappings to get search results to create milestone----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchLOAForMilestone','/letterofacceptance/ajaxsearch-loaformilestone',null,(select id from EG_MODULE where name = 'WorksMilestoneSearchLoa'),1,'Search LOA For Milestone','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchLOAForMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchLOAForMilestone' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLOAForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLOAForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchLOAForMilestone' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkOrderNumberForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkOrderNumberForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxWorkOrderNumberForMilestone' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkIdentificationNumberForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxWorkIdentificationNumberForMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxWorkIdentificationNumberForMilestone' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksCreateTrackMilestoneSearch' and contextroot = 'egworks');

