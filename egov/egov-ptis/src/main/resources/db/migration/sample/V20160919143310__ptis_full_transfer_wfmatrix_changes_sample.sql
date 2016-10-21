update eg_wf_matrix set validactions='Reject' where objecttype='PropertyMutation' and additionalrule='FULL TRANSFER' and nextstate='Transfer Fee Collected' and currentstate='Assistant Approved';
update eg_wf_matrix set validactions='Reject' where objecttype='PropertyMutation' and additionalrule='FULL TRANSFER' and nextstate='Registration Completed' and currentstate='Transfer Fee Collected';
update eg_wf_matrix set validactions='Approve,Reject' where objecttype='PropertyMutation' and additionalrule='FULL TRANSFER' and nextstate='Commissioner Approved' and currentstate='Registration Completed';
