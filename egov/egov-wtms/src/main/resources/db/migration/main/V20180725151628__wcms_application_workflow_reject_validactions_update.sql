update eg_wf_matrix set validactions ='Forward,Cancel' where objecttype ='WaterConnectionDetails' and currentstate='Estimate Notice Generated' and validactions ='Forward,Reject';

