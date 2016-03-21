------ START : Line Estimate workflow  ---
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 'Assistant engineer', 'NEWLINEESTIMATE', 'Created', 'Pending Check', 'Superintending Engineer', 'Created', 'Forward', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'Created', NULL, NULL, 'Superintending Engineer', 'NEWLINEESTIMATE', 'Checked', 'Pending Budget sanction', 'Superintendent', 'Checked', 'Submit,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'Checked', NULL, NULL, 'Superintendent', 'NEWLINEESTIMATE', 'Budget sanctioned', 'Pending Admin sanction', 'Commissioner', 'Budget sanctioned', 'Submit,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'Budget sanctioned', NULL, NULL, 'Commissioner', 'NEWLINEESTIMATE', 'Admin sanctioned', 'Pending Technical sanction', 'Superintending Engineer', 'Admin sanctioned', 'Submit,Reject', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'Admin sanctioned', NULL, NULL, 'Superintending Engineer', 'NEWLINEESTIMATE', 'END', 'END', NULL, NULL, 'Approve', NULL, NULL, now(), now());
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 'Assistant engineer', 'NEWLINEESTIMATE', 'Created', 'Pending Check', 'Superintending Engineer', NULL, 'Forward,Cancel', NULL, NULL, now(), now());
------ END : Line Estimate workflow  ---

------ START : Deleteing old Line Estimate application status ---
delete from egw_status where MODULETYPE = 'LineEstimate';
------ END : Deleteing old Line Estimate application status ---

------ START : Line Estimate application status ---
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Created',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Checked',now(),'CHECKED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Budget Sanctioned',now(),'BUDGET_SANCTIONED',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Administrative Sanctioned',now(),'ADMINISTRATIVE_SANCTIONED',5);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Technical Sanctioned',now(),'TECHNICAL_SANCTIONED',5);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'LINEESTIMATE','Cancelled',now(),'CANCELLED',6);
------ END : Line Estimate application status ---

------ START : Application types for workflow ---
INSERT INTO EG_WF_TYPES (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Works Management'), 'LineEstimate', '/egworks/lineestimate/update/:ID', 1, '2016-03-14 10:45:18.201078', 1, '2016-03-14 10:45:18.201078', 'Y', 'N', 'org.egov.works.lineestimate.entity.LineEstimate', 'Line Estimate', 0);
------ END : Application types for workflow ---


--rollback delete from EG_WF_TYPES where type = 'LineEstimateApplicationDetails' and link = '/egworks/lineestimate/update/:ID';
--rollback delete from egw_status where moduletype = 'LINEESTIMATEAPPLICATION';
--rollback delete from EG_WF_MATRIX where objecttype = 'LineEstimateApplicationDetails';