--View Track Milestone
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxViewTrackMilestoneDetails','/milestone/ajaxtrackmilestone-search',null,(select id from EG_MODULE where name = 'WorksMilestone'),1,'Ajax Track Milestone Search','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks'));

--Track Milestone Approved work identification number
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchWINForTrackMilestone','/milestone/ajaxworkidentificationnumbers-trackmilestone',null,(select id from EG_MODULE where name = 'WorksMilestone'),1,'Ajax Search Work Identification Number','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchWINForTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchWINForTrackMilestone' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxViewTrackMilestoneDetails' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxViewTrackMilestoneDetails' and contextroot = 'egworks';