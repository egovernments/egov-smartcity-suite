-----------------Role action mappings for Employee to view update page----------------------
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Employee'),(select id from eg_action where name ='WorksUpdateLineEstimate' and contextroot = 'egworks'));

-----------------Role action mappings to show success page----------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksLineEstimateSuccess','/lineestimate/lineestimate-success',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'Line Estimate Success Page','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksLineEstimateSuccess' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Employee'),(select id from eg_action where name ='WorksLineEstimateSuccess' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Employee') and actionid = (select id from eg_action where name ='WorksLineEstimateSuccess' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksLineEstimateSuccess' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksLineEstimateSuccess' and contextroot = 'egworks';
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Employee') and actionid = (select id from eg_action where name ='WorksUpdateLineEstimate' and contextroot = 'egworks');