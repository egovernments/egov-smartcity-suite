INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Commissioner', 
 'NEWCONNECTION', 'Commissioner Approved', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Preview,Sign', NULL, NULL, now(), '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Commissioner', 
 'ADDNLCONNECTION', 'Commissioner Approved', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Preview,Sign', NULL, NULL, now(), '2099-04-01');


 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Engineer Executive Forwarded', NULL, NULL, 'Commissioner', 
 'CHANGEOFUSE', 'Commissioner Approved', 'Digital Signature Pending', 'Senior Assistant,Junior Assistant', 
 NULL, 'Preview,Sign', NULL, NULL, now(), '2099-04-01');