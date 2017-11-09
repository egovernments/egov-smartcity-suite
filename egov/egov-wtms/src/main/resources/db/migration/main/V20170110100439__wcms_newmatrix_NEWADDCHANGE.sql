
delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and additionalrule='NEWCONNECTION' ;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'NEWCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 'Clerk Approved', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'NEWCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 null, 'Forward,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Assistant engineer', 
 'NEWCONNECTION', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Assistant Engineer approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant engineer', 
 'NEWCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Digital Signature Updated', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'NEWCONNECTION', 'Work order generated', 'Tap execution pending', 'Assistant engineer', 
 'Metered connection entry', 'Generate WorkOrder', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Asst engg approved', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'NEWCONNECTION', 'Estimate Notice Generated', 'Ready For Payment', 'Assistant engineer', 
 'Ready for Payment', 'Generate Estimation Notice', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, null, 
 'NEWCONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Senior Assistant,Junior Assistant', 
 'Clerk Approved Pending', 'Forward', NULL, NULL, now(), '2099-04-01');





 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Commissioner', 
 'NEWCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 
 'Digital Signature Pending', 'Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Assistant engineer', 
 'NEWCONNECTION', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Assistant engineer', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant engineer', 
 'NEWCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approval pending', NULL, NULL, 
'Deputy Executive engineer', 'NEWCONNECTION', 'Deputy Engineer Approved', 'Approval pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner','Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

--after deputy forward respective user

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'Engineer Approval Pending', 'Executive engineer', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'MunicipalEng Approval Pending', 'Municipal engineer', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval pending', 'Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'SuperintendEng Approval Pending', 'Superintendent engineer', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'SuperintendEng Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');


--once approed by EE,ME,SE 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Executive engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Municipal Engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Superintendent Engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Executive Engineer Approved', NULL, NULL, 'Executive engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Executive engineer', 
 'NEWCONNECTION', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Municipal Engineer', 
 'NEWCONNECTION', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Superintendent Engineer', 
 'NEWCONNECTION', 'Digital Signature Updated', 'Digital Signature Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');




-- for ADDNCONNECTION
delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and additionalrule='ADDNLCONNECTION' ;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'ADDNLCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 'Clerk Approved', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'ADDNLCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 null, 'Forward,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Assistant engineer', 
 'ADDNLCONNECTION', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Assistant Engineer approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant engineer', 
 'ADDNLCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Digital Signature Updated', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'ADDNLCONNECTION', 'Work order generated', 'Tap execution pending', 'Assistant engineer', 
 'Metered connection entry', 'Generate WorkOrder', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Asst engg approved', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'ADDNLCONNECTION', 'Estimate Notice Generated', 'Ready For Payment', 'Assistant engineer', 
 'Ready for Payment', 'Generate Estimation Notice', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, null, 
 'ADDNLCONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Senior Assistant,Junior Assistant', 
 'Clerk Approved Pending', 'Forward', NULL, NULL, now(), '2099-04-01');





 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Commissioner', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 
 'Digital Signature Pending', 'Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Assistant engineer', 
 'ADDNLCONNECTION', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Assistant engineer', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant engineer', 
 'ADDNLCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approval pending', NULL, NULL, 
'Deputy Executive engineer', 'ADDNLCONNECTION', 'Deputy Engineer Approved', 'Approval pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner','Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

--after deputy forward respective user

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'Engineer Approval Pending', 'Executive engineer', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'MunicipalEng Approval Pending', 'Municipal engineer', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval pending', 'Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'SuperintendEng Approval Pending', 'Superintendent engineer', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'SuperintendEng Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');


--once approed by EE,ME,SE 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Executive engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Municipal Engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Superintendent Engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Executive Engineer Approved', NULL, NULL, 'Executive engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Executive engineer', 
 'ADDNLCONNECTION', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Municipal Engineer', 
 'ADDNLCONNECTION', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Superintendent Engineer', 
 'ADDNLCONNECTION', 'Digital Signature Updated', 'Digital Signature Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');




--changeofuse
delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and additionalrule='CHANGEOFUSE' ;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'CHANGEOFUSE', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 'Clerk Approved', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'CHANGEOFUSE', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant engineer', 
 null, 'Forward,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Assistant Engineer approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Digital Signature Updated', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'CHANGEOFUSE', 'Work order generated', 'Tap execution pending', 'Assistant engineer', 
 'Metered connection entry', 'Generate WorkOrder', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Asst engg approved', NULL, NULL, 'Senior Assistant,Junior Assistant', 
 'CHANGEOFUSE', 'Estimate Notice Generated', 'Ready For Payment', 'Assistant engineer', 
 'Ready for Payment', 'Generate Estimation Notice', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, null, 
 'CHANGEOFUSE', 'NEW', 'Revenue Clerk approval pending', 'Senior Assistant,Junior Assistant', 
 'Clerk Approved Pending', 'Forward', NULL, NULL, now(), '2099-04-01');





 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Commissioner', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Senior Assistant,Junior Assistant', 
 'Digital Signature Pending', 'Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Assistant engineer', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Assistant engineer', 
 'CHANGEOFUSE', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnectionDetails', 'Deputy Executive Engineer approval pending', NULL, NULL, 
'Deputy Executive engineer', 'CHANGEOFUSE', 'Deputy Engineer Approved', 'Approval pending', 'Executive engineer,Municipal Engineer,Superintendent Engineer,Commissioner','Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

