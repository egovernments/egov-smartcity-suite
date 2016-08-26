-----------------Role action mappings to validate Revision Estimate---------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksValidateRevisionEstimate','/revisionestimate/validatere',null,(select id from EG_MODULE where name = 'WorksRevisionEstimate'),1,'Validate Revision Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksValidateRevisionEstimate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksValidateRevisionEstimate' and contextroot = 'egworks'));

-----------------Feature action mapping to validate Revision Estimate-----------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksValidateRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));

--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksValidateRevisionEstimate') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Revision Estimate');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksValidateRevisionEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksValidateRevisionEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksValidateRevisionEstimate' and contextroot = 'egworks';