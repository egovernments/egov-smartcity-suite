update eg_wf_matrix set validactions='Approve' where objecttype='PropertyMutation' and additionalrule='FULL TRANSFER' and nextstate='Commissioner Approved' and currentstate='Registration Completed';
