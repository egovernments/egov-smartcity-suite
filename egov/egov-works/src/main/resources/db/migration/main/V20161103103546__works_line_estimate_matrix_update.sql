update eg_wf_matrix set nextstate  = 'Admin sanctioned' where currentstate='Budget Sanctioned' and objecttype='LineEstimate';

--rollback update eg_wf_matrix set nextstate  = 'END' where currentstate='Budget Sanctioned' and objecttype='LineEstimate'