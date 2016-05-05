--Inside Milestone Project Progress Trackker
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksMilestoneSearchLoa','true',null,(select id from eg_module where name = 'Works Management'),'Physical Progress Tracker', (select max(ordernumber)+1 from eg_module where name = 'Works Management'));

--Insert into eg_action and roleaction
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateTrackMilestoneSearch','/searchloa-milestone',null,(select id from EG_MODULE where name = 'WorksMilestoneSearchLoa'),1,'Create/Track Milestone','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateTrackMilestoneSearch' and contextroot = 'egworks'));

--Insert into eg_action and roleaction	
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'ajaxsearchloa','/ajax-searchresultloa',null,(select id from EG_MODULE where name = 'WorksMilestoneSearchLoa'),1,'Search Ajax Milestone',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ajaxsearchloa'and contextroot = 'egworks'));

--rollback delete from eg_module where name in ('WorksMilestoneSearchLoa');

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksCreateTrackMilestoneSearch' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksCreateTrackMilestoneSearch' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='ajaxsearchloa' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'ajaxsearchloa' and contextroot = 'egworks';