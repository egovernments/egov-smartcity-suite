-----------------Role action mappings to View Track Milestone----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewTrackMilestone','/milestone/viewtrackmilestone',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'View Track Milestone','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ViewTrackMilestone' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ViewTrackMilestone' and contextroot = 'egworks'));

update eg_action set url = '/milestone/viewmilestonetemplate' where name = 'ViewMilestoneTemplateDetails' and parentmodule = (select id from eg_module where name = 'Works Management' and contextroot = 'egworks');

--rollback update eg_action set url = '/milestone/view' where name = 'ViewMilestoneTemplateDetails' and parentmodule = (select id from eg_module where name = 'Works Management' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTrackMilestone' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ViewTrackMilestone' and contextroot = 'egworks';