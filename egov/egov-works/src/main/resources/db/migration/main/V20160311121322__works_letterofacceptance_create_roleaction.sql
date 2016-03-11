-----------------Role action mappings to create letter of acceptance----------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchLineEstimatesToCreateLOA','/lineestimate/searchlineestimateforloa-form',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),1,'Create Letter of Acceptance',true,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchLineEstimatesToCreateLOA' and contextroot = 'egworks'));

-----------------Role action mappings to search line estimate for LOA----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'WorksSearchLineEstimateForLOA','/lineestimate/ajaxsearchlineestimatesforloa',null,(select id from eg_module where name='WorksLineEstimate'),1,'WorksSearchLineEstimateForLOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='WorksSearchLineEstimateForLOA'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksSearchLineEstimateForLOA' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksSearchLineEstimateForLOA' and contextroot = 'egworks';
--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksSearchLineEstimatesToCreateLOA') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name in ('WorksSearchLineEstimatesToCreateLOA') and contextroot = 'egworks';