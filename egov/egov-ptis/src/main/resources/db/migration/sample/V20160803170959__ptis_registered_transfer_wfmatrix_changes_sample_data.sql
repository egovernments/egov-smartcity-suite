---------------------------------Workflow matrix changes for Registered Transfer---------------------------------------

update eg_wf_matrix set nextaction= 'Revenue Officer Approval Pending' where currentstate= 'Bill Collector Approved' and additionalrule = 'REGISTERED TRANSFER' and objecttype = 'PropertyMutation';

update eg_wf_matrix set currentstate= 'UD Revenue Inspector Approved' where nextaction= 'Commissioner Approval Pending' and additionalrule = 'REGISTERED TRANSFER' and objecttype = 'PropertyMutation';

update eg_wf_matrix set nextstate= 'Bill Collector Approved' where nextstate= 'Transfer Fee Collected' and currentstate='UD Revenue Inspector Approved' and additionalrule = 'REGISTERED TRANSFER' and objecttype = 'PropertyMutation' and validactions= 'Reject';
