------Cancel Revision Estimates Role actions---------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'SearchEstimateToCancel','/revisionestimate/cancel/search',null,
(select id from eg_module where name='WorksAdministrator'),1,'Cancel Revision Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'CancelRevisionEstimate','/revisionestimate/cancel',null,
(select id from eg_module where name='WorksAdministrator'),1,'Cancel Revision Estimate Submit','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxCancelRevisionEstimate','/revisionestimate/cancel/ajax-search',null,
(select id from eg_module where name='WorksAdministrator'),1,'Ajax Cancel Revision Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxSearchRevisionEstimateNumbers','/revisionestimate/ajaxsearchretocancel',null,
(select id from eg_module where name='WorksAdministrator'),1,'Ajax Search Revision Estimate Numbers','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxCheckDependanObjectsForRevisionEstimate','/revisionestimate/ajax-checkifdependantObjectscreated',null,
(select id from eg_module where name='WorksAdministrator'),1,'Ajax Check Dependants Objects','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchEstimateToCancel' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='SearchEstimateToCancel' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='CancelRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='CancelRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxCancelRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxCancelRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxSearchRevisionEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxSearchRevisionEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxCheckDependanObjectsForRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction values((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='AjaxCheckDependanObjectsForRevisionEstimate' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in ('SearchEstimateToCancel','CancelRevisionEstimate','AjaxCancelRevisionEstimate','AjaxSearchRevisionEstimateNumbers','AjaxCheckDependanObjectsForRevisionEstimate')) and roleid in(select id from eg_role where name in('Super User','Works Administrator'));
--rollback delete from eg_action where name in('SearchEstimateToCancel','CancelRevisionEstimate','AjaxCancelRevisionEstimate','AjaxSearchRevisionEstimateNumbers','AjaxCheckDependanObjectsForRevisionEstimate');

----Cancel Revision Estimate feature mappings----
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Revision Estimate','Cancel Revision Estimate',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchEstimateToCancel') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCancelRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxSearchRevisionEstimateNumbers') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCheckDependanObjectsForRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel Revision Estimate'));



