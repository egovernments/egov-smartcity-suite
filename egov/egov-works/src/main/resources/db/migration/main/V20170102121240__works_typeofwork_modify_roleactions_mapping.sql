----------------------------------------Role Action Mappings to Modify  Type Of Work-----------------------------------------------------------
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchTypeOfWorkToModify','/masters/typeofwork-search',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),2,'Modify Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SearchTypeOfWorkToModify' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SearchTypeOfWorkToModify' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SearchTypeOfWorkToModify' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ModifyTypeOfWork','/masters/typeofwork-update',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),0,'Modify Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ModifyTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ModifyTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ModifyTypeOfWork' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveModifiedTypeOfWork','/masters/modifytypeofwork',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),3,'Modify Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SaveModifiedTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SaveModifiedTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SaveModifiedTypeOfWork' and contextroot = 'egworks'));

insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'Modify Type Of Work','Modify a Type Of Work',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchTypeOfWorkToModify') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchTypeOfWorkForView') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ModifyTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SaveTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SuccessTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SaveModifiedTypeOfWork') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));

insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator') ,(select id FROM eg_feature WHERE name = 'Modify Type Of Work'));

update eg_action set url = '/masters/ajaxsearch-typeofwork' where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks';

--rollback update eg_action set url = '/masters/ajaxsearch-viewtypeofwork' where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks';

--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='Modify Type Of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='Modify Type Of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'Modify Type Of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid in((SELECT id FROM eg_role WHERE name = 'Works Masters Creator'),(SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_role WHERE name = 'Works Administrator')) and actionid in ((SELECT id FROM eg_action WHERE name ='ModifyTypeOfWork' and contextroot = 'egworks'),(SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkToModify' and contextroot = 'egworks'),(SELECT id FROM eg_action WHERE name ='SaveModifiedTypeOfWork' and contextroot = 'egworks'));

--rollback delete FROM EG_ACTION WHERE name in ('ModifyTypeOfWork','SearchTypeOfWorkToModify','SaveModifiedTypeOfWork') and contextroot = 'egworks';

