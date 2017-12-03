
update eg_appconfig_values set value='Statistical Officer,Deputy Statistical Officer,Assistant Statistical Officer,Health Assistant/Birth and Death Registrar,Junior Assistant,Senior Assistant' where key_id=(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDESIGNATIONFORCSCOPERATORWORKFLOW');

update eg_appconfig_values set value='PUBLIC HEALTH AND SANITATION' where key_id=(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='MRSDEPARTMENTFORCSCOPERATORWORKFLOW');

update eg_wf_matrix set nextdesignation ='Statistical Officer,Deputy Statistical Officer,Assistant Statistical Officer,Health Assistant/Birth and Death Registrar,Junior Assistant,Senior Assistant',nextaction='Revenue Clerk Approval Pending' where currentstate='CSC Operator created' and objecttype='MarriageRegistration';

update eg_wf_matrix set nextdesignation ='Commissioner,Chief Medical Officer of Health,Municipal Health Officer',pendingactions='Revenue Clerk Approval Pending' where currentstate='NEW' and objecttype='MarriageRegistration';

update eg_wf_matrix set nextdesignation ='Commissioner,Chief Medical Officer of Health,Municipal Health Officer' where currentstate='Approver Rejected Application' and objecttype='MarriageRegistration';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'CREATED', NULL, 'Chief Medical Officer of Health Approval Pending', 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Chief Medical Officer of Health Approval Pending', 'Chief Medical Officer of Health', 'CREATED','Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Revenue Clerk Approved', NULL, 'Chief Medical Officer of Health Approval Pending', 'Chief Medical Officer', 'MARRIAGE REGISTRATION', 'Chief Medical Officer of Health Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED','Forward,Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Chief Medical Officer of Health Approved', NULL, 'Commisioner Approval Pending_DigiSign', 'Commissioner', 'MARRIAGE REGISTRATION', 'Application Approved', 'Digital Signature Pending', 'Commissioner', 'APPROVED','Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'CREATED', NULL, 'Municipal Health Officer Approval Pending', 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Municipal Health Officer Approval Pending', 'Municipal Health Officer', 'CREATED','Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Revenue Clerk Approved', NULL, 'Municipal Health Officer Approval Pending', 'Municipal Health Officer', 'MARRIAGE REGISTRATION', 'Municipal Health Officer Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED','Forward,Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Municipal Health Officer Approved', NULL, 'Commisioner Approval Pending_DigiSign', 'Commissioner', 'MARRIAGE REGISTRATION', 'Application Approved', 'Digital Signature Pending', 'Commissioner', 'APPROVED','Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');



---------------------reissue-------------------------------

update eg_wf_matrix set nextdesignation ='Statistical Officer,Deputy Statistical Officer,Assistant Statistical Officer,Health Assistant/Birth and Death Registrar,Junior Assistant,Senior Assistant',nextaction='Revenue Clerk Approval Pending' where currentstate='CSC Operator created' and objecttype='ReIssue';

update eg_wf_matrix set nextdesignation ='Commissioner,Chief Medical Officer of Health,Municipal Health Officer',pendingactions='Revenue Clerk Approval Pending' where currentstate='NEW' and objecttype='ReIssue';

update eg_wf_matrix set nextdesignation ='Commissioner,Chief Medical Officer of Health,Municipal Health Officer' where currentstate='Approver Rejected Application' and objecttype='ReIssue';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'CREATED', NULL, 'Chief Medical Officer of Health Approval Pending', 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Chief Medical Officer of Health Approval Pending', 'Chief Medical Officer of Health', 'CREATED','Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Revenue Clerk Approved', NULL, 'Chief Medical Officer of Health Approval Pending', 'Chief Medical Officer', 'MARRIAGE REGISTRATION', 'Chief Medical Officer of Health Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED','Forward,Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Chief Medical Officer of Health Approved', NULL, 'Commisioner Approval Pending_DigiSign', 'Commissioner', 'MARRIAGE REGISTRATION', 'Application Approved', 'Digital Signature Pending', 'Commissioner', 'APPROVED','Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'CREATED', NULL, 'Municipal Health Officer Approval Pending', 'Revenue Clerk', 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Municipal Health Officer Approval Pending', 'Municipal Health Officer', 'CREATED','Forward', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Revenue Clerk Approved', NULL, 'Municipal Health Officer Approval Pending', 'Municipal Health Officer', 'MARRIAGE REGISTRATION', 'Municipal Health Officer Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED','Forward,Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'ReIssue', 'Municipal Health Officer Approved', NULL, 'Commisioner Approval Pending_DigiSign', 'Commissioner', 'MARRIAGE REGISTRATION', 'Application Approved', 'Digital Signature Pending', 'Commissioner', 'APPROVED','Approve,Reject', NULL, NULL, '2016-04-01', '2099-04-01');
