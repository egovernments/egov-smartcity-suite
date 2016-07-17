insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchAbstractEstimateToSetOfflineStattus','/abstractestimate/searchabstractestimateforofflinestatus-form',null,(select id from EG_MODULE where name = 'WorksOfflineStatus'),2,'Offline status for Abstract Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AbsractEstimateSearchResultToSetOfflineStattus','/offlinestatus/ajaxsearchabstractestimatesforofflinestatus',null,(select id from EG_MODULE where name = 'WorksOfflineStatus'),1,'LOA Search Result','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AbsractEstimateSearchResultToSetOfflineStattus' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AbsractEstimateSearchResultToSetOfflineStattus' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SetOfflineStattusForAEForm','/offlinestatus/setstatus-abstractestimate',null,(select id from EG_MODULE where name = 'WorksOfflineStatus'),1,'Set Offline Stattus For AE Form','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SetOfflineStattusForAEForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SetOfflineStattusForAEForm' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SaveOfflineStattusForAbstractEstimate','/offlinestatus/offlinestatus-saveabstractestimate',null,(select id from EG_MODULE where name = 'WorksOfflineStatus'),1,'Save Offline Stattus For Abstract Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SaveOfflineStattusForAbstractEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SaveOfflineStattusForAbstractEstimate' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxAbstractEstimateNumberForOfflineStatus','/abstractestimate/ajaxestimatenumbers-forofflinestatus',null,(select id from EG_MODULE where name = 'WorksOfflineStatus'),1,'Abstract Estimate Numbers For Offline Status','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxAbstractEstimateNumberForOfflineStatus' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxAbstractEstimateNumberForOfflineStatus' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxAbstractEstimateNumberForOfflineStatus' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxAbstractEstimateNumberForOfflineStatus' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxAbstractEstimateNumberForOfflineStatus' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksSearchAbstractEstimateToSetOfflineStattus' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AbsractEstimateSearchResultToSetOfflineStattus' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AbsractEstimateSearchResultToSetOfflineStattus' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AbsractEstimateSearchResultToSetOfflineStattus' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SetOfflineStattusForAEForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SetOfflineStattusForAEForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SetOfflineStattusForAEForm' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SaveOfflineStattusForAbstractEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SaveOfflineStattusForAbstractEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SaveOfflineStattusForAbstractEstimate' and contextroot = 'egworks';
