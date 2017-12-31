INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Exe Engineer Approved','','Estimation Notice Generation Pending','Executive Engineer','NEWSEWERAGECONNECTION','Payment Done Against Estimation','Donation Charges Payment Pending','Executive Engineer','ESTIMATIONAMOUNTPAID','Generate Estimation Notice',null,null,'2017-12-01','2099-04-01','0');

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Exe Engineer Approved','','Estimation Notice Generation Pending','Executive Engineer','CHANGEINCLOSETS','Payment Done Against Estimation','Donation Charges Payment Pending','Executive Engineer','ESTIMATIONAMOUNTPAID','Generate Estimation Notice',null,null,'2017-12-01','2099-04-01','0');

UPDATE eg_wf_matrix SET nextdesignation='Executive Engineer' ,validactions='Forward,Reject',nextstatus='ESTIMATIONAMOUNTPAID' where objecttype ='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentdesignation='Deputy Executive Engineer';

UPDATE eg_wf_matrix SET nextdesignation='Executive Engineer' ,validactions='Forward,Reject',nextstatus='ESTIMATIONAMOUNTPAID' where objecttype ='SewerageApplicationDetails' and additionalrule='CHANGEINCLOSETS' and currentdesignation='Deputy Executive Engineer';  

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Executive Engineer Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', 'NEWSEWERAGECONNECTION', 'Commisioner Approved', 'Work Order Generation Pending','Senior Assistant,Junior Assistant', 'FINALAPPROVED','Approve', NULL, NULL, '2017-12-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Executive Engineer Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', 'CHANGEINCLOSETS', 'Commisioner Approved', 'Work Order Generation Pending','Senior Assistant,Junior Assistant', 'FINALAPPROVED','Approve', NULL, NULL, '2017-12-01', '2099-04-01');

UPDATE eg_wf_matrix SET validactions ='Approve,Forward' , nextdesignation='Commissioner' where currentstate='Deputy Exe Engineer Approved' and pendingactions='Executive Engineer Approval Pending' and additionalrule='CHANGEINCLOSETS NOCOLLECTION';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Executive Engineer Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', 'CHANGEINCLOSETS NOCOLLECTION', 'Commisioner Approved', 'Work Order Generation Pending','Senior Assistant,Junior Assistant', 'FINALAPPROVED','Approve', NULL, NULL, '2017-12-01', '2099-04-01');

UPDATE eg_wf_matrix SET nextdesignation ='Commissioner' ,validactions='Approve,Forward' where currentstate='Deputy Exe Engineer Approved' and additionalrule='CLOSESEWERAGECONNECTION';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'SewerageApplicationDetails', 'Executive Engineer Approved', NULL, 'Commissioner Approval Pending', 'Commissioner', 'CLOSESEWERAGECONNECTION', 'Commisioner Approved', 'Close Connection Notice Generation Pending','Senior Assistant,Junior Assistant', 'FINALAPPROVED','Approve', NULL, NULL, '2017-12-01', '2099-04-01');

UPDATE eg_wf_matrix SET validactions ='Approve,Forward' , nextdesignation='Commissioner' where currentstate='Payment Done Against Estimation' and pendingactions='Executive Engineer Approval Pending' and additionalrule='NEWSEWERAGECONNECTION';

UPDATE eg_wf_matrix SET validactions ='Approve,Forward' , nextdesignation='Commissioner' where currentstate='Payment Done Against Estimation' and pendingactions='Executive Engineer Approval Pending' and additionalrule='CHANGEINCLOSETS';

UPDATE eg_wf_matrix SET pendingactions ='Close Connection Notice Generation Pending' , nextstate='END', nextaction='END',nextdesignation=NULL,nextstatus=NULL where currentdesignation='Commissioner' and additionalrule='CLOSESEWERAGECONNECTION';




