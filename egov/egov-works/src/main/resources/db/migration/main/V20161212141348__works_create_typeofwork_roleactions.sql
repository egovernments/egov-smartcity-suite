--Create Module Type of Work
Insert into EG_MODULE(ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) values (NEXTVAL('SEQ_EG_MODULE'),'WorksTypeOfWorkMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Type Of Work',9);

--Insert Type of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Type of Work','/masters/typeofwork-newform',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),1,'Create Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'Create Type of Work' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'Create Type of Work' and contextroot  = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'Create Type of Work' and contextroot = 'egworks'));

--Save Type of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveTypeOfWork','/masters/typeofwork-save',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),1,'Save Type of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SaveTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SaveTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SaveTypeOfWork' and contextroot = 'egworks'));

--Success Type of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessTypeOfWork','/masters/typeofwork-success',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),1,'Success Type of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessTypeOfWork' and contextroot = 'egworks'));

--Type of Work Feature
Insert into EG_FEATURE(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'Create Type of Work','Create a Type of Work',(select id from EG_MODULE where name = 'Works Management'));

--Type of Work Feature Action
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'Create Type of Work'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'SuccessTypeOfWork'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'SaveTypeOfWork'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));

--Type of Work Feature Role
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator'),(select id FROM eg_feature WHERE name = 'Create Type of Work'));

--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='Create Type of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='Create Type of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'Create Type of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'SuccessTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SuccessTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'SaveTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SaveTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SaveTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SaveTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Type of Work' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Type of Work'	and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Type of Work' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION where name='Create Type of Work' and contextroot = 'egworks';

--rollback delete FROM EG_MODULE WHERE name='WorksTypeOfWorkMaster';