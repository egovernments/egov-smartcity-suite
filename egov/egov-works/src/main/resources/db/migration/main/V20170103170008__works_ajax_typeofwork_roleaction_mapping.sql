insert into eg_action(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchSubTypeOfWork','/masters/ajaxsearch-getsubtypeofwork',null,(select id from EG_MODULE where name = 'WorksTypeOfWorkMaster'),0,'Ajax Search For Sub Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));

insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name = 'AjaxSearchSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'AjaxSearchSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works View Access'),(select id from eg_action where name = 'AjaxSearchSubTypeOfWork' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name = 'AjaxSearchSubTypeOfWork' and contextroot = 'egworks'));

insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'AjaxSearchSubTypeOfWork') ,(select id from eg_feature where name = 'View Sub Type Of Work'));

--rollback delete from EG_FEATUTE_ACTION where action = (select id from eg_action  WHERE name = 'AjaxSearchSubTypeOfWork')

--rollback delete FROM EG_ROLEACTION WHERE roleid in((SELECT id FROM eg_role WHERE name = 'Works Masters Creator'),(SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_role WHERE name = 'Works View Access'),(SELECT id FROM eg_role WHERE name = 'Works Administrator')) and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchSubTypeOfWork' and contextroot = 'egworks');

--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchSubTypeOfWork' and contextroot = 'egworks';
