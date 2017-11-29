update eg_wf_matrix set validactions='Reject' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentstate='Inspection Fee Pending' and currentdesignation='Assistant Engieer';

update eg_wf_matrix set currentdesignation='Assistant Engineer' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentdesignation='Assistant Engieer' and currentstate in ('Inspection Fee Pending','Inspection Fee Collected');

update eg_wf_matrix set currentstate='Deputy Exe Engineer Approved' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentstate='Deputy Executive Engineer Approved';

update eg_wf_matrix set pendingactions='Rejected Inspection Fee Collection' where objecttype='SewerageApplicationDetails'
and additionalrule='NEWSEWERAGECONNECTION' and pendingactions='Inspection Fee Collection' 
and currentstate ='Rejected';

update eg_wf_matrix set pendingactions='Rejected' where objecttype='SewerageApplicationDetails'
and additionalrule='NEWSEWERAGECONNECTION' and pendingactions=''
and currentstate ='Rejected';
