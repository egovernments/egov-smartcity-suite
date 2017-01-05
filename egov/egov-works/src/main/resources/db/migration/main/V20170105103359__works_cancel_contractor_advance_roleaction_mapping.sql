---Cancel advance requisition Role action mapping
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CancelAdvanceRequisitionForm','/contractoradvance/cancel/search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),4,'Cancel Advance Requisition','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'CancelAdvanceRequisitionForm' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'CancelAdvanceRequisitionForm' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'LOANumbersForCancelAdvanceRequisition','/contractoradvance/ajaxloanumbers-cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search LOA Numbers','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'LOANumbersForCancelAdvanceRequisition' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'LOANumbersForCancelAdvanceRequisition' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ContractorsForCancelAdvanceRequisition','/contractoradvance/ajaxcontractors-cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search Contractors','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ContractorsForCancelAdvanceRequisition' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ContractorsForCancelAdvanceRequisition' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ARFNumbersForCancelAdvanceRequisition','/contractoradvance/ajaxarfnumbers-cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search ARF Number','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ARFNumbersForCancelAdvanceRequisition' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ARFNumbersForCancelAdvanceRequisition' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchCancelAdvanceRequisition','/contractoradvance/cancel/ajax-search',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Search Result For Cancel Advance Requisition','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchCancelAdvanceRequisition' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchCancelAdvanceRequisition' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CancelContractorAdvance','/contractoradvance/cancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),0,'Cancel Advance Requisition','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'CancelContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'CancelContractorAdvance' and contextroot = 'egworks'));

--Cancel Contractor Advance eg_feature
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Advance Requisition','Cancel Advance Requisition',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelAdvanceRequisitionForm') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LOANumbersForCancelAdvanceRequisition') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorsForCancelAdvanceRequisition') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ARFNumbersForCancelAdvanceRequisition') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchCancelAdvanceRequisition') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelContractorAdvance') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
--Cancel Contractor Advance eg_feature_role
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'Cancel Contractor Advance'));

--rollback delete from eg_feature_role  where feature = (select id from eg_feature  where name ='Cancel Advance Requisition');
--rollback delete from eg_feature_action where feature = (select id from eg_feature  where name ='Cancel Advance Requisition');
--rollback delete from eg_feature  where name ='Cancel Advance Requisition';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='CancelAdvanceRequisitionForm') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='CancelAdvanceRequisitionForm' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='LOANumbersForCancelAdvanceRequisition') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='LOANumbersForCancelAdvanceRequisition' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ContractorsForCancelAdvanceRequisition') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='ContractorsForCancelAdvanceRequisition' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ARFNumbersForCancelAdvanceRequisition') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='ARFNumbersForCancelAdvanceRequisition' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='SearchCancelAdvanceRequisition') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='SearchCancelAdvanceRequisition' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='CancelContractorAdvance') and roleid in(select id from eg_role where name in('Works Administrator','Super User'));
--rollback delete from eg_action where name='CancelContractorAdvance' and contextroot='egworks';