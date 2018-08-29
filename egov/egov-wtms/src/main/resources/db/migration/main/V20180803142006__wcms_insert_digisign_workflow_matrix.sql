

delete from eg_wf_matrix where objecttype ='WaterConnectionDetails' and additionalrule='NEWCONNECTION' and 
currentstate='Commissioner Approved' and currentdesignation='Commissioner' and nextstate='END';

delete from eg_wf_matrix where objecttype ='WaterConnectionDetails' and additionalrule='CHANGEOFUSE' and 
currentstate='Commissioner Approved' and currentdesignation='Commissioner' and nextstate='END';

delete from eg_wf_matrix where objecttype ='WaterConnectionDetails' and additionalrule='ADDNLCONNECTION' and 
currentstate='Commissioner Approved' and currentdesignation='Commissioner' and nextstate='END';


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'NEWCONNECTION', 'END',
  'END', 'Commissioner', 'END', 'Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'CHANGEOFUSE', 'END',
  'END', 'Commissioner', 'END', 'Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions,
 currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, 
 fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 
 'Commissioner Approved', NULL, NULL, 'Commissioner', 'ADDNLCONNECTION', 'END',
  'END', 'Commissioner', 'END', 'Preview,Sign', NULL, NULL,
   '2015-08-01', '2099-04-01');
