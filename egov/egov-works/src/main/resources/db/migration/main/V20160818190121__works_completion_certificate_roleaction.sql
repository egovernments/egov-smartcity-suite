insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewCompletionCertificate','/contractorbill/contractorbillcompletionPDF',null,(select id from EG_MODULE where name = 'WorksContractorBill'),1,'ViewCompletionCertificate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ViewCompletionCertificate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='ViewCompletionCertificate' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ViewCompletionCertificate'));
insert into eg_roleaction (roleid, actionid) values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='ViewCompletionCertificate'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Contract Completion Certificate','View Contract Completion Certificate',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewCompletionCertificate') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewCompletionCertificate') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewCompletionCertificate') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Bill Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Bill Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Voucher Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Voucher Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Payment Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Payment Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'View Contract Completion Certificate'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewCompletionCertificate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewCompletionCertificate' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ViewCompletionCertificate' and contextroot = 'egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name = 'ViewCompletionCertificate') and roleid in(select id from eg_role where name in('Super User','Bill Creator','Bill Approver','Voucher Creator','Voucher Approver','Payment Creator','Payment Approver','Works Creator','Works Approver'));
--rollback delete from eg_action where name='ViewCompletionCertificate' and contextroot='egworks';