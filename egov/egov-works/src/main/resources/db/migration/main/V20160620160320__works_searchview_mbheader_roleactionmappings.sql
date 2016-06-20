-------Search Measurement Book roleaction mappings----

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'SearchMBHeader','/mbheader/searchform',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Search/View Measurement Book',true,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimateNumbers','/mbheader/ajaxestimateNumbers',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Ajax Search Estimate Numbers',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchWorkOrderNumbers','/mbheader/ajaxworkordernumbers',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Ajax Search WorkOrder Numbers',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchContractors','/mbheader/ajaxcontractors',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Ajax Search Contractor',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'SearchMBHeaderSubmit','/mbheader/ajax-searchmbheader',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Search MBHeader Submit',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));


insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchMBHeader' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SearchMBHeader' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchWorkOrderNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchWorkOrderNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchEstimateNumbers' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchContractors' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchContractors' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchMBHeaderSubmit' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SearchMBHeaderSubmit' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in ('SearchMBHeader','AjaxSearchWorkOrderNumbers','AjaxSearchEstimateNumbers','AjaxSearchContractors','SearchMBHeaderSubmit')) and roleid in(select id from eg_role where name in('Super User','Works Creator'));
--rollback delete from eg_action where name in ('SearchMBHeader','AjaxSearchWorkOrderNumbers','AjaxSearchEstimateNumbers','AjaxSearchContractors','SearchMBHeaderSubmit') and contextroot='egworks';