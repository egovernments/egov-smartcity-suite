delete from eg_wf_matrix where objecttype = 'LineEstimate';

--0 to 200000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Technical Approve', 'Deputy Executive Engineer', 'Created', 'Forward', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Technical Approve', 
'Deputy Executive Engineer', null, 'Technically Approved', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 0, 200000, now(), now());


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Technical Approve', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 0, 200000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Technical Approve','Deputy Executive Engineer', null, 'Technically Approved', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 
'Technically Approved', 'Submit,Reject', 0, 200000, now(), now());

--200000.1 to 1000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Approve', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked By Deputy Executive Engineer', NULL, 'Pending Technical Approve', 
'Executive Engineer', null, 'Technically Approved', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 200000.1, 1000000, now(), now());


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Resubmitted', 'Forward,Cancel', 200000.1, 1000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Technical Approve', 'Executive Engineer', 'Checked', 'Submit,Reject', 200000.1, 1000000, now(), now());


--1000000.1 to 5000000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Technical Approve', 'Superintending Engineer', 'Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Technical Approve', 
'Superintending Engineer', null, 'Technically Approved', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', NULL, 'Forward,Cancel', 1000000.1, 5000000, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer','Checked', 'Submit,Reject', 1000000.1, 5000000, now(), now());


--- >500000
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'NEW', NULL, NULL, 
'Assistant engineer', null, 'Created', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', 'Created', 'Forward', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Created', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Executive Engineer', 
'Executive engineer', null, 'Checked', 'Pending Check By Superintending Engineer', 'Superintending Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Check By Superintending Engineer', 
'Superintending Engineer', null, 'Checked', 'Pending Technical Approve', 'Chief Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Checked', NULL, 'Pending Technical Approve', 
'Chief Engineer', null, 'Technically Approved', 'Pending Budgetary Sanction', 'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', 'Technically Approved', 'Submit,Reject', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Rejected', NULL, NULL, 
'Assistant engineer', null, 'Resubmitted', 'Pending Check By Deputy Executive Engineer', 'Deputy Executive Engineer', null, 'Forward,Cancel', 5000000.1, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Resubmitted', NULL, 'Pending Check By Deputy Executive Engineer', 
'Deputy Executive Engineer', null, 'Checked', 'Pending Check By Executive Engineer', 'Executive Engineer', 'Checked', 'Submit,Reject', 5000000.1, null, now(), now());

----Common budget and admin saction
INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Technically Approved', NULL, 'Pending Budgetary Sanction', 
'Junior Accountant,Senior Accountant,Accountant Category - IV,Junior Accounts Officer,Accounts Officer', null, 'Budget Sanctioned', 'Pending Admin Sanction', 'Commissioner', 'Budget Sanctioned', 'Submit,Reject', 0, null, now(), now());

INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, 
nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'LineEstimate', 'Budget Sanctioned', NULL, 'Pending Admin Sanction', 
'Commissioner', null, 'END', 'END', null, null, 'Approve,Reject', 0, null, now(), now());
