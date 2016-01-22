
--Workflow Matrix for object type "CVoucherHeader"
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'CVoucherHeader','Create VoucherHeader:NEW', NULL, NULL, 'Accounts Officer', NULL, 'Create VoucherHeader:Accounts Officer Approved', 'Examiner of Accounts Approval Pending', 'Examiner of Accounts', 'Accounts Officer Approved', 'Forward,Reject', NULL, NULL, '2016-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'CVoucherHeader', 'Create VoucherHeader:Rejected', NULL, NULL, 'Accounts Officer', NULL, 'Create VoucherHeader:Accounts Officer Approved', 'Examiner of Accounts Approval Pending', 'Examiner of Accounts', NULL, 'Forward', NULL, NULL, '2016-01-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'CVoucherHeader', 'Create VoucherHeader:Accounts Officer Approved', NULL, NULL, 'Examiner of Accounts', NULL, 'Create VoucherHeader:Examiner of Accounts Approved', 'Commissioner Approval pending', 'Commissioner', 'Examiner of Accounts Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'CVoucherHeader', 'Create VoucherHeader:Examiner of Accounts Approved', NULL, NULL, 'Examiner of Accounts', NULL,'Create VoucherHeader:END', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2016-01-01', '2099-04-01');



--Userrole mapping
Insert into eg_userrole values((select id from eg_role  where name  = 'Voucher Approver'),(select id from eg_user where username ='EOA'));

Insert into eg_userrole values((select id from eg_role  where name  = 'Voucher Approver'),(select id from eg_user where username ='satyam'));


