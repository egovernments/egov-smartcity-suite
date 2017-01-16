delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION' ;


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus,
 pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
 nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'),
  'ANY','WaterConnectionDetails', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 'CLOSECONNECTION', 'Close Connection By AE',
   'close approval pending By AE', 
'Assistant engineer,Tap Inspector', 
'Connection Initiated', 'Forward',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'Close Connection By AE', NULL, NULL, 'Assistant engineer', 
'CLOSECONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer',
 'Assistant Engineer approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'Close Connection By AE', NULL, NULL, 'Tap Inspector', 
'CLOSECONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer',
 'Tap Inspector approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approval pending', NULL, NULL, 
'Deputy Executive engineer', 'CLOSECONNECTION', 'Deputy Engineer Approved', 'Approval pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner','Approval pending', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'Engineer Approval Pending', 'Executive engineer', 
 'CLOSECONNECTION', 'Close approve By Comm', 'Approval pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'MunicipalEng Approval Pending', 'Municipal engineer', 
 'CLOSECONNECTION', 'Close approve By Comm', 'Approval pending', 'Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'SuperintendEng Approval Pending', 'Superintendent engineer', 
 'CLOSECONNECTION', 'Close approve By Comm', 'Approval pending', 'Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, NULL, 'Commissioner', 
 'CLOSECONNECTION', 'Close approve By Comm', 'Digital Signature Pending', 'Commissioner', 'Approval pending', 'Approve', NULL, NULL, now(), '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Municipal engineer', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Digital Signature Pending', 'Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Superintendent Engineer', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Digital Signature Pending', 'Commissioner',
 'Digital Signature Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Commissioner', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Digital Signature Pending', 'Commissioner',
 'Digital Signature Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Executive engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 'Municipal engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Municipal engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 'Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Superintendent Engineer', 'CLOSECONNECTION', 
'Close forwared By Approver', 'Approval Pending', 'Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close forwared By Approver', NULL, NULL, 'Commissioner', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Approval Pending', 'Commissioner',
 'Approval Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Executive engineer', 'CLOSECONNECTION', 'Closure Digital Sign Updated',
  'Acknowledgemnt pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Municipal Engineer', 'CLOSECONNECTION', 'Closure Digital Sign Updated',
  'Acknowledgemnt pending', 'Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Superintendent Engineer', 'CLOSECONNECTION', 'Closure Digital Sign Updated',
  'Acknowledgemnt pending', 'Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'Closure Approved By Commissioner', NULL, NULL, 'Commissioner', 'CLOSECONNECTION', 'Closure Digital Sign Updated',
  'Acknowledgemnt pending', 'Senior Assistant,Junior Assistant', 'Acknowledgemnt pending', 'Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'),
 'ANY','WaterConnectionDetails', 'Closure Digital Sign Updated',NULL, NULL, 'Senior Assistant,Junior Assistant', 
'CLOSECONNECTION', 'END', 'END', NULL,NULL,'Generate Acknowledgement',NULL, NULL, '2015-08-01', '2099-04-01');





INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY','WaterConnectionDetails', 
'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 'CLOSECONNECTION', 'Close Connection By AE', 
'Close Connection approval pending By AE', 
'Assistant engineer,Tap Inspector', 
NULL, 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



update eg_wf_matrix set nextaction='Digital Signature Pending' 
where additionalrule='CLOSECONNECTION' and currentstate='Closure Approved By Commissioner' and currentdesignation='Commissioner';


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Close approve By Comm', NULL, NULL, 'Executive engineer', 'CLOSECONNECTION', 
'Closure Approved By Commissioner', 'Digital Signature Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and additionalrule='RECONNECTION' ;


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus,
 pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
 nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'),
  'ANY','WaterConnectionDetails', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 'RECONNECTION', 'ReConnection By AE',
   'close approval pending By AE', 
