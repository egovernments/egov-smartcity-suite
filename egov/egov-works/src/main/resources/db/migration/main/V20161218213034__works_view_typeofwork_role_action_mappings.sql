----------------------------------------Role Action Mappings to Search Type Of Work-----------------------------------------------------------
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchTypeOfWorkToView','/masters/typeofwork-search','mode=view',(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),3,'View Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SearchTypeOfWorkToView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SearchTypeOfWorkToView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'SearchTypeOfWorkToView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SearchTypeOfWorkToView' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewTypeOfWork','/masters/typeofwork-view',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),0,'View Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'ViewTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ViewTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'ViewTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ViewTypeOfWork' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchTypeOfWorkForView','/masters/ajaxsearch-viewtypeofwork',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),0,'Search Type Of Work To View','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SearchTypeOfWorkForView' and contextroot = 'egworks'));

-----------------------------------Feature Action Mappings to View Type Of Work---------------------------------------------------------------------------------------------------------
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'View Type Of Work','View a Type Of Work',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ViewTypeOfWork') ,(select id from eg_feature where name = 'View Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchTypeOfWorkToView') ,(select id from eg_feature where name = 'View Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchTypeOfWorkForView') ,(select id from eg_feature where name = 'View Type Of Work'));

insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User') ,(select id from eg_feature where name = 'View Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator') ,(select id from eg_feature where name = 'View Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_feature where name = 'View Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator') ,(select id from eg_feature where name = 'View Type Of Work'));

--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='View Type Of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='View Type Of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'View Type Of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkForView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkForView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkForView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkForView' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchTypeOfWorkForView' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ViewTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkToView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkToView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkToView' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchTypeOfWorkToView' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchTypeOfWorkToView' and contextroot = 'egworks';