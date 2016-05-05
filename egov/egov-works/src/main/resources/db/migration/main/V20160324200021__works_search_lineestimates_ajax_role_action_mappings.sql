-----------------Role action mappings to search line estimate numbers----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Line Estimate Numbers For Loa','/lineestimate/lineEstimateNumbersForLoa',null,(select id from eg_module where name='WorksLineEstimate'),1,'Ajax Search Line Estimate Numbers For Loa','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Line Estimate Numbers For Loa'));
Insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='Ajax Search Line Estimate Numbers For Loa'));
Insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='Ajax Search Line Estimate Numbers For Loa'));

-----------------Role action mappings to search administrative sanction numbers----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Admin Sanction Numbers For Loa','/lineestimate/adminSanctionNumbersForLoa',null,(select id from eg_module where name='WorksLineEstimate'),1,'Ajax Search Admin Sanction Numbers For Loa','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Admin Sanction Numbers For Loa'));
Insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='Ajax Search Admin Sanction Numbers For Loa'));
Insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='Ajax Search Admin Sanction Numbers For Loa'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Approver') and actionid = (select id from eg_action where name ='Ajax Search Admin Sanction Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Creator') and actionid = (select id from eg_action where name ='Ajax Search Admin Sanction Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Ajax Search Admin Sanction Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Ajax Search Admin Sanction Numbers For Loa' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Approver') and actionid = (select id from eg_action where name ='Ajax Search Line Estimate Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Creator') and actionid = (select id from eg_action where name ='Ajax Search Line Estimate Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Ajax Search Line Estimate Numbers For Loa' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Ajax Search Line Estimate Numbers For Loa' and contextroot = 'egworks';