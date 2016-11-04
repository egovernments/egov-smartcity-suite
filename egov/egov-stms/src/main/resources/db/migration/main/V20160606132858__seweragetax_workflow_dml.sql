update eg_wf_matrix set nextstate='Clerk Approved', nextaction='Assistant Engineer Approval Pending', nextdesignation='Assistant Engineer',
nextstatus='CREATED' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and pendingactions='Rejected' 
and currentstate ='Rejected';

update eg_wf_matrix set pendingactions='Deputy Exe Engineer Approval Pending', nextstate='Deputy Exe Engineer Approved'
where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and pendingactions='Deputy Executive Engineer Approval Pending' and currentstate ='Assistant Engineer Approved' and nextstate='Deputy Executive Engineer Approved';

update eg_wf_matrix set nextaction='Deputy Exe Engineer Approval Pending' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and nextaction='Deputy Executive Engineer Approval Pending' and currentstate ='Inspection Fee Collected'; 

update eg_wf_matrix set nextaction='Deputy Exe Engineer Approval Pending',nextdesignation='Deputy Executive Engineer',
validactions='Forward,Reject' where objecttype='SewerageApplicationDetails'
and additionalrule='NEWSEWERAGECONNECTION' and nextaction='Estimation Notice Generation Pending' and currentstate ='Clerk Approved';
