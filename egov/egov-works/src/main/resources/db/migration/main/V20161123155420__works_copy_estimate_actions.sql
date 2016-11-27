-----------------Role action mappings to Copy Estimate----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxEstimateNumbersToCopy','/abstractestimate/ajaxestimatenumbers-estimatetocopy',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Ajax Estimate Numbers To Copy Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxEstimateNumbersToCopy' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxEstimateNumbersToCopy' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxActivitiesToCopyEstimate','/abstractestimate/ajaxactivities-estimatetocopy',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Ajax Activities To Copy Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxActivitiesToCopyEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxActivitiesToCopyEstimate' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchEstimateForm','/abstractestimate/searchestimateform',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Search Estimate Form','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchEstimateForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SearchEstimateForm' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimates','/abstractestimate/ajaxestimates-search',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Ajax Search Estimates','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchEstimates' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchEstimates' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AjaxSearchEstimates' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='AjaxSearchEstimates' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='SearchEstimateForm' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='SearchEstimateForm' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AjaxEstimateNumbersToCopy' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='AjaxEstimateNumbersToCopy' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AjaxActivitiesToCopyEstimate' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='AjaxActivitiesToCopyEstimate' and contextroot='egworks';
