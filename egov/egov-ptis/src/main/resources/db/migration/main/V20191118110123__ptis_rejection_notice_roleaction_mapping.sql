-------RP/GRP
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'PropTax Rev Petition Reject to cancel', '/revPetition/revPetition-rejecttocancel.action', null,(select id from eg_module where name='Existing property'), 1, 'PropTax Rev Petition Reject to cancel', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'PropTax Rev Petition Reject to cancel'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

----------property transfer
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Transfer Property Reject to cancel', '/property/transfer/rejecttocancel.action', null,(select id from eg_module where name='Existing property'), 1, 'Transfer Property Reject to cancel', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Transfer Property Reject to cancel'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

-------modify
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Modify Property Reject To Cancel', '/modify/modifyProperty-rejecttocancel.action', null,(select id from eg_module where name='Existing property'), 1, 'Reject To Cancel Modify Property', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Modify Property Reject To Cancel'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

-------Amalgamation
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Amalgamation Reject To Cancel', '/amalgamation/amalgamation-rejecttocancel.action', null,(select id from eg_module where name='Existing property'), 1, 'Amalgamation Reject To Cancel', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Amalgamation Reject To Cancel'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');








