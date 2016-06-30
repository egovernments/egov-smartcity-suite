-----------------Role action mappings to View Measurement Book----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'ViewMeasurementBook','/mb/view',null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'View Measurement Book','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='ViewMeasurementBook' and contextroot = 'egworks'));

update eg_action set ordernumber = 2 where name = 'SearchMBHeader' and contextroot = 'egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ViewMeasurementBook' and contextroot='egworks') and roleid in(select id from eg_role where name in ('Super User','Works Creator','Works Approver'));
--rollback delete from eg_action where name='ViewMeasurementBook' and contextroot='egworks';