
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Revenue Clerk', 
 'RECONNECTION', 'ReConnection By AE', 'Reconnection approval pending By AE', 'Assistant engineer', 
'Connection Initiated', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'ReConnection By AE', NULL, NULL, 'Assistant engineer', 
 'RECONNECTION', 'Reconnection approve By Comm', 'Reconnection Approval by Commissioner', 
 'Commissioner', 
'Assistant Engineer approved', 'Forward,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Commissioner', 
 'RECONNECTION', 'Reconnection Acknowledgemnt', 'Acknowledgemnt Print pending', 'Revenue Clerk', 
'CloseApprov by Commissioner', 'Approve',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Reconnection Acknowledgemnt',NULL, NULL, 'Revenue Clerk', 
 'RECONNECTION', 'END', 'END', NULL,NULL,'Generate Reconnection Ack',
 NULL, NULL, '2015-08-01', '2099-04-01');

  INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Revenue Clerk', 
 'RECONNECTION', 'ReConnection By AE', 'Reconnection approval pending By AE', 'Assistant engineer',NULL, 'Forward,Reject',
 NULL, NULL, '2015-08-01', '2099-04-01');
 
 
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','ReConnectionIntiated',now(),
 'RECONNECTIONINITIATED',1);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','ReConnectionInProgress',now(),
 'RECONNECTIONINPROGRESS',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','ReConnectionApproved',now(),
 'RECONNECTIONAPPROVED',1);

 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID)
 values (nextval('SEQ_EGW_STATUS'),'WATERTAXAPPLICATION','ReConnectionSanctioned',now(),
 'RECONNECTIONSANCTIONED',1);