'Assistant engineer,Tap Inspector', 
'ReConnection Initiated', 'Forward',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'ReConnection By AE', NULL, NULL, 'Assistant engineer', 
'RECONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer',
 'Assistant Engineer approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, 
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY','WaterConnectionDetails', 'ReConnection By AE', NULL, NULL, 'Tap Inspector', 
'RECONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer',
 'Tap Inspector approved', 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approval pending', NULL, NULL, 
'Deputy Executive engineer', 'RECONNECTION', 'Deputy Engineer Approved', 'Approval pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner','Approval pending', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'Engineer Approval Pending', 'Executive engineer', 
 'RECONNECTION', 'Reconnection approve By Comm', 'Approval pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'MunicipalEng Approval Pending', 'Municipal engineer', 
 'RECONNECTION', 'Reconnection approve By Comm', 'Approval pending', 'Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'SuperintendEng Approval Pending', 'Superintendent engineer', 
 'RECONNECTION', 'Reconnection approve By Comm', 'Approval pending', 'Commissioner', 
 'Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, NULL, 'Commissioner', 
 'RECONNECTION', 'Reconnection approve By Comm', 'Digital Signature Pending', 'Commissioner', 'Approval pending', 'Approve', NULL, NULL, now(), '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Municipal engineer', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Digital Signature Pending', 'Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Superintendent Engineer', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Digital Signature Pending', 'Commissioner',
 'Digital Signature Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Commissioner', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Digital Signature Pending', 'Commissioner',
 'Digital Signature Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Executive engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Municipal engineer,Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Municipal engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Superintendent Engineer,Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Superintendent Engineer', 'RECONNECTION', 
'ReConn forwared By Approver', 'Approval Pending', 'Commissioner',
 'Approval Pending', 'Forward,Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'ReConn forwared By Approver', NULL, NULL, 'Commissioner', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Approval Pending', 'Commissioner',
 'Approval Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'ReConn Approved By Commissioner', NULL, NULL, 'Executive engineer', 'RECONNECTION', 'ReConn Digital Sign Updated',
  'Acknowledgemnt pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'ReConn Approved By Commissioner', NULL, NULL, 'Municipal Engineer', 'RECONNECTION', 'ReConn Digital Sign Updated',
  'Acknowledgemnt pending', 'Superintendent Engineer,Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'ReConn Approved By Commissioner', NULL, NULL, 'Superintendent Engineer', 'RECONNECTION', 'ReConn Digital Sign Updated',
  'Acknowledgemnt pending', 'Commissioner', 'Acknowledgemnt pending', 'Forward,Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 
 'ReConn Approved By Commissioner', NULL, NULL, 'Commissioner', 'RECONNECTION', 'ReConn Digital Sign Updated',
  'Acknowledgemnt pending', 'Senior Assistant,Junior Assistant', 'Acknowledgemnt pending', 'Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'),
 'ANY','WaterConnectionDetails', 'ReConn Digital Sign Updated',NULL, NULL, 'Senior Assistant,Junior Assistant', 
'RECONNECTION', 'END', 'END', NULL,NULL,'Generate Reconnection Ack',NULL, NULL, '2015-08-01', '2099-04-01');





INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY','WaterConnectionDetails', 
'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 'RECONNECTION', 'ReConnection By AE', 
'Close Connection approval pending By AE', 
'Assistant engineer,Tap Inspector', 
NULL, 'Forward,Reject',NULL, NULL, '2015-08-01', '2099-04-01');



update eg_wf_matrix set nextaction='Digital Signature Pending' 
where additionalrule='RECONNECTION' and currentstate='ReConn Approved By Commissioner' and currentdesignation='Commissioner';


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate)VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY',
'WaterConnectionDetails', 'Reconnection approve By Comm', NULL, NULL, 'Executive engineer', 'RECONNECTION', 
'ReConn Approved By Commissioner', 'Digital Signature Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner',
 'Digital Signature Pending', 'Approve,Reject',NULL, NULL, '2015-08-01', '2099-04-01');