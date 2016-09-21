update eg_wf_matrix set validactions='Approve,Reject,Provide more info' where objecttype='CouncilPreamble' 
and currentstatus='CREATED' and currentdesignation='Commissioner';