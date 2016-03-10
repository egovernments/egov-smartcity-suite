-----------------Role action mappings to search line estimate numbers----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Line Estimate Numbers','/lineestimate/lineEstimateNumbers',null,(select id from eg_module where name='WorksLineEstimate'),1,'Ajax Search Line Estimate Numbers','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Line Estimate Numbers'));

-----------------Role action mappings to search administrative sanction numbers----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Admin Sanction Numbers','/lineestimate/adminSanctionNumbers',null,(select id from eg_module where name='WorksLineEstimate'),1,'Ajax Search Admin Sanction Numbers','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Admin Sanction Numbers'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Ajax Search Admin Sanction Numbers' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Ajax Search Admin Sanction Numbers' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Ajax Search Line Estimate Numbers' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Ajax Search Line Estimate Numbers' and contextroot = 'egworks';