alter table EGW_SCHEDULEOFRATE add column version bigint default 0;
alter table EGW_SOR_RATE add column version bigint default 0;
alter table EGW_MARKETRATE add column version bigint default 0;

alter table EGW_MARKETRATE drop column market_sor_index;
alter table EGW_SOR_RATE drop column my_sor_index;

alter table EGW_SCHEDULEOFRATE RENAME column modified_by TO lastmodifiedby;
alter table EGW_SCHEDULEOFRATE RENAME column modified_date TO lastmodifieddate;
alter table EGW_SCHEDULEOFRATE RENAME column created_by TO createdby;
alter table EGW_SCHEDULEOFRATE RENAME column created_date TO createddate;

alter table EGW_SOR_RATE RENAME column modified_by TO lastmodifiedby;
alter table EGW_SOR_RATE RENAME column modified_date TO lastmodifieddate;
alter table EGW_SOR_RATE RENAME column created_by TO createdby;
alter table EGW_SOR_RATE RENAME column created_date TO createddate;

alter table EGW_MARKETRATE RENAME column modified_by TO lastmodifiedby;
alter table EGW_MARKETRATE RENAME column modified_date TO lastmodifieddate;
alter table EGW_MARKETRATE RENAME column created_by TO createdby;
alter table EGW_MARKETRATE RENAME column created_date TO createddate;

update EG_ACTION set url = '/masters/scheduleofrate-search' where name ='WorksScheduleOfRateSearch' and contextroot='egworks';
update EG_ACTION set url = '/masters/scheduleofrate-newform' where name ='Create Schedule Of Rate' and contextroot='egworks';
update EG_ACTION set url = '/masters/scheduleofrate-save' where name ='WorksSaveScheduleOfRate' and contextroot='egworks';
update EG_ACTION set url = '/masters/scheduleofrate-edit' where name ='WorksEditScheduleOfRate' and contextroot='egworks';
update EG_ACTION set url = '/masters/scheduleofrate-searchdetails' where name ='WorksSearchScheduleOfRate' and contextroot='egworks';
update EG_ACTION set url = '/masters/scheduleofrate-search' where name ='ViewScheduleOfRate' and contextroot='egworks';

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessScheduleOfRate','/masters/scheduleofrate-success',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),1,'Success Schedule Of Rate ','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessScheduleOfRate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessScheduleOfRate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessScheduleOfRate' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewScheduleOfRateMaster','/masters/scheduleofrate-view',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),1,'View Schedule Of Rate ','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'ViewScheduleOfRateMaster' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ViewScheduleOfRateMaster' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'ViewScheduleOfRateMaster' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ViewScheduleOfRateMaster' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ViewScheduleOfRateMaster') ,(select id from eg_feature where name = 'View Schedule Of Rate'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessScheduleOfRate') ,(select id from eg_feature where name = 'Modify Schedule Of Rate'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessScheduleOfRate') ,(select id from eg_feature where name = 'Create Schedule Of Rate'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchSORsForEstimateTemplate') ,(select id from eg_feature where name = 'View Estimate Template'));

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SearchSORsForEstimateTemplate') and feature = (select id from eg_feature where name = 'View Estimate Template');

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'SuccessScheduleOfRate');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessScheduleOfRate' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('SuccessScheduleOfRate') and contextroot = 'egworks';

--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'ViewScheduleOfRateMaster');
--rollback delete from eg_roleaction where roleid in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Works Administrator'),(select id from eg_role where name = 'Works View Access'),(select id from eg_role where name = 'Works Masters Creator')) and actionid = (select id from eg_action where name = 'SuccessScheduleOfRate' and contextroot = 'egworks');
--rollback delete from eg_action where name in ('ViewScheduleOfRateMaster') and contextroot = 'egworks';

--rollback update EG_ACTION set url = '/masters/scheduleOfRate-searchList.action' where name ='ViewScheduleOfRate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/scheduleOfRate-searchSorDetails.action' where name ='WorksSearchScheduleOfRate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/scheduleOfRate-edit.action' where name ='WorksEditScheduleOfRate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/scheduleOfRate-save.action' where name ='WorksSaveScheduleOfRate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/scheduleOfRate-newform.action' where name ='Create Schedule Of Rate' and contextroot='egworks';
--rollback update EG_ACTION set url = '/masters/scheduleOfRate-searchList.action' where name ='WorksScheduleOfRateSearch' and contextroot='egworks';

--rollback alter table EGW_MARKETRATE rename column lastmodifiedby to modified_by;
--rollback alter table EGW_MARKETRATE rename column lastmodifieddate to modified_date;
--rollback alter table EGW_MARKETRATE rename column createdby to created_by;
--rollback alter table EGW_MARKETRATE rename column createddate to created_date;

--rollback alter table EGW_SOR_RATE rename column lastmodifiedby to modified_by;
--rollback alter table EGW_SOR_RATE rename column lastmodifieddate to modified_date;
--rollback alter table EGW_SOR_RATE rename column createdby to created_by;
--rollback alter table EGW_SOR_RATE rename column createddate to created_date;

--rollback alter table EGW_SCHEDULEOFRATE rename column lastmodifiedby to modified_by;
--rollback alter table EGW_SCHEDULEOFRATE rename column lastmodifieddate to modified_date;
--rollback alter table EGW_SCHEDULEOFRATE rename column createdby to created_by;
--rollback alter table EGW_SCHEDULEOFRATE rename column createddate to created_date;

--rollback alter table EGW_MARKETRATE add column market_sor_index bigint;
--rollback alter table EGW_SOR_RATE add column my_sor_index bigint;

--rollback alter table EGW_SCHEDULEOFRATE drop column version;
--rollback alter table EGW_SOR_RATE drop column version;
--rollback alter table EGW_MARKETRATE drop column version;