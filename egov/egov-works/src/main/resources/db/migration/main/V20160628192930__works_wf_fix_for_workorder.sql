update eg_wf_matrix set nextstate  = 'Created' where objecttype = 'WorkOrder' and currentstate= 'NEW';

update eg_wf_matrix set nextstate  = 'Created' where objecttype = 'WorkOrder' and currentstate= 'Rejected';

update eg_wf_matrix set currentstate  = 'Created' where objecttype = 'WorkOrder' and currentstate= 'Approve';
