--------------Update script for Line Estimate WF Matrix-------------
update eg_wf_matrix set nextstatus = 'Administrative_sanctioned' where objecttype = 'LineEstimate' and pendingactions = 'Pending Admin Sanction';

--rollback update eg_wf_matrix set nextstatus = NULL where objecttype = 'LineEstimate' and pendingactions = 'Pending Admin Sanction';