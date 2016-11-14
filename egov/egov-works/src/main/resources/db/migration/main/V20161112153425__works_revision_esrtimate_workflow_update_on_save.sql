update eg_wf_matrix set validactions = 'Save,Forward,Cancel' where objecttype ='RevisionAbstractEstimate' and pendingactions ='Pending Submission';
--rollback update eg_wf_matrix set validactions = 'Save,Forward' where objecttype ='RevisionAbstractEstimate' and pendingactions ='Pending Submission';
