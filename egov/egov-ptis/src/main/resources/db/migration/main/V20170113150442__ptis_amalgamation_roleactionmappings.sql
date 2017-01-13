INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Amalgamation','/search/searchproperty-amalgamation.action', null, (select id from EG_MODULE where name = 'Existing property'),null,'Amalgamation of Property',true,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Amalgamation'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Property Amalgamation Form', '/amalgamation/amalgamation-newForm.action', null,(select id from eg_module where name='Existing property'), 1, 'Amalgamation of Property', true, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Amalgamation Form'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Forward Amalgamation', '/amalgamation/amalgamation-forwardModify.action', null,(select id from eg_module where name='Existing property'), 2, 'Forward Amalgamation', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Forward Amalgamation'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Amalgamation View', '/amalgamation/amalgamation-view.action', null,(select id from eg_module where name='Existing property'), 3, 'Amalgamation View', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Amalgamation View'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Forward Amalgamation View', '/amalgamation/amalgamation-forwardView.action', null,(select id from eg_module where name='Existing property'), 4, 'Forward Amalgamation View', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Forward Amalgamation View'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Approve Amalgamation', '/amalgamation/amalgamation-approve.action', null,(select id from eg_module where name='Existing property'), 5, 'Approve Amalgamation', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Approve Amalgamation'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Reject Amalgamation', '/amalgamation/amalgamation-reject.action', null,(select id from eg_module where name='Existing property'), 6, 'Reject Amalgamation', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Reject Amalgamation'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


-----------------Ajax calls ---------------------
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'AmalgamatedPropInfoAjax', '/common/amalgamation/getamalgamatedpropdetails', null,(select id from eg_module where name='Existing property'), 1, 'AmalgamatedPropInfoAjax', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AmalgamatedPropInfoAjax'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'UsageByPropertyTypeAjax', '/common/getusagebypropertytype', null,(select id from eg_module where name='Existing property'), 2, 'UsageByPropertyTypeAjax', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'UsageByPropertyTypeAjax'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

