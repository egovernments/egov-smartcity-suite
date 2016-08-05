---Update ValidActions for NEW status
update eg_wf_matrix set validactions = 'CreateAndApprove,Save,Forward,Cancel' where objecttype='MBHeader' and currentstate = 'NEW';

--rollback update eg_wf_matrix set validactions = 'Save,Forward,Cancel' where objecttype='MBHeader' and currentstate = 'NEW';