update eg_action set url='/masters/overhead-newform' where name='Create Overhead' and contextroot = 'egworks';

--Save Overhead
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveOverhead','/masters/overhead-save',null,(select id from EG_MODULE where name = 'WorksOverheadMaster'),1,'Save Overhead','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SaveOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='SaveOverhead' and contextroot = 'egworks'));

--Success Overhead
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessOverhead','/masters/overhead-success',null,(select id from EG_MODULE where name = 'WorksOverheadMaster'),1,'Success Overhead','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SuccessOverhead' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='SuccessOverhead' and contextroot = 'egworks'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='Create Overhead' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='Create Overhead' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessOverhead' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessOverhead' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SuccessOverhead' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SaveOverhead' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SaveOverhead' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SaveOverhead' and contextroot = 'egworks';

--rollback update eg_action set url='/masters/overhead-newform.action' where name='Create Overhead' and contextroot = 'egworks';