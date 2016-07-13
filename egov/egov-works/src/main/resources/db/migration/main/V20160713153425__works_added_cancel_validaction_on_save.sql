update eg_wf_matrix set validactions = 'Save,Forward,Cancel' where objecttype= 'AbstractEstimate' and currentstate = 'NEW';

update eg_wf_matrix set validactions = 'Save,Forward,Cancel' where objecttype= 'MBHeader' and currentstate = 'NEW';


--rollback update eg_wf_matrix set validactions = 'Save,Forward' where objecttype= 'AbstractEstimate' and currentstate = 'NEW';

--rollback update eg_wf_matrix set validactions = 'Save,Forward' where objecttype= 'MBHeader' and currentstate = 'NEW';

