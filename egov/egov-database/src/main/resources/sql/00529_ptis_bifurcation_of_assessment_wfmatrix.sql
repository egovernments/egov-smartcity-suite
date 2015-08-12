INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' || 'NEW', NULL, NULL, 'Revenue Clerk', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Revenue Clerk Approved', 'Create', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' || 'Revenue Clerk Approved', NULL, NULL, 'Bill Collector', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Bill Collector Approved', 'Revenue inspector Approval Pending', 'Revenue inspector', 'Bill Collector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' || 'Bill Collector Approved', NULL, NULL, 'Revenue inspector', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Revenue Inspector Approved', 'Revenue Officer Approval Pending', 'Revenue officer', 'Revenue inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' || 'Revenue Inspector Approved', NULL, NULL, 'Revenue officer', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Revenue Officer Approved', 'Commissioner Approval Pending', 'Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' ||'Revenue Officer Approved', NULL, NULL, 'Commissioner', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Commissioner Approved', 'Notice Print Pending', 'Revenue Clerk', 'Notice Generated', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' ||'Commissioner Approved', NULL, NULL, 'Revenue Clerk', 'BIFURCATE ASSESSMENT','Bifurcate' || ':' || 'END', 'END', NULL, NULL, 'Generate Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bifurcate' || ':' ||'Rejected', NULL, NULL, 'Revenue Clerk', 'BIFURCATE ASSESSMENT', 'Bifurcate' || ':' ||'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

--rollback delete from eg_wf_matrix where objecttype = 'PropertyImpl' and additionalrule = 'BIFURCATE ASSESSMENT';