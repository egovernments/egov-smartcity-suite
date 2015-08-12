---WORKFLOW MATRIX
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation', 'NEW', NULL, NULL, 'Revenue Clerk', 'PROPERTY TRANSFER', 'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Revenue Clerk Approved', 'Save', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation', 'Revenue Clerk Approved', NULL, NULL, 'Bill Collector', 'PROPERTY TRANSFER', 'Bill Collector Approved', 'Revenue inspector Approval Pending', 'Revenue inspector', 'Bill Collector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation','Bill Collector Approved', NULL, NULL, 'Revenue inspector', 'PROPERTY TRANSFER', 'Revenue Inspector Approved', 'Revenue Officer Approval Pending', 'Revenue officer', 'Revenue Inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation','Revenue Inspector Approved', NULL, NULL, 'Revenue Officer', 'PROPERTY TRANSFER','Revenue Officer Approved', 'Ready For Payment', 'Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation','Revenue Officer Approved', NULL, NULL, 'Revenue officer', 'PROPERTY TRANSFER','Revenue Officer Approved', 'Commissioner Approval Pending', 'Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation','Transfer Fee Collected', NULL, NULL, 'Commissioner', 'PROPERTY TRANSFER','Commissioner Approved', 'Transfer Notice Print Pending', 'Revenue Clerk', 'Transfer Notice Generated', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation', 'Commissioner Approved', NULL, NULL, 'Revenue Clerk', 'PROPERTY TRANSFER','END', 'END', NULL, NULL, 'Generate Title Transfer Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyMutation', 'Rejected', NULL, NULL, 'Revenue Clerk', 'PROPERTY TRANSFER', 'Revenue Clerk Approved', 'Bill Collector Approval Pending', 'Bill Collector', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


---ROLEACTION MAPPINGS FOR WORKFLOW
INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Acknowledgement Transfer Property' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='ULB Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Notice Transfer Property' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='ULB Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Reject' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='ULB Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property View' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Forward' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Property Transfer Success Redirect' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Reject' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Approve' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Approve' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property View' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Forward' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transfer Property Reject' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Property Transfer Success Redirect' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Calculate Mutation Fee' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Calculate Mutation Fee' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transferee Delete' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Approver' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Transferee Delete' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='Property Verifier' ));

