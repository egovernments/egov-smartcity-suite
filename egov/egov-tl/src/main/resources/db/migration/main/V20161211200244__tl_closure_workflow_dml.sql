update eg_wf_types set  link =':ID' where type='TradeLicense' and displayname='Trade License';
update eg_action set url ='/viewtradelicense/viewTradeLicense-closure.action' where name='viewclosurelicense';

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','NEW', NULL, NULL, 
'Revenue Clerk,Junior Assistant', 'CLOSURELICENSE', 'Revenue Clerk/JA Approved',
 'SI/SS Approval Pending', 'Sanitary inspector,Medical store officer', 'Closure Initiated',
 'Forward', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Revenue Clerk/JA Approved', 'Revenue Clerk/JA Approved', NULL, 
'Sanitary inspector,Medical store officer', 'CLOSURELICENSE', 'SI/SS Approved',
 'Commissioner Approval Pending', 'Commissioner', 'SI/SS Approved',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','SI/SS Approved', 'SI/SS Approved', NULL, 
'Commissioner', 'CLOSURELICENSE', 'END',
 'END', null, 'License Cancelled',
 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'ANY', 'TradeLicense','Rejected', 'Rejected', NULL, 
'Revenue Clerk,Junior Assistant', 'CLOSURELICENSE', 'Revenue Clerk/JA Approved',
 'SI/SS Approval Pending', 'Sanitary inspector,Medical store officer', 'Closure Initiated',
 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01',0);

