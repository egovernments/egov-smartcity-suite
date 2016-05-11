-----------------Renaming Project Progress Tracker to Milestone-----------------
update eg_module set displayname = 'Milestone' where name = 'WorksMilestoneSearchLoa' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set NAME = 'WorksMilestone' where DISPLAYNAME = 'Milestone' and PARENTMODULE = (select id from eg_module where name = 'Works Management');

-----------------Renaming Create/Track Milestone to Create Milestone-----------------
update eg_action set displayname = 'Create Milestone' where name = 'WorksCreateTrackMilestoneSearch' and parentmodule = (select id from EG_MODULE where name = 'WorksMilestone');

-----------------Role action mappings to View Milestone----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewMilestone','/milestone/searchtoview-form',null,(select id from EG_MODULE where name = 'WorksMilestone'),2,'View Milestone','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ViewMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ViewMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'ViewMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'ViewMilestone' and contextroot = 'egworks'));

-----------------Role action mappings to Track Milestone----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchMilestoneToTrack','/milestone/search-form',null,(select id from EG_MODULE where name = 'WorksMilestone'),3,'Track Milestone','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchMilestoneToTrack' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchMilestoneToTrack' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchMilestoneToTrack' and contextroot = 'egworks'));

-----------------Role action mappings to View Tracked Milestones----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'TrackedMilestoneView','/milestone/searchtracked-form',null,(select id from EG_MODULE where name = 'WorksMilestone'),4,'Tracked Milestone View','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'TrackedMilestoneView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'TrackedMilestoneView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'TrackedMilestoneView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'TrackedMilestoneView' and contextroot = 'egworks'));

--rollback delete FROM EG_ACTION WHERE name = 'TrackedMilestoneView' and contextroot = 'egworks';
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='TrackedMilestoneView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='TrackedMilestoneView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='TrackedMilestoneView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='TrackedMilestoneView' and contextroot = 'egworks');

--rollback delete FROM EG_ACTION WHERE name = 'SearchMilestoneToTrack' and contextroot = 'egworks';
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchMilestoneToTrack' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchMilestoneToTrack' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchMilestoneToTrack' and contextroot = 'egworks');

--rollback delete FROM EG_ACTION WHERE name = 'ViewMilestone' and contextroot = 'egworks';
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='ViewMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewMilestone' and contextroot = 'egworks');

--rollback update eg_action set displayname = 'Create/Track Milestone' where name = 'WorksCreateTrackMilestoneSearch' and parentmodule = (select id from EG_MODULE where name = 'WorksMilestone');

--rollback update eg_module set NAME = 'WorksMilestoneSearchLoa' where DISPLAYNAME = 'Milestone' and PARENTMODULE = (select id from eg_module where name = 'Works Management');
--rollback update eg_module set DISPLAYNAME = 'Physical Progress Tracker' where NAME = 'WorksMilestoneSearchLoa' and PARENTMODULE = (select id from eg_module where name = 'Works Management');