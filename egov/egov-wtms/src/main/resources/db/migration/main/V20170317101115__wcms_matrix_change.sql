
update eg_wf_matrix set nextdesignation='Assistant engineer,Assistant executive engineer,Tap Inspector' where
objecttype='WaterConnectionDetails' and currentstate='NEW' ;


update eg_wf_matrix set nextdesignation='Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner' where
objecttype='WaterConnectionDetails' and currentstate='Payment done against Estimation' ;



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant executive engineer', 
 'NEWCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant executive engineer', 
 'ADDNLCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant executive engineer', 
 'CHANGEOFUSE', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 update eg_wf_matrix set nextstate='Application Approval Pending' where   objecttype='WaterConnectionDetails'  and currentstate='Payment done against Estimation';

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Deputy Executive engineer', 
 'NEWCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Deputy Executive engineer', 
 'CHANGEOFUSE', 'Application Approval Pending', 'Approval Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Deputy Executive engineer', 
 'ADDNLCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'Application Approval Pending', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Assistant executive engineer', 
 'ADDNLCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Assistant engineer', 
 'NEWCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Assistant executive engineer', 
 'NEWCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Deputy Executive engineer,Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


update eg_wf_matrix set nextdesignation='Assistant executive engineer,Assistant engineer,Tap Inspector' where
objecttype='WaterConnectionDetails' and currentstate='Digital Signature Updated' ;



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Deputy Executive engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Digital Signature Pending', 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');

    INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Deputy Executive engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Digital Signature Pending', 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Deputy Executive engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Digital Signature Pending', 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');

update eg_wf_matrix set nextstate='Digital Signature Updated', nextdesignation='Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner' where
objecttype='WaterConnectionDetails' and currentstate='Commissioner Approved' and currentdesignation='Deputy Executive engineer';




update eg_wf_matrix set nextdesignation='Assistant executive engineer,Assistant engineer,Tap Inspector' where
objecttype='WaterConnectionDetails' and currentstate='Asst engg approved' ;



delete from eg_wf_matrix where objecttype='WaterConnectionDetails'  and currentstate='Estimate Notice Generated' and currentdesignation 
 in('Tap Inspector');

 update eg_wf_matrix set validactions='Approve,Forward,Preview,Sign' where     objecttype='WaterConnectionDetails'  and currentstate='Commissioner Approved' and currentdesignation ='Deputy Executive engineer';





  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant executive engineer', 
 'NEWCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');

 
  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant executive engineer', 
 'ADDNLCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');

 
  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant executive engineer', 
 'CHANGEOFUSE', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');

--after deputy approv n forwards to comm
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, null, 'Deputy Executive engineer', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'Commissioner approval pending', 'Sign,Preview', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, null, 'Deputy Executive engineer', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'Commissioner approval pending', 'Sign,Preview', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, null, 'Deputy Executive engineer', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'Commissioner approval pending', 'Sign,Preview', NULL, NULL, now(), '2099-04-01');