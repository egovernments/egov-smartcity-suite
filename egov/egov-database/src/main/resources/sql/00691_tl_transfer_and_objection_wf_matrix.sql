update eg_wf_matrix set objecttype = 'TradeLicense' where objecttype = 'License';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'Assistant health officer Approved', NULL, NULL, NULL, NULL,'END', 'END', NULL, NULL, 'Generate License', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'Assistant health officer Approved', NULL, NULL, NULL, NULL,'END', 'END', NULL, NULL, 'Generate License', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');