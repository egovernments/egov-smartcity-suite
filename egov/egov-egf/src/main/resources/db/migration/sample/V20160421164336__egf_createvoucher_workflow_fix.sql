INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'CVoucherHeader','Cancelled', NULL, NULL, 'Accounts Officer', NULL,
 'Accounts Officer Approved', 'Examiner of Accounts Approval Pending', 'Examiner of Accounts', 'Accounts Officer Approved', 
 'Forward', NULL, NULL, '2016-01-01', '2099-04-01');
