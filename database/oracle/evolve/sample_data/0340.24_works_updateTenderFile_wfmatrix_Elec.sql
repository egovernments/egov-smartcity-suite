#UP

update eg_wf_matrix set
 currentdesignation='JUNIOR ENGINEER', nextaction='Pending for Check', nextdesignation='ASSISTANT ENGINEER'
  where objecttype='TenderFile' and currentstate='NEW' and department='Electrical';

update eg_wf_matrix set
 currentdesignation='ASSISTANT ENGINEER', pendingactions='Pending for Check', nextstate='CHECKED', nextaction='Pending for Approval',
 nextdesignation='EXECUTIVE ENGINEER', nextstatus='CHECKED', validactions='Forward,Reject'
  where objecttype='TenderFile' and currentstate='CREATED' and department='Electrical';

update eg_wf_matrix set 
 currentdesignation='JUNIOR ENGINEER', nextaction='Pending for Check', nextdesignation='ASSISTANT ENGINEER'
 where objecttype='TenderFile' and currentstate='REJECTED' and department='Electrical'; 

update eg_wf_matrix set 
 currentdesignation='ASSISTANT ENGINEER', pendingactions='Pending for Check', nextstate='CHECKED', nextaction='Pending for Approval', 
 nextdesignation='EXECUTIVE ENGINEER', nextstatus='CHECKED', validactions='Forward,Reject'
 where objecttype='TenderFile' and currentstate='RESUBMITTED' and department='Electrical';


Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,
NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Electrical','TenderFile','CHECKED',null,'Pending for Approval',
'EXECUTIVE ENGINEER',null,'APPROVED','END',null,'APPROVED','Approve,Reject',null,null);

#DOWN

delete from eg_wf_matrix 
 where objecttype='TenderFile' and currentstate='CHECKED' and pendingactions='Pending for Approval' and department='Electrical';

update eg_wf_matrix set
 currentdesignation='JUNIOR ENGINEER,ASSISTANT ENGINEER', nextaction='Pending for Approval', nextdesignation='EXECUTIVE ENGINEER'
  where objecttype='TenderFile' and currentstate='NEW' and department='Electrical';

update eg_wf_matrix set
 currentdesignation='EXECUTIVE ENGINEER', pendingactions='Pending for Approval', nextstate='APPROVED', nextaction='END',
 nextdesignation=null, nextstatus='APPROVED', validactions='Approve,Reject'
  where objecttype='TenderFile' and currentstate='CREATED' and department='Electrical';

update eg_wf_matrix set 
 currentdesignation='JUNIOR ENGINEER,ASSISTANT ENGINEER', nextaction='Pending for Approval', nextdesignation='EXECUTIVE ENGINEER'
 where objecttype='TenderFile' and currentstate='REJECTED' and department='Electrical'; 

update eg_wf_matrix set 
 currentdesignation='EXECUTIVE ENGINEER', pendingactions='Pending for Approval', nextstate='APPROVED', nextaction='END', 
 nextdesignation=null, nextstatus='APPROVED', validactions='Approve,Reject'
 where objecttype='TenderFile' and currentstate='RESUBMITTED' and department='Electrical';






