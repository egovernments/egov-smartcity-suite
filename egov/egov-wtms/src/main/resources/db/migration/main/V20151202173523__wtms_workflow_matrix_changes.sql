update eg_wf_matrix set  currentstate='Estimate Notice Generated' where objecttype='WaterConnectionDetails' and currentstate='Estmate Notice Generated';

update eg_wf_matrix set nextstatus='Acknowledgement Print pending',nextaction='Acknowledgement Print pending' where objecttype='WaterConnectionDetails' and additionalrule='RECONNECTION' 
and currentstate='ReConn Approved By Commissioner';

update eg_wf_matrix set nextstatus='Acknowledgemnt pending',nextaction='Acknowledgemnt pending' where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION' 
and currentstate='Closure Approved By Commissioner';