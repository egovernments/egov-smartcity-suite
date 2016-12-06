---Search contractor advance Role action mapping
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchContractorAdvance','/contractoradvance/searchform',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),2,'Search Advance Requisition','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'SearchContractorAdvance' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchResultContractorAdvance','/contractoradvance/ajaxsearch-contractorrequisition',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),0,'SearchResultContractorAdvance','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchResultContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchResultContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'SearchResultContractorAdvance' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetAdvanceRequisitionNumberToSearchCR','/contractoradvance/ajaxarfnumbers-searchcr',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),0,'getAdvanceRequisitionNumberToSearchCR','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'GetAdvanceRequisitionNumberToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'GetAdvanceRequisitionNumberToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'GetAdvanceRequisitionNumberToSearchCR' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'getWorkOrderNumberToSearchCR','/contractoradvance/ajaxworkordernumbers-searchcr',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),0,'getWorkOrderNumberToSearchCR','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'getWorkOrderNumberToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'getWorkOrderNumberToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'getWorkOrderNumberToSearchCR' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'getContractorToSearchCR','/contractoradvance/ajaxcontractors-searchcr',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),0,'getContractorToSearchCR','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'getContractorToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'getContractorToSearchCR' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'getContractorToSearchCR' and contextroot = 'egworks'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewContractorAdvance','/contractoradvance/view',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),0,'ViewContractorAdvance','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'ViewContractorAdvance' and contextroot = 'egworks'));

--Search Contractor Advance eg_feature
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Advance Requisition','Search Advance Requisition',(select id from EG_MODULE where name = 'Works Management'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchContractorAdvance') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchResultContractorAdvance') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GetAdvanceRequisitionNumberToSearchCR') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'getWorkOrderNumberToSearchCR') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'getContractorToSearchCR') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewContractorAdvance') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
--Create Contractor Advance eg_feature_role
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));

--rollback delete from eg_feature_role  where feature = (select id from eg_feature  where name ='Search Advance Requisition');
--rollback delete from eg_feature_action where feature = (select id from eg_feature  where name ='Search Advance Requisition');
--rollback delete from eg_feature  where name ='Search Advance Requisition';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ViewContractorAdvance') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='ViewContractorAdvance' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='SearchContractorAdvance') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='SearchContractorAdvance' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='SearchResultContractorAdvance') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='SearchResultContractorAdvance' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='GetAdvanceRequisitionNumberToSearchCR') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='GetAdvanceRequisitionNumberToSearchCR' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='getWorkOrderNumberToSearchCR') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='getWorkOrderNumberToSearchCR' and contextroot='egworks';

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='getContractorToSearchCR') and roleid in(select id from eg_role where name in('Works Creator','Super User','Works Approver'));
--rollback delete from eg_action where name='getContractorToSearchCR' and contextroot='egworks';

