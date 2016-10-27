INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule,
 nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),
  'ANY', 'BudgetDetail', 'NEW', NULL, NULL, NULL, NULL, 'Created', 'HOD Approval Pending',
   NULL, 'Created', 'Forward,Cancel', NULL, NULL, now(), now(),0);

   
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule,
 nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),
  'ANY', 'BudgetDetail', 'Rejected', NULL, NULL, NULL, NULL, 'Created', 'HOD Approval Pending',
   NULL, NULL, 'Forward,Cancel', NULL, NULL, now(), now(),0);

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule,
 nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),
  'ANY', 'BudgetDetail', 'Cancelled', NULL, NULL, NULL, NULL, 'Created', 'HOD Approval Pending',
   NULL, 'Created', 'Forward', NULL, NULL, now(), now(),0);
   
   INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule,
 nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,version) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),
  'ANY', 'BudgetDetail', 'Created', NULL, NULL, NULL, NULL, 'END', 'END',
   NULL, NULL, 'Verify,Reject', NULL, NULL, now(), now(),0);


Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'BUDGETDETAIL','Created',now(),'CREATED',1);
