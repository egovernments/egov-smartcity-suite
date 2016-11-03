---new egw_status
Insert into egw_status values (nextval('seq_egw_status'),'TRADELICENSE','Second level Collection Pending',now(),'SECONDLVLCOLLECTIONPENDING',1);
update egw_status set description ='First level collection done',code='FIRSTLVLCOLLECTIONDONE' where description='Collection Pending' and moduletype='TRADELICENSE';
Insert into egw_status values (nextval('seq_egw_status'),'TRADELICENSE','Rejected',now(),'REJECTED',1);
Insert into egw_status values (nextval('seq_egw_status'),'TRADELICENSE','Cancelled',now(),'CANCELLED',1);

--Delete existing workflow matrix for tradelicense

delete from eg_wf_matrix where objecttype='TradeLicense';

---NEW Workflow matrix
INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','NEW', NULL, NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'License Created',
 'First level Collection pending', 'Revenue Clerk,Junior Assistant', 'Clerk Initiated',
 'Forward', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','License Created', 'Clerk Initiated', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'First level fee collected',
 'Revenue clerk approval pending', 'Revenue Clerk,Junior Assistant', 'First level fee collected',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','First level fee collected', 'Revenue clerk approval pending', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'Revenue clerk approved',
 'SI/MHO approval pending', 'Sanitary inspector,Medical store officer', 'SI/MHO approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Revenue clerk approved', 'SI/MHO approval pending', NULL, 
'Sanitary inspector,Medical store officer', 'NEWTRADELICENSE', 'SI/MHO approved',
 'Commissioner approval pending', 'Commissioner', 'Commissioner approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','SI/MHO approved', 'Commissioner approval pending', NULL, 
'Commissioner', 'NEWTRADELICENSE', 'Commissioner approved',
 'Second level collection pending', 'Commissioner', 'Second level fee pending',
 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Commissioner approved', 'Second level fee pending', NULL, 
'Commissioner', 'NEWTRADELICENSE', 'Second level fee collected',
 'Certificate generation pending', 'Revenue Clerk,Junior Assistant', 'Certificate generation pending',
 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Second level fee collected', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Commissioner approved no collection', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Rejected', 'Rejected', NULL, 
'Revenue Clerk,Junior Assistant', 'NEWTRADELICENSE', 'Revenue clerk approved',
 'SI/MHO approval pending', 'Sanitary inspector,Medical store officer', 'SI/MHO approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

---RENEW workflow matrix

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','NEW', NULL, NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'License Created',
 'First level Collection pending', 'Revenue Clerk,Junior Assistant', 'Clerk Initiated',
 'Forward', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','License Created', 'Clerk Initiated', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'First level fee collected',
 'Revenue clerk approval pending', 'Revenue Clerk,Junior Assistant', 'First level fee collected',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','First level fee collected', 'Revenue clerk approval pending', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'Revenue clerk approved',
 'SI/MHO approval pending', 'Sanitary inspector,Medical store officer', 'SI/MHO approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Revenue clerk approved', 'SI/MHO approval pending', NULL, 
'Sanitary inspector,Medical store officer', 'RENEWTRADELICENSE', 'SI/MHO approved',
 'Commissioner approval pending', 'Commissioner', 'Commissioner approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','SI/MHO approved', 'Commissioner approval pending', NULL, 
'Commissioner', 'RENEWTRADELICENSE', 'Commissioner approved',
 'Second level collection pending', 'Commissioner', 'Second level fee pending',
 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Commissioner approved', 'Second level fee pending', NULL, 
'Commissioner', 'RENEWTRADELICENSE', 'Second level fee collected',
 'Certificate generation pending', 'Revenue Clerk,Junior Assistant', 'Certificate generation pending',
 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Second level fee collected', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);


INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Commissioner approved no collection', 'Certificate generation pending', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'END',
 'END', '', 'Certificate generated',
 'Generate Certificate', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Rejected', 'Rejected', NULL, 
'Revenue Clerk,Junior Assistant', 'RENEWTRADELICENSE', 'Revenue clerk approved',
 'SI/MHO approval pending', 'Sanitary inspector,Medical store officer', 'SI/MHO approval pending',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);


