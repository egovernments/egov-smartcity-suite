INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'NEW', NULL, NULL, 'Operator', NULL, 'Operator Approved', 'Bill Collector Approved', 'Bill Collector', 'Bill Collector Approved', 'Create', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Operator Approved', NULL, NULL, 'Bill Collector', NULL, 'Bill Collector Approved', 'Bill Collector Approved', 'Revenue officer', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bill Collector Approved', NULL, NULL, 'Revenue officer', NULL, 'Revenue Officer Approved', 'Revenue Officer Approved', 'Commissioner', 'Commissionar Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Revenue Officer Approved', NULL, NULL, 'Commissioner', NULL, 'Commissionar Approved', 'Commissionar Approved', 'Operator', 'Notice Generated', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Commissionar Approved', NULL, NULL, 'Opertor', NULL, 'END', 'END', NULL, NULL, 'Generate Notice, Cancel', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES 
(nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Rejected', NULL, NULL, 'Operator', NULL, 'Operator Approved', 'Operator Approved', 'Bill Collector', NULL, 'Save, Cancel', NULL, NULL, '2015-04-01', '2099-04-01');
