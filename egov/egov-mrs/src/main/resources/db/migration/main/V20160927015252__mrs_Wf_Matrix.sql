
-- working, registration workflow ---
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'NEW', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Asst. engineer Approval Pending', 'Assistant engineer', 'Revenue Clerk Approved', 'Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'Revenue Clerk Approved', NULL, NULL, 'Assistant Engineer', 'MARRIAGE REGISTRATION', 'Assistant Engineer Approved', 'Fee Collection Pending', 'Revenue Clerk', 'Assistant Engineer Approved', 'Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'Assistant Engineer Approved', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Fee Collected', 'Certificate Print Pending', 'Revenue Clerk', 'Certificate Issued', 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'Fee Collected', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'END', 'END', NULL, NULL, 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'Assistant Engineer Rejected', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Asst. engineer Approval Pending', 'Assistant engineer', NULL, 'Forward,Print Rejection Certificate, Close Registration', NULL, NULL, '2016-04-01', '2099-04-01');

-------- working, reissue workflow

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'NEW', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Asst. engineer Approval Pending', 'Assistant engineer', 'Revenue Clerk Approved', 'Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Revenue Clerk Approved', NULL, NULL, 'Assistant Engineer', 'MARRIAGE REGISTRATION', 'Assistant Engineer Approved', 'Fee Collection Pending', 'Revenue Clerk', 'Assistant Engineer Approved', 'Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Assistant Engineer Approved', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Fee Collected', 'Certificate Print Pending', 'Revenue Clerk', 'Certificate Issued', 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Fee Collected', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'END', 'END', NULL, NULL, 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Assistant Engineer Rejected', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Asst. engineer Approval Pending', 'Assistant engineer', NULL, 'Forward,Print Rejection Certificate, Close ReIssue', NULL, NULL, '2016-04-01', '2099-04-01');

