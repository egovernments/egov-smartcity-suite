update eg_wf_matrix set validactions='Approve,Reject' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentstate='Assistant Engineer Approved' and currentdesignation='Deputy Executive Engineer';

update eg_wf_matrix set validactions='Forward,Cancel' where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION' and currentstate='Estimation Notice Generated' and currentdesignation='Senior Assistant,Junior Assistant';

