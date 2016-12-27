----------------------------------------Role Action Mappings to Search Sub Type Of Work-----------------------------------------------------------
insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchSubTypeOfWorkForm','/masters/subtypeofwork-search','mode=view',(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),3,'View Sub Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SearchSubTypeOfWorkForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SearchSubTypeOfWorkForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'SearchSubTypeOfWorkForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SearchSubTypeOfWorkForm' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewSubTypeOfWork','/masters/viewsubtypeofwork',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),0,'View Sub Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'ViewSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ViewSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'ViewSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'ViewSubTypeOfWork' and contextroot = 'egworks'));

insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SubTypeOfWorkSearchResult','/masters/ajaxsearch-viewsubtypeofwork',null,(select id from EG_MODULE where name = 'WorksSubTypeOfWorksMaster'),0,'Search Sub Type Of Work To View','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks'));

-----------------------------------Feature Action Mappings to View Sub Type Of Work---------------------------------------------------------------------------------------------------------
insert into eg_feature(ID,NAME,DESCRIPTION,MODULE) values (NEXTVAL('seq_eg_feature'),'View Sub Type Of Work','View a Sub Type Of Work',(select id from EG_MODULE where name = 'Works Management'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'ViewSubTypeOfWork') ,(select id from eg_feature where name = 'View Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SearchSubTypeOfWorkForm') ,(select id from eg_feature where name = 'View Sub Type Of Work'));
insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'SubTypeOfWorkSearchResult') ,(select id from eg_feature where name = 'View Sub Type Of Work'));

insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Super User') ,(select id from eg_feature where name = 'View Sub Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Administrator') ,(select id from eg_feature where name = 'View Sub Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works View Access'),(select id from eg_feature where name = 'View Sub Type Of Work'));
insert into eg_feature_role (ROLE, FEATURE) values ((select id from eg_role where name = 'Works Masters Creator') ,(select id from eg_feature where name = 'View Sub Type Of Work'));

--rollback delete FROM EG_FEATURE_ROLE WHERE feature = (select id from eg_feature  where name ='View Sub Type Of Work');
--rollback delete FROM EG_FEATURE_ACTION WHERE feature = (select id from eg_feature  where name ='View Sub Type Of Work');
--rollback delete FROM EG_FEATURE WHERE name = 'View Sub Type Of Work';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SubTypeOfWorkSearchResult' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='SubTypeOfWorkSearchResult' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SubTypeOfWorkSearchResult' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SubTypeOfWorkSearchResult' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SubTypeOfWorkSearchResult' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='ViewSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewSubTypeOfWork' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ViewSubTypeOfWork' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Masters Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchSubTypeOfWorkForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works View Access') and actionid = (SELECT id FROM eg_action WHERE name ='SearchSubTypeOfWorkForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchSubTypeOfWorkForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchSubTypeOfWorkForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchSubTypeOfWorkForm' and contextroot = 'egworks';