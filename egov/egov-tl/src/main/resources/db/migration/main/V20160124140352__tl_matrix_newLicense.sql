delete from eg_wf_matrix  where objecttype='TradeLicense';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense','Create License:NEW', NULL, NULL, 'Revenue Clerk,Junior Assistant', NULL, 'Create License:Sanitary inspector Approve pending', 'Sanitory Inspector Approval Pending', 'Sanitary inspector,Medical store officer', 'Clerk Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Sanitary inspector Approve pending', NULL, NULL, 'Sanitary inspector,Medical store officer', NULL, 'Create License:Sanitary inspector Approved', 'Commissioner Approval pending', 'Commissioner', 'Assistant health officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Sanitary inspector Approved', NULL, NULL, 'Commissioner', NULL, 'Create License:Commissioner Approved', 'collection pending', 'Commissioner', 'Commissioner  Approved', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Commissioner Approved', NULL, NULL, 'Commissioner', NULL, 'Create License:generate Certificate', 'generate certificate pending', 'Revenue Clerk,Junior Assistant', 'collction Done', 'Save', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:generate Certificate', NULL, NULL, 'Revenue Clerk,unior Assistant', NULL,'Create License:END', 'END',NULL, NULL, 'Generate Certificate', NULL,NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Rejected', NULL, NULL, 'Revenue Clerk,Junior Assistant', NULL, 'Create License:Sanitary inspector Approve pending', 'Sanitory Inspector Approval Pending', 'Sanitary inspector,Medical store officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

