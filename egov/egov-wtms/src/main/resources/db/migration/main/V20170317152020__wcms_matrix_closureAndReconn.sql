update eg_wf_matrix  set currentstate='Applicaion Forwarded for Approval' where
objecttype='WaterConnectionDetails' and currentstate='Deputy Engineer Approved' and additionalrule in
('CLOSECONNECTION','RECONNECTION');



update eg_wf_matrix  set nextdesignation='Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner' where
objecttype='WaterConnectionDetails' and currentstate='Close Connection By AE' and additionalrule  in
('CLOSECONNECTION','RECONNECTION');



update eg_wf_matrix  set nextdesignation='Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner' where
objecttype='WaterConnectionDetails' and currentstate='ReConnection By AE' and additionalrule  in
('RECONNECTION');

update eg_wf_matrix  set nextdesignation='Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner' where
objecttype='WaterConnectionDetails' and currentstate='Close Connection By AE' and additionalrule  in
('CLOSECONNECTION');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'ReConnection By AE', NULL, NULL, 'Assistant executive engineer', 
'RECONNECTION', 'Applicaion Forwarded for Approval', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Assistant Engineer approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'Close Connection By AE', NULL, NULL, 'Assistant executive engineer', 
'CLOSECONNECTION', 'Applicaion Forwarded for Approval', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Assistant Engineer approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Deputy Executive engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 'Executive engineer,Municipal engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Sign,Preview,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'Close Connection By AE', NULL, NULL, 'Assistant executive engineer', 
'RECONNECTION', 'Applicaion Forwarded for Approval', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Assistant Engineer approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Deputy Executive engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Executive engineer,Municipal engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and currentstate='Deputy Executive Engineer approval pending' 
and additionalrule in
('CLOSECONNECTION','RECONNECTION');




update eg_wf_matrix  set nextdesignation='Assistant executive engineer,Assistant engineer,Tap Inspector' where
objecttype='WaterConnectionDetails' and currentstate in('NEW','Rejected') and additionalrule  in
('CLOSECONNECTION','RECONNECTION');

update eg_wf_matrix  set nextdesignation='Assistant executive engineer,Assistant engineer,Tap Inspector' where
objecttype='WaterConnectionDetails' and currentstate in('Rejected') and additionalrule  in
('NEWCONNECTION','ADDNLCONNECTION','CHANGEOFUSE');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Assistant engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 
'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Assistant executive engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 
'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Tap Inspector', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 
'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Tap Inspector', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Assistant executive engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Assistant engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');





INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Deputy Executive engineer', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Digital Signature Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Forward,Sign,Preview,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Deputy Executive engineer', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Digital Signature Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Forward,Sign,Preview,Reject',NULL, NULL, '2015-08-01', '2099-04-01');





INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'ReConn Approved By Commissioner', NULL, NULL, 'Deputy Executive engineer', 'RECONNECTION', 'ReConn Digital Sign Updated',
  'Acknowledgemnt pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Deputy Executive engineer', 'CLOSECONNECTION', 'Closure Digital Sign Updated',
  'Acknowledgemnt pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

update eg_wf_matrix  set validactions='Forward,Sign,Preview,Reject' where currentdesignation='Deputy Executive engineer'
and objecttype='WaterConnectionDetails' and currentstate in('Reconnection approve By Comm','Close approve By Comm') 
and additionalrule  in
('CLOSECONNECTION','RECONNECTION');



update eg_wf_matrix  set validactions='Forward,Approve,Sign,Preview,Reject' where currentdesignation='Deputy Executive engineer'
and objecttype='WaterConnectionDetails' and currentstate in('Close forwared By Approver','ReConn forwared By Approver') 
and additionalrule  in
('CLOSECONNECTION','RECONNECTION');
