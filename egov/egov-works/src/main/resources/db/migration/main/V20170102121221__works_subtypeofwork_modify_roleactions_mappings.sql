----------------------------------------Role Action Mappings to Modify Sub Type Of Work-----------------------------------------------------------
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchSubTypeOfWorkToModify','/masters/subtypeofwork-search',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),2,'Modify Sub Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SearchSubTypeOfWorkToModify' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SearchSubTypeOfWorkToModify' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SearchSubTypeOfWorkToModify' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ModifySubTypeOfWork','/masters/subtypeofwork-update',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),0,'Modify Sub Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ModifySubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ModifySubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ModifySubTypeOfWork' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveModifiedSubTypeOfWork','/masters/modifysubtypeofwork',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),3,'Modify Sub Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SaveModifiedSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SaveModifiedSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SaveModifiedSubTypeOfWork' and contextroot = 'egworks'));

insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'Modify Sub Type Of Work','Modify a Sub Type Of Work',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchSubTypeOfWorkToModify') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ViewSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ModifySubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SaveSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SaveModifiedSubTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));

insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Modify Sub Type Of Work'));

update eg_action set url = '/masters/ajaxsearch-subtypeofwork' where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks';

--rollback update eg_action set url = '/masters/ajaxsearch-viewsubtypeofwork' where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks';

--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='Modify Sub Type Of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='Modify Sub Type Of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'Modify Sub Type Of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid in((SELECT id FROM eg_role WHERE name = 'Works Masters Creator'),(SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_role WHERE name = 'Works Administrator')) and actionid in ((SELECT id FROM eg_action WHERE name ='ModifySubTypeOfWork' and contextroot = 'egworks'),(SELECT id FROM eg_action WHERE name ='SearchSubTypeOfWorkToModify' and contextroot = 'egworks'),(SELECT id FROM eg_action WHERE name ='SaveModifiedSubTypeOfWork' and contextroot = 'egworks'));

--rollback delete FROM EG_ACTION WHERE name in ('ModifySubTypeOfWork','SearchSubTypeOfWorkToModify','SaveModifiedSubTypeOfWork') and contextroot = 'egworks';

