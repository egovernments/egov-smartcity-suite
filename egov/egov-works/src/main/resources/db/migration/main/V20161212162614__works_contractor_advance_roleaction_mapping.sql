insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'getAdvanceBillNumber','/contractoradvance/ajaxadvancebillnumbers',null,(select id from EG_MODULE where name = 'WorksContractorAdvance'),2,'getAdvanceBillNumber','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'getAdvanceBillNumber' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'getAdvanceBillNumber' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'getAdvanceBillNumber' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works View Access'), (select id from eg_action where name = 'getAdvanceBillNumber' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'ViewExpenseBillForm' and contextroot = 'EGF'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'ViewExpenseBillForm' and contextroot = 'EGF'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works View Access'), (select id from eg_action where name = 'ViewExpenseBillForm' and contextroot = 'EGF'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewExpenseBillForm') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'getAdvanceBillNumber') ,(select id FROM eg_feature WHERE name = 'Search Advance Requisition'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('getAdvanceBillNumber') and contextroot = 'egworks') and roleid in(select id from eg_role where name in('Works View Access','Works Creator','Works Approver','Super User'));
--rollback delete from eg_action where name in ('getAdvanceBillNumber') and contextroot = 'egworks';

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('ViewExpenseBillForm') and contextroot = 'EGF') and roleid in(select id from eg_role where name in('Works View Access','Works Creator','Works Approver'));
--rollback delete from eg_feature_action  where feature = (select id FROM eg_feature WHERE name = 'Search Advance Requisition') and action = (select id FROM eg_action  WHERE name = 'ViewExpenseBillForm');