INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Revenue Clerk', 
 'CHANGEOFUSE', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
'Clerk Approved', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'Asst engg approved', 'Estimation Notice print pending', 'Revenue Clerk', 
'Assistant Engineer approved', 'Submit,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Asst engg approved', NULL, NULL, 'Revenue Clerk', 
 'CHANGEOFUSE', 'Payment done against Estimation', 'Commissioner approval pending', 'Commissioner', 
'Payment against Estimation', 'Generate Estimation Notice',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Commissioner', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Work order print pending', 'Revenue Clerk', 
'workorder pending', 'Approve',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Revenue Clerk', 
 'CHANGEOFUSE', 'Work order generated', 'Tap execution pending', 'Assistant engineer', 
'Metered connection entry', 'WorkOrder Generate',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'END', 'END', NULL,NULL, 'Tap Execution Date',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Revenue Clerk', 
 'CHANGEOFUSE', 'NEW', NULL, 'Assistant engineer',NULL, 'Forward,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');

