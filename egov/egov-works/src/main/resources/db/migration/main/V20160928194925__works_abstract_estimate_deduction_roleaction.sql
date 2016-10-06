Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Ajax Abstract Estimate Deduction','/abstractestimate/ajaxdeduction-coa',null,(select id from EG_MODULE where name = 'WorksAbstractEstimate'),1,'Ajax Abstract Estimate Deduction','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Ajax Abstract Estimate Deduction' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Abstract Estimate Deduction' and contextroot = 'egworks'));

Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'Ajax Abstract Estimate Deduction') ,(select id FROM eg_feature WHERE name = 'Create Abstract Estimate'));
Insert into eg_feature_action (ACTION, FEATURE) values ((select id FROM eg_action  WHERE name = 'Ajax Abstract Estimate Deduction') ,(select id FROM eg_feature WHERE name = 'Update Abstract Estimate'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Abstract Estimate Deduction' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Abstract Estimate Deduction' and contextroot = 'egworks');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'Ajax Abstract Estimate Deduction') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Abstract Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'Ajax Abstract Estimate Deduction') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Abstract Estimate');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Abstract Estimate Deduction' and contextroot = 'egworks';

