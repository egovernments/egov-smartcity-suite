-----------------Role action mappings to create letter of acceptance----------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSaveLOASuccess','/letterofacceptance/loa-success',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),1,'WorksSaveLOASuccess',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSaveLOASuccess' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAjaxContractorForLOA','/letterofacceptance/ajaxcontractors-loa',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),1,'WorksAjaxContractorForLOA',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksAjaxContractorForLOA' and contextroot = 'egworks'));
 
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksAjaxContractorForLOA' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksAjaxContractorForLOA' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksSaveLOASuccess' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksSaveLOASuccess' and contextroot = 'egworks';
