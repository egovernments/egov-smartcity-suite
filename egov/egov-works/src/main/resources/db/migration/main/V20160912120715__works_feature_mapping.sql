INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksSearchLOAToCreateRE') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchAbstractEstimateToCreateRE','/letterofacceptance/ajaxsearch-loatocreatere',null,(select id from EG_MODULE where name = 'WorksRevisionEstimate'),3,'SearchAbstractEstimateToCreateRE',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchAbstractEstimateToCreateRE' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchAbstractEstimateToCreateRE' and contextroot = 'egworks'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchAbstractEstimateToCreateRE') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SorsByScheduleCategoriesAndEstimateId') ,(select id FROM eg_feature WHERE name = 'Create Revision Estimate'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Revision Estimate','Update Revision Estimate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimateView') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'File Download') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LineEstimatePDF') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id from eg_action where name ='View-Asset' and contextroot = 'egassets') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RevisionEstimateView') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksRevisionEstimateSuccessPage') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WorksUpdateRevisionEstimate') ,(select id FROM eg_feature WHERE name = 'Update Revision Estimate'));

--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksSearchLOAToCreateRE') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'SearchAbstractEstimateToCreateRE') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'SorsByScheduleCategoriesAndEstimateId') and FEATURE = (select id FROM eg_feature WHERE name = 'Create Revision Estimate');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchAbstractEstimateToCreateRE' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchAbstractEstimateToCreateRE' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchAbstractEstimateToCreateRE' and contextroot = 'egworks';

--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'AbstractEstimateView') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'ViewBillOfQuatities') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'AbstractEstimatePDF') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'File Download') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'LetterOfAcceptancePDF') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksViewLetterOfAcceptance') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'DownloadLineEstimateDoc') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'LineEstimatePDF') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksViewLineEstimate') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'View-Asset') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'RevisionEstimateView') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksRevisionEstimateSuccessPage') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');
--rollback delete FROM eg_feature_action WHERE ACTION = (select id FROM eg_action  WHERE name = 'WorksUpdateRevisionEstimate') and FEATURE = (select id FROM eg_feature WHERE name = 'Update Revision Estimate');

--rollback delete from eg_feature_role  where feature  = (select id from eg_feature where name = 'Update Revision Estimate');
--rollback delete from eg_feature_role  where feature  = (select id from eg_feature where name = 'Update Revision Estimate');
--rollback delete from eg_feature_role  where feature  = (select id from eg_feature where name = 'Update Revision Estimate');

--rollback delete from eg_feature where name = 'Update Revision Estimate';