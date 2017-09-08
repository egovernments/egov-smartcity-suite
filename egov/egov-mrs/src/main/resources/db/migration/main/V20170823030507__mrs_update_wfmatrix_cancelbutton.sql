update eg_wf_matrix set validactions ='Forward,Cancel Registration' where objecttype ='MarriageRegistration' and currentstate='NEW' and validactions='Forward';

update eg_wf_matrix set validactions ='Forward,Cancel ReIssue' where objecttype ='ReIssue' and currentstate='NEW' and validactions='Forward';
