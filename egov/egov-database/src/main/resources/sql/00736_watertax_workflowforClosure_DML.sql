
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Revenue Clerk', 
 'CLOSECONNECTION', 'Close Connection By AE', 'close approval pending By AE', 'Assistant engineer', 
'Connection Initiated', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Close Connection By AE', NULL, NULL, 'Assistant engineer', 
 'CLOSECONNECTION', 'Close approve By Comm', 'Close Approval by Commissioner', 
 'Commissioner', 
'Assistant Engineer approved', 'Forward,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Commissioner', 
 'CLOSECONNECTION', 'Generate Acknowledgemnt', 'acknowledgemnt pending', 'Revenue Clerk', 
'CloseApprov by Commissioner', 'Approve,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Generate Acknowledgemnt',NULL, NULL, 'Revenue Clerk', 
 'CLOSECONNECTION', 'END', 'END', NULL,NULL,'Generate Acknowledgement',
 NULL, NULL, '2015-08-01', '2099-04-01');

  INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Revenue Clerk', 
 'CLOSECONNECTION', 'Close Connection By AE', 'Close Connection approval pending By AE', 'Assistant engineer',NULL, 'Forward,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','CloserInitiated',now(),
 'CLOSERINITIATED',1);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','CloserInProgress',now(),
 'CLOSERINPROGRESS',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','CloserApproved',now(),
 'CLOSERAPPROVED',1);

 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','CloserSanctioned',now(),
 'CLOSERSANCTIONED',1);