update EG_ACTION set url='/masters/schedulecategory-newform' where name='Create Schedule Category' and contextroot='egworks';
update EG_ACTION set url='/masters/schedulecategory-save' where name='WorksSaveScheduleCategory' and contextroot='egworks';
update EG_ACTION set url='/masters/schedulecategory-update' where name='WorksScheduleCategoryEdit' and contextroot='egworks';
update EG_ACTION set url='/masters/schedulecategory-view' where name='ViewScheduleCategory' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ScheduleCategorySuccess','/masters/schedulecategory-success',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),2,'Modify Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'ScheduleCategorySuccess' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ScheduleCategorySuccess' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ScheduleCategorySuccess' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid in((SELECT id FROM eg_role WHERE name = 'Works Masters Creator'),(SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_role WHERE name = 'Works Administrator')) and actionid in ((SELECT id FROM eg_action WHERE name ='ScheduleCategorySuccess' and contextroot = 'egworks'));

--rollback delete FROM EG_ACTION WHERE name in ('ScheduleCategorySuccess') and contextroot = 'egworks';

--rollback update EG_ACTION set url='/masters/schedulecategory-newform.action' where name='Create Schedule Category' and contextroot='egworks';
--rollback update EG_ACTION set url='/masters/schedulecategory-save.action' where name='WorksSaveScheduleCategory' and contextroot='egworks';
--rollback update EG_ACTION set url='/masters/schedulecategory-update.action' where name='WorksScheduleCategoryEdit' and contextroot='egworks';
--rollback update EG_ACTION set url='/masters/scheduleCategory-view' where name='ViewScheduleCategory' and contextroot='egworks';