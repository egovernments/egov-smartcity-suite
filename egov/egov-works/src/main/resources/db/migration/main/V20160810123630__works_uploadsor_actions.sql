insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UploadSORRatesFirstTimeForm','/uploadsor/form',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),3,'Upload SOR Rates',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='UploadSORRatesFirstTimeForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'UploadSORRatesFirstTimeForm' and contextroot = 'egworks'));



insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UploadSORRatesExistingForm','/uploadexistingsor/form',null,(select id from EG_MODULE where name = 'WorksScheduleOfRateMaster'),3,'Upload SOR Rates',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='UploadSORRatesExistingForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'UploadSORRatesExistingForm' and contextroot = 'egworks'));


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='UploadSORRatesFirstTimeForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='UploadSORRatesFirstTimeForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'UploadSORRatesFirstTimeForm' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='UploadSORRatesExistingForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='UploadSORRatesExistingForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'UploadSORRatesExistingForm' and contextroot = 'egworks';


