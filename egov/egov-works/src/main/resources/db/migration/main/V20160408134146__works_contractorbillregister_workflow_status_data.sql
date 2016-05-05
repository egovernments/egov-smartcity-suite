------ START : Contractor Bill Register workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'ContractorBillRegister', 'NEW', NULL, NULL, 'Assistant engineer', 'NEWCONTRACTORBILLREGISTER', 'Created', 'Pending Approval', 'Superintending Engineer', 'Created', 'Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'ContractorBillRegister', 'Created', NULL, NULL, 'Superintending Engineer', 'NEWCONTRACTORBILLREGISTER', 'END', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'ContractorBillRegister', 'Rejected', NULL, NULL, 'Assistant engineer', 'NEWCONTRACTORBILLREGISTER', 'Created', 'Pending Approval', 'Superintending Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());
------ END : Contractor Bill Register workflow  ---

------ START : Application types for workflow ---
INSERT INTO EG_WF_TYPES (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Works Management'), 'ContractorBillRegister', '/egworks/contractorbill/update/:ID', 1, '2016-04-08 10:45:18.201078', 1, '2016-04-08 10:45:18.201078', 'Y', 'N', 'org.egov.works.contractorbill.entity.ContractorBillRegister', 'Contractor Bill Register', 0);
------ END : Application types for workflow ---


--rollback delete from EG_WF_TYPES where type = 'ContractorBillRegister' and link = '/egworks/contractorbill/update/:ID';
--rollback delete from EG_WF_MATRIX where objecttype = 'ContractorBillRegister';