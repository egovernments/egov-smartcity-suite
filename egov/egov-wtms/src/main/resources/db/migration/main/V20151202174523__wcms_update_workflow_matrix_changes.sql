update eg_wf_matrix set nextstatus='Acknowledgement Print Pending',nextaction='Acknowledgement Print Pending' where objecttype='WaterConnectionDetails' and additionalrule='RECONNECTION' 
and currentstate='ReConn Approved By Commissioner';

update eg_wf_matrix set nextstatus='Acknowledgement Pending',nextaction='Acknowledgement Pending' where objecttype='WaterConnectionDetails' and additionalrule='CLOSECONNECTION' 
and currentstate='Closure Approved By Commissioner';