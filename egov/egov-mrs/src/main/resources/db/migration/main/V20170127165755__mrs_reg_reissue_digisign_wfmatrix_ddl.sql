-- registration workflow ---
DELETE FROM eg_wf_matrix where objecttype='MarriageRegistration' and additionalrule='MARRIAGE REGISTRATION';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'NEW', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Revenue Clerk Approved', NULL, 'Commisioner Approval Pending_DigiSign', 'Assistant Engineer', 'MARRIAGE REGISTRATION', 'Application Approved', 'Digital Signature Pending', 'Commissioner', 'APPROVED', 'Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Digital Signed', NULL, 'Certificate Print Pending', 'Commissioner', 'MARRIAGE REGISTRATION', 'END', 'END', NULL, NULL, 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Approver Rejected Application', NULL, NULL, 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Approver Approval Pending', 'Commissioner', NULL, 'Forward,Cancel Registration', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Application Approved', NULL, 'Digital Signature Pending', 'Commissioner', 'MARRIAGE REGISTRATION', 'Digital Signed', 'Certificate Print Pending', 'Revenue Clerk', 'DIGITALSIGNED', 'Sign,Preview', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Revenue Clerk Approved', NULL, 'Commisioner Approval Pending_PrintCert', 'Assistant Engineer', 'MARRIAGE REGISTRATION', 'Application Approved', 'Certificate Print Pending', 'Revenue Clerk', 'APPROVED', 'Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Application Approved', NULL, 'Certificate Print Pending', 'Commissioner', 'MARRIAGE REGISTRATION', 'END', 'END', NULL, NULL, 'Print Certificate', NULL, NULL, '2016-04-01', '2099-04-01');

------------- REISSUE -------------------------

------------- with digital sign -----------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Application Approved', NULL, 'Digital Signature Pending', 'Commissioner',
'MARRIAGE REGISTRATION','Digital Signed', 'Certificate Print Pending', 'Revenue Clerk', 'DIGITALSIGNED', 'Sign,Preview',
NULL, NULL, '2016-04-01', '2099-04-01');

update eg_wf_matrix set nextaction = 'Digital Signature Pending', nextdesignation='Commissioner', pendingactions='Commisioner Approval Pending_DigiSign'
where nextaction='Certificate Print Pending' and nextdesignation='Revenue Clerk' and currentdesignation='Assistant Engineer'
and currentstate='Revenue Clerk Approved' and objecttype='ReIssue' and additionalrule='MARRIAGE REGISTRATION';

update eg_wf_matrix set currentstate='Digital Signed' 
where currentstate='Application Approved' and nextaction='END' AND nextstate='END' and currentdesignation='Commissioner'
and objecttype='ReIssue' and additionalrule='MARRIAGE REGISTRATION';

------------- without digi sign -------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Revenue Clerk Approved', NULL, 'Commisioner Approval Pending_PrintCert', 'Assistant Engineer',
'MARRIAGE REGISTRATION','Application Approved', 'Certificate Print Pending', 'Revenue Clerk', 'APPROVED', 'Approve,Reject',
NULL, NULL, '2016-04-01', '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Application Approved', NULL, 'Certificate Print Pending', 'Commissioner',
 'MARRIAGE REGISTRATION','END', 'END', null, null, 'Print Certificate',
  NULL, NULL, '2016-04-01', '2099-04-01');
