insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksAbstractEstimate','true',null,(select id from eg_module where name = 'Works Management'),'Abstract Estimate', 2);

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateAbstractEstimateNewForm','/estimate/abstractEstimate-newform.action',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Create Abstract Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateAbstractEstimateNewForm' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksCreateAbstractEstimateNewForm') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name in ('WorksCreateAbstractEstimateNewForm') and contextroot = 'egworks';
--rollback delete from eg_module where name in ('WorksAbstractEstimate');