------ START : Deleteing old Abstract Estimate Workflow Matrix ---
delete from EG_WF_MATRIX where objecttype = 'AbstractEstimate';
------ END : Deleteing old Abstract Estimate Workflow Matrix ---

------ START : Abstract Estimate workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AbstractEstimate', 'NEW', NULL, NULL, 'Assistant engineer', NULL, 'Created', 'Pending Technical Sanction', 'Superintending Engineer', 'Created', 'Save,Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AbstractEstimate', 'Created', NULL, NULL, 'Superintending Engineer', NULL, 'Technical Sanctioned', 'Pending Admin sanction', 'Commissioner', 'Technical Sanctioned', 'Submit,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AbstractEstimate', 'Technical Sanctioned', NULL, NULL, 'Commissioner', NULL, 'Admin Sanctioned', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AbstractEstimate', 'Rejected', NULL, NULL, 'Assistant engineer', NULL, 'Created', 'Pending Technical Sanction', 'Superintending Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());
------ END : Abstract Estimate workflow  ---

------ START : Updating Application types for workflow ---
UPDATE EG_WF_TYPES SET link = '/egworks/abstractestimate/update/:ID' WHERE type = 'AbstractEstimate' and module = (select id from eg_module where name = 'Works Management');
------ END : Updating Application types for workflow ---

--rollback update EG_WF_TYPES set link = '/egworks/estimate/abstractEstimate-edit.action?id=:ID&sourcepage=inbox' where link = '/egworks/abstractestimate/update/:ID';
--rollback delete from EG_WF_MATRIX where objecttype = 'AbstractEstimate';