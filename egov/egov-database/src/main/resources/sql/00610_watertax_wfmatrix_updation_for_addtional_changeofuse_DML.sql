UPDATE eg_wf_matrix SET nextstate='Clerk approved',nextaction='Asst.engineer approval pending' WHERE
objecttype ='WaterConnectionDetails' AND currentstate='Rejected' AND additionalrule='ADDNLCONNECTION';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Created', NULL, NULL, NULL, 
 'ADDNLCONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 
'Clerk Approved Pending', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');

UPDATE eg_wf_matrix SET nextstate='Clerk approved',nextaction='Asst.engineer approval pending' WHERE
objecttype ='WaterConnectionDetails' AND currentstate='Rejected' AND additionalrule='CHANGEOFUSE';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Created', NULL, NULL, NULL, 
 'CHANGEOFUSE', 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 
'Clerk Approved Pending', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');
