
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense','Renewal License:NEW', NULL, NULL, 'Revenue Clerk,Junior Assistant', 'RENEWALTRADE', 'Renewal License:Sanitary inspector Approve pending', 'Renewal-Sanitory Inspector Approval Pending', 'Sanitary inspector,Medical store officer', 'Renewal-Clerk Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Renewal License:Sanitary inspector Approve pending', NULL, NULL, 'Sanitary inspector,Medical store officer', 'RENEWALTRADE', 'Renewal License:Sanitary inspector Approved', 'Renewal-Commissioner Approval pending', 'Commissioner', 'Renewal-Assistant health officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Renewal License:Sanitary inspector Approved', NULL, NULL, 'Commissioner', 'RENEWALTRADE', 'Renewal License:Commissioner Approved', 'Renewal-collection pending', 'Commissioner', 'Renewal-Commissioner  Approved', 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Renewal License:Commissioner Approved', NULL, NULL, 'Commissioner', 'RENEWALTRADE', 'Renewal License:generate Certificate', 'Renewal-generate certificate pending', 'Revenue Clerk,Junior Assistant', 'collction Done', 'Submit', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Renewal License:generate Certificate', NULL, NULL, 'Revenue Clerk,unior Assistant', 'RENEWALTRADE','Renewal License:END', 'END',NULL, NULL, 'Generate Certificate', NULL,NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Renewal License:Rejected', NULL, NULL, 'Revenue Clerk,Junior Assistant', 'RENEWALTRADE', 'Renewal License:Sanitary inspector Approve pending', 'Renewal-Sanitory Inspector Approval Pending', 'Sanitary inspector,Medical store officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


