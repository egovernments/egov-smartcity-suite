delete from eg_wf_matrix where objecttype = 'RevisionAbstractEstimate';

--0 to 200000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'NEW', NULL, 'Pending Submission', 
'Assistant engineer', null, 'Created', 'Pending Technical Sanction', 'Deputy Executive Engineer', 'Created', 'Save,Forward', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Created', NULL, 'Pending Technical Sanction', 
'Deputy Executive Engineer', null, 'Technical Sanctioned', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technical Sanctioned', 'Submit,Reject', 0, 200000, now(), now());


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Technical Sanction', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Resubmitted', NULL, 'Pending Technical Sanction','Deputy Executive Engineer', null, 'Technical Sanctioned', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 
'Technical Sanctioned', 'Submit,Reject', 0, 200000, now(), now());

--200000.1 to 1000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'NEW', NULL, 'Pending Submission', 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Save,Forward', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Sanction', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked By Deputy Executive Engineer', NULL, 'Pending Technical Sanction', 
'Executive Engineer', null, 'Technical Sanctioned', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technical Sanctioned', 'Submit,Reject', 200000.1, 1000000, now(), now());


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Sanction', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());


--1000000.1 to 5000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'NEW', NULL, 'Pending Submission', 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Save,Forward', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Technical Sanction', 'Superintending Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked', NULL, 'Pending Technical Sanction', 
'Superintending Engineer', null, 'Technical Sanctioned', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technical Sanctioned', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', NULL, 'Forward,Cancel', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer','Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());


-- >5000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'NEW', NULL, 'Pending Submission', 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Save,Forward', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Check By Superintending Engineer', 'Superintending Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked', NULL, 'Pending Check By Superintending Engineer', 
'Superintending Engineer', null, 'Checked', 'Pending Technical Sanction', 'Chief Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Checked', NULL, 'Pending Technical Sanction', 
'Chief Engineer', null, 'Technical Sanctioned', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technical Sanctioned', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', null, 'Forward,Cancel', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

----Common budget and admin saction
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Technical Sanctioned', NULL, 'Pending Budgetary Sanction', 
'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', null, 'Budget Sanctioned', 'Pending Approval', 'Commissioner', 'Budget Sanctioned', 'Submit,Reject', 0, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'RevisionAbstractEstimate', 'Budget Sanctioned', NULL, 'Pending Approval', 
'Commissioner', null, 'END', 'END', null, null, 'Approve,Reject', 0, null, now(), now());


----egw_status--
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Checked',now(),'CHECKED',2);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Technical Sanctioned',now(),'TECH_SANCTIONED',2);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Budget Sanctioned',now(),'BUDGET_SANCTIONED',2);
