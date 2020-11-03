INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Third Party operator created', NULL, NULL, NULL, 'CHANGEINCLOSETS', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'WARDSECRETARYCREATED','Forward', NULL, NULL, '2020-08-12', '2099-04-01');

