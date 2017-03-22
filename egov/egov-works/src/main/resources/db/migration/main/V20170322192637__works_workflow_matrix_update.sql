update eg_wf_matrix set nextstatus = 'APPROVED' where pendingactions ='Pending Technical Sanction' and objecttype='AbstractEstimate';
update eg_wf_matrix set currentdesignation  = '' where objecttype='AbstractEstimate';

--rollback update eg_wf_matrix set nextstatus = '' where pendingactions ='Pending Technical Sanction' and objecttype='AbstractEstimate';