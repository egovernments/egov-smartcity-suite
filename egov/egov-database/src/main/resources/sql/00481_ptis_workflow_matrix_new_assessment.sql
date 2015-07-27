DELETE FROM eg_wf_matrix WHERE additionalrule ='NEW ASSESSMENT';
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' || 'NEW', NULL, NULL, 'Revenue Clerk', 'NEW ASSESSMENT', 'Create' || ':' ||'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Revenue Clerk Approved', 'Create', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' || 'Revenue Clerk Approved', NULL, NULL, 'Bill Collector', 'NEW ASSESSMENT', 'Create' || ':' ||'Bill Collector Approved', 'Revenue inspector Approval Pending', 'Revenue inspector', 'Bill Collector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' || 'Bill Collector Approved', NULL, NULL, 'Revenue inspector', 'NEW ASSESSMENT', 'Create' || ':' ||'Revenue Inspector Approved', 'Revenue Officer Approval Pending', 'Revenue officer', 'Revenue inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' || 'Revenue Inspector Approved', NULL, NULL, 'Revenue officer', 'NEW ASSESSMENT', 'Create' || ':' ||'Revenue Officer Approved', 'Commissioner Approval Pending', 'Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' ||'Revenue Officer Approved', NULL, NULL, 'Commissioner', 'NEW ASSESSMENT', 'Create' || ':' ||'Commissioner Approved', 'Notice Print Pending', 'Revenue Clerk', 'Notice Generated', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' ||'Commissioner Approved', NULL, NULL, 'Revenue Clerk', 'NEW ASSESSMENT','Create' || ':' || 'END', 'END', NULL, NULL, 'Generate Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Create' || ':' ||'Rejected', NULL, NULL, 'Revenue Clerk', 'NEW ASSESSMENT', 'Create' || ':' ||'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

