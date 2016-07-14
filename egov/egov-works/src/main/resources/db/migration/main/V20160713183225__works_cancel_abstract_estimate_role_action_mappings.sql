-----------------Role action mappings to search Abstract Estimate form----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchEstimateToCancelForm','/abstractestimate/cancel/search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Cancel Abstract Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchEstimateToCancelForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchEstimateToCancelForm' and contextroot = 'egworks'));

-----------------Role action mappings to search Abstract Estimates----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimateToCancel','/abstractestimate/cancel/ajax-search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search Abstract Estimate to Cancel','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxSearchEstimateToCancel' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'AjaxSearchEstimateToCancel' and contextroot = 'egworks'));

-----------------Role action mappings to cancel Abstract Estimate----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CancelEstimate','/abstractestimate/cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Cancel Abstract Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'CancelEstimate' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'CancelEstimate' and contextroot = 'egworks'));

-----------------Role action mappings to search Estimate Numbers to Cancel Abstract Estimate----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchEstimateNumbersToCancelEstimate','/abstractestimate/ajaxestimatenumbers-estimatetocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Search Estimate Numbers to cancel Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchEstimateNumbersToCancelEstimate' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchEstimateNumbersToCancelEstimate' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchEstimateNumbersToCancelEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchEstimateNumbersToCancelEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchEstimateNumbersToCancelEstimate' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CancelEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='CancelEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'CancelEstimate' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateToCancel' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateToCancel' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchEstimateToCancel' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchEstimateToCancelForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchEstimateToCancelForm' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchEstimateToCancelForm' and contextroot = 'egworks';