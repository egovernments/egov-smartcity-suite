INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'NEW', NULL, NULL, 'Operator', 'ALTER ASSESSMENT', 'Operator Approved', 'Bill Collector Approval Pending', 'Bill Collector', 'Operator Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Operator Approved', NULL, NULL, 'Bill Collector', 'ALTER ASSESSMENT', 'Bill Collector Approved', 'Revenue inspector Approval Pending', 'Revenue inspector', 'Bill Collector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Bill Collector Approved', NULL, NULL, 'Revenue inspector', 'ALTER ASSESSMENT', 'Revenue Inspector Approved', 'Revenue Officer Approval Pending', 'Revenue officer', 'Revenue inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Revenue Inspector Approved', NULL, NULL, 'Revenue officer', 'ALTER ASSESSMENT', 'Revenue Officer Approved', 'Commissioner Approval Pending', 'Commissioner', 'Revenue Officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Revenue Officer Approved', NULL, NULL, 'Commissioner', 'ALTER ASSESSMENT', 'Commissioner Approved', 'Notice Print Pending', 'Operator', 'Notice Generated', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Commissioner Approved', NULL, NULL, 'Opertor', 'ALTER ASSESSMENT', 'END', 'END', NULL, NULL, 'Generate Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Rejected', NULL, NULL, 'Operator', 'ALTER ASSESSMENT', 'Operator Approved', 'Bill Collector Approval Pending', 'Bill Collector', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Property Approver'), (select id from eg_action where name = 'View Modify Property'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Property Approver'), (select id from eg_action where name = 'Reject Modify Property'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Property Approver'), (select id from eg_action where name = 'Forward View Modify Property'));

update eg_action set contextroot = 'eis' where name = 'AjaxDesignationDropdown';
update eg_action set contextroot = 'eis' where name = 'AjaxApproverDropdown';


--rollback update eg_action set contextroot = 'ptis' where name = 'AjaxDesignationDropdown';
--rollback update eg_action set contextroot = 'ptis' where name = 'AjaxApproverDropdown';
--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Property Approver') and actionid in (select id from eg_action where name in ('View Modify Property', 'Reject Modify Property', 'Forward View Modify Property'));
--rollback delete from eg_wf_matrix where objecttype = 'PropertyImpl' and additionalrule = 'ALTER ASSESSMENT';
