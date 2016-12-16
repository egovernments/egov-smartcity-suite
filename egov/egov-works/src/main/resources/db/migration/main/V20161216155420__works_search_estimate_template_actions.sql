-----------------Role action mappings to search Estimate Templates----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchEstimateTemplateForm','/abstractestimate/searchestimatetemplateform',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Search Estimate Template Form','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchEstimateTemplateForm' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='SearchEstimateTemplateForm' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimateTemplates','/abstractestimate/ajaxestimatetemplates-search',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Ajax Search Estimate Templates','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchEstimateTemplates' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchEstimateTemplates' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='AjaxSearchEstimateTemplates' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='AjaxSearchEstimateTemplates' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='SearchEstimateTemplateForm' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator'));
--rollback delete from eg_action where name='SearchEstimateTemplateForm' and contextroot='egworks';