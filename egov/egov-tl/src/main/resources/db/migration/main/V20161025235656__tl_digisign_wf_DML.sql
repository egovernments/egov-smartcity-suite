INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY', 'TradeLicense','Digital sign-Second level fee collected', 'Digital Signature Pending', NULL, 
'Commissioner','NEWTRADELICENSE','Certificate generation pending', 'Digitally Signed', 'Revenue Clerk,Junior Assistant', 
 'Certificate generation pending', 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY', 'TradeLicense','Digital sign-Second level fee collected', 'Digital Signature Pending', NULL, 
'Commissioner','RENEWTRADELICENSE','Certificate generation pending', 'Digitally Signed', 'Revenue Clerk,Junior Assistant', 
 'Certificate generation pending', 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Digital signed', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Digital signed', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY', 'TradeLicense','Digital sign-Commissioner Approved no collection', 'Digital Signature Pending', NULL, 
'Commissioner','NEWTRADELICENSE','Certificate generation pending', 'Digitally Signed', 'Commissioner', 
 'Certificate generation pending', 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
'ANY', 'TradeLicense','Digital sign-Commissioner Approved no collection', 'Digital Signature Pending', NULL, 
'Commissioner','RENEWTRADELICENSE','Certificate generation pending', 'Digitally Signed', 'Commissioner', 
 'Certificate generation pending', 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01',0);
