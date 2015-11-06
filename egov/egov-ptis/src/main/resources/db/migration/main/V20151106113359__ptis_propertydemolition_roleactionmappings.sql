---egpt_mutation_master---
INSERT INTO egpt_mutation_master (id,mutation_name,mutation_desc,type,code,order_id) VALUES (nextval('seq_egpt_mutation_master'),'FULL DEMOLITION','Full Demolition','DEMOLITION','FULL DEMOLITION',8);

---eg_action---
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Property Demolition Form', '/property/demolition', null,(select id from eg_module where name='Existing property'), 1, 'Property Demolition Form', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Property Demolition update', '/demolition/update', null,(select id from eg_module where name='Existing property'), 1, 'Property Demolition update', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

---eg_roleaction---
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Demolition Form'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Demolition update'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');
