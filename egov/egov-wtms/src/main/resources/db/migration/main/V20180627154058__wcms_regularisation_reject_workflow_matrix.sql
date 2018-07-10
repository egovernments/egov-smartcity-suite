
delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and currentstate='Clerk approved' and pendingactions='Pending rejection by Assistant Engineer' and additionalrule='REGLZNCONNECTION' and currentdesignation='Assistant Engineer' and validactions='Forward,Cancel';

delete from eg_wf_matrix where objecttype='WaterConnectionDetails' and currentstate='Rejected' and pendingactions='Pending approval by Assistant Engineer' and additionalrule='REGLZNCONNECTION' and currentdesignation='Senior Assistant,Junior Assistant' and validactions='Forward,Cancel';


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Clerk approved', null, 'Pending rejection by Assistant Engineer', 'Assistant Engineer', 'REGLZNCONNECTION', 'Rejected', 'Clerk approval pending', 'Senior Assistant,Junior Assistant', 'Rejected', 'Forward,Cancel', null, null, '2016-04-01', '2099-04-01');


INSERT INTO EG_WF_MATRIX (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction,nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'WaterConnectionDetails', 'Rejected', null, 'Pending approval by Assistant Engineer', 'Senior Assistant,Junior Assistant', 'REGLZNCONNECTION', 'Clerk approved', 'Asst.engineer approval pending', 'Assistant Engineer', 'Clerk approved', 'Forward,Cancel', null, null, '2016-04-01', '2099-04-01');
