update eg_wf_matrix set validactions='Forward,Cancel' where objecttype='WaterConnectionDetails' and validactions='Forward,Reject' and currentstate='Rejected';
