---egpt_mutation_master--
INSERT INTO egpt_mutation_master (id,mutation_name,mutation_desc,type,code,order_id) VALUES (nextval('seq_egpt_mutation_master'),'TAX EXEMPTION','Tax Exemption','EXEMPTION','TAX EXEMPTION',1);

---eg_action---
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Property Exemption Form', '/exemption/form', null,(select id from eg_module where name='Existing property'), 1, 'Property Exemption Form', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Property Exemption update', '/exemption/update', null,(select id from eg_module where name='Existing property'), 1, 'Property Exemption update', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

---eg_roleaction---
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Exemption Form'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Exemption update'), id from eg_role where name in ('CSC Operator','Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