--after deputy forward respective user

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'Engineer Approval Pending', 'Executive engineer', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'MunicipalEng Approval Pending', 'Municipal engineer', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval pending', 'Superintendent Engineer,Commissioner', 
 'Municipal Engineer Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');

 

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, 'SuperintendEng Approval Pending', 'Superintendent engineer', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval pending', 'Commissioner', 
 'SuperintendEng Approval pending', 'Forward,Approve', NULL, NULL, now(), '2099-04-01');


--once approed by EE,ME,SE 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Executive engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Municipal Engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Commissioner Approved', NULL, NULL, 'Superintendent Engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'WorkOrder Print Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Executive Engineer Approved', NULL, NULL, 'Executive engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Executive engineer', 
 'CHANGEOFUSE', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Municipal Engineer', 
 'CHANGEOFUSE', 'Engineer Executive Forwarded', 'Digital Signature Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Superintendent Engineer', 
 'CHANGEOFUSE', 'Digital Signature Updated', 'Digital Signature Pending', 'Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');
 
 
 update  eg_wf_matrix set nextstate='Commissioner Approved' where objecttype='WaterConnectionDetails'
and currentstate='Engineer Executive Forwarded' and currentdesignation='Superintendent Engineer';


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Executive engineer', 
 'NEWCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Municipal Engineer', 
 'NEWCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Superintendent Engineer', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 
 'WaterConnectionDetails', 'Superintendent Executive Forwarded', NULL, NULL, 'Commissioner', 'NEWCONNECTION', 
 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', '
 Digital Signature Pending',
  'Approve', NULL, NULL, '2015-08-01', '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Executive engineer', 
 'CHANGEOFUSE', 'Application Approval Pending', 'Approval Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Municipal Engineer', 
 'CHANGEOFUSE', 'Application Approval Pending', 'Approval Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Superintendent Engineer', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 
 'WaterConnectionDetails', 'Superintendent Executive Forwarded', NULL, NULL, 'Commissioner', 'CHANGEOFUSE', 
 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', '
 Digital Signature Pending',
  'Approve', NULL, NULL, '2015-08-01', '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Executive engineer', 
 'ADDNLCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Municipal Engineer,Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward,Preview,Sign', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Municipal Engineer', 
 'ADDNLCONNECTION', 'Application Approval Pending', 'Approval Pending', 'Superintendent Engineer,Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Superintendent Engineer', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve,Forward', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,
 validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 
 'WaterConnectionDetails', 'Superintendent Executive Forwarded', NULL, NULL, 'Commissioner', 'ADDNLCONNECTION', 
 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', '
 Digital Signature Pending',
  'Approve', NULL, NULL, '2015-08-01', '2099-04-01');

update   eg_wf_matrix set nextstate='Superintendent Executive Forwarded' where objecttype='WaterConnectionDetails'
and currentstate='Application Approval Pending' and currentdesignation='Superintendent Engineer';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Tap Inspector', 
 'NEWCONNECTION', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Tap Inspector approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Tap Inspector', 
 'NEWCONNECTION', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Tap Inspector', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');

update eg_wf_matrix set nextdesignation='Assistant engineer,Tap Inspector' where objecttype='WaterConnectionDetails' 
 and currentstate='Asst engg approved';


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Tap Inspector', 
 'ADDNLCONNECTION', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Tap Inspector approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Tap Inspector', 
 'ADDNLCONNECTION', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Tap Inspector', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Clerk approved', NULL, NULL, 'Tap Inspector', 
 'CHANGEOFUSE', 'Asst engg approved', 'Estimation Notice print pending', 'Senior Assistant,Junior Assistant', 
 'Tap Inspector approved', 'Submit,Reject', NULL, NULL, now(), '2099-04-01');



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Estimate Notice Generated', NULL, NULL, 'Tap Inspector', 
 'CHANGEOFUSE', 'Payment done against Estimation', 'AssistantEng Approval pending', 'Tap Inspector', 
 'Payment against Estimation', 'Forward,Reject', NULL, NULL, now(), '2099-04-01');


update eg_wf_matrix set nextdesignation='Assistant engineer,Tap Inspector' where objecttype='WaterConnectionDetails' 
 and currentstate='Digital Signature Updated';



 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Tap Inspector', 
 'NEWCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Tap Inspector', 
 'ADDNLCONNECTION', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Work order generated', NULL, NULL, 'Tap Inspector', 
 'CHANGEOFUSE', 'END', 'END', null, 
 null, 'Execute Tap', NULL, NULL, now(), '2099-04-01');



update eg_wf_matrix set nextdesignation='Assistant engineer,Tap Inspector' where objecttype='WaterConnectionDetails' 
 and currentstate='Asst engg approved';

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Tap Inspector', 
 'NEWCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Tap Inspector', 
 'ADDNLCONNECTION', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Payment done against Estimation', NULL, NULL, 'Tap Inspector', 
 'CHANGEOFUSE', 'Deputy Executive Engineer approval pending', 'Approval pending', 'Deputy Executive engineer', 
 'Approval pending', 'Forward', NULL, NULL, now(), '2099-04-01');




 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, NULL, 'Commissioner', 
 'NEWCONNECTION', 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', 
 'Digital Signature Pending', 'Approve', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, NULL, 'Commissioner', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', 
 'Digital Signature Pending', 'Approve', NULL, NULL, now(), '2099-04-01');


 
 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Deputy Engineer Approved', NULL, NULL, 'Commissioner', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', 
 'Digital Signature Pending', 'Approve', NULL, NULL, now(), '2099-04-01');
 


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Commissioner', 
 'NEWCONNECTION', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Commissioner', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve', NULL, NULL, now(), '2099-04-01');


  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Application Approval Pending', NULL, NULL, 'Commissioner', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Approval Pending', 'Commissioner', 
 NULL, 'Approve', NULL, NULL, now(), '2099-04-01');