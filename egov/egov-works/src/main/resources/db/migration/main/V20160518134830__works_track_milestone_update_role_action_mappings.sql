-----Updating Track Milestone Display name and removing access from Works Administrator-------
update eg_module set displayname = 'Milestone/Track Milestone' where name = 'WorksMilestone' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber  = (select max(ordernumber)+1 from eg_module where name = 'Works Management') where name = 'WorksReports' and parentmodule = (select id from eg_module where name = 'Works Management');

delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchMilestoneToTrack' and contextroot = 'egworks');
delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='TrackMilestone' and contextroot = 'egworks');

--rollback insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'TrackMilestone' and contextroot = 'egworks'));
--rollback insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchMilestoneToTrack' and contextroot = 'egworks'));
--rollback update eg_module set displayname = 'Milestone' where name = 'WorksMilestone' and parentmodule = (select id from eg_module where name = 'Works Management');