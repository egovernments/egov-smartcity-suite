--Create Module Sub Type Of Work
Insert into EG_MODULE(ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) values (NEXTVAL('SEQ_EG_MODULE'),'WorksSubTypeOfWorksMaster','true',null,(select id from eg_module where name = 'WorksMasters'),'Sub Type Of Work',10);

--Insert Sub Type Of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Sub Type Of Work','/masters/subtypeofwork-newform',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),1,'Create Sub Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'Create Sub Type Of Work' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'Create Sub Type Of Work' and contextroot  = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'Create Sub Type Of Work' and contextroot = 'egworks'));

--Save Sub Type of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveSubTypeOfWork','/masters/subtypeofwork-save',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),1,'Save Sub Type of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SaveSubTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SaveSubTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SaveSubTypeOfWork' and contextroot = 'egworks'));

--Success Sub Type of Work
Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SuccessSubTypeOfWork','/masters/subtypeofwork-success',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),1,'Success Sub Type of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SuccessSubTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SuccessSubTypeOfWork' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SuccessSubTypeOfWork' and contextroot = 'egworks'));

--Sub Type of Work Feature
Insert into EG_FEATURE(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'Create Sub Type of Work','Create a Sub Type of Work',(select id from EG_MODULE where name = 'Works Management'));

--Sub Type of Work Feature Action
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'Create Sub Type Of Work'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'SuccessSubTypeOfWork'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action WHERE name = 'SaveSubTypeOfWork'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));

--Sub Type of Work Feature Role
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));
Insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator'),(select id FROM eg_feature WHERE name = 'Create Sub Type of Work'));


--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='Create Sub Type of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='Create Sub Type of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'Create Sub Type of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'SuccessSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SuccessSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SuccessSubTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'SaveSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SaveSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SaveSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SaveSubTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Sub Type Of Work' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Sub Type Of Work'	and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name = 'Create Sub Type Of Work' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION where name='Create Sub Type Of Work' and contextroot = 'egworks';

--rollback delete FROM EG_MODULE WHERE name='WorksSubTypeOfWorksMaster';
