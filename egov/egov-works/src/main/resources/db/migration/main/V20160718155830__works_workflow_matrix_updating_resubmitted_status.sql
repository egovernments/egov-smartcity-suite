-----------Insert script to add Resubmitted state to Line Estimate---------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, NULL, 'Superintending Engineer', NULL, 'Checked', 'Pending Budget sanction', 'Superintendent', 'Checked', 'Submit,Reject', NULL, NULL, now(), now());
UPDATE EG_WF_MATRIX SET nextstate = 'Resubmitted' where objecttype = 'LineEstimate' and currentstate = 'Rejected';

-----------Insert script to add Resubmitted state to Abstract Estimate---------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AbstractEstimate', 'Resubmitted', NULL, NULL, 'Superintending Engineer', NULL, 'Technical Sanctioned', 'Pending Admin sanction', 'Commissioner', 'Technical Sanctioned', 'Submit,Reject', NULL, NULL, now(), now());
UPDATE EG_WF_MATRIX SET nextstate = 'Resubmitted' where objecttype = 'AbstractEstimate' and currentstate = 'Rejected';

-----------Insert script to add Resubmitted state to Measurement Book---------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'MBHeader', 'Resubmitted', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
UPDATE EG_WF_MATRIX SET nextstate = 'Resubmitted' where objecttype = 'MBHeader' and currentstate = 'Rejected';

-----------Insert script to add Resubmitted state to LOA---------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WorkOrder', 'Resubmitted', NULL, NULL, 'Assistant Executive Engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
UPDATE EG_WF_MATRIX SET nextstate = 'Resubmitted' where objecttype = 'WorkOrder' and currentstate = 'Rejected';

-----------Insert script to add Resubmitted state to Contractor Bill---------------
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'ContractorBillRegister', 'Resubmitted', NULL, NULL, 'Assistant engineer', NULL, 'Approved', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, now(), now());
UPDATE EG_WF_MATRIX SET nextstate = 'Resubmitted' where objecttype = 'ContractorBillRegister' and currentstate = 'Rejected';

-----------Creating New Status Resubmitted for Line Estimate---------------------
INSERT INTO EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Resubmitted',now(),'RESUBMITTED',7);

-----------Creating New Status Resubmitted for Measurement Book---------------------
INSERT INTO EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'MBHeader','Resubmitted',now(),'RESUBMITTED',7);

-----------Creating New Status Resubmitted for LOA---------------------
INSERT INTO EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'WorkOrder','Resubmitted',now(),'RESUBMITTED',7);

--rollback delete from egw_status where moduletype = 'WorkOrder' and code = 'RESUBMITTED';
--rollback delete from egw_status where moduletype = 'MBHeader' and code = 'RESUBMITTED';
--rollback delete from egw_status where moduletype = 'LINEESTIMATE' and code = 'RESUBMITTED';

--rollback UPDATE EG_WF_MATRIX SET nextstate = 'Created' where objecttype = 'ContractorBillRegister' and currentstate = 'Rejected';
--rollback delete from EG_WF_MATRIX where objecttype = 'ContractorBillRegister' and currentstate = 'Resubmitted';

--rollback UPDATE EG_WF_MATRIX SET nextstate = 'Created' where objecttype = 'WorkOrder' and currentstate = 'Rejected';
--rollback delete from EG_WF_MATRIX where objecttype = 'WorkOrder' and currentstate = 'Resubmitted';

--rollback UPDATE EG_WF_MATRIX SET nextstate = 'Created' where objecttype = 'MBHeader' and currentstate = 'Rejected';
--rollback delete from EG_WF_MATRIX where objecttype = 'MBHeader' and currentstate = 'Resubmitted';

--rollback UPDATE EG_WF_MATRIX SET nextstate = 'Created' where objecttype = 'AbstractEstimate' and currentstate = 'Rejected';
--rollback delete from EG_WF_MATRIX where objecttype = 'AbstractEstimate' and currentstate = 'Resubmitted';

--rollback UPDATE EG_WF_MATRIX SET nextstate = 'Created' where objecttype = 'LineEstimate' and currentstate = 'Rejected';
--rollback delete from EG_WF_MATRIX where objecttype = 'LineEstimate' and currentstate = 'Resubmitted';