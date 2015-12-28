
update eg_wf_matrix set validactions='Preview,Sign' where objecttype = 'WaterConnectionDetails' and 
currentdesignation='Commissioner' and currentState = 'ReConn Approved By Commissioner' and additionalrule='RECONNECTION';


update eg_wf_matrix set validactions='Preview,Sign' where objecttype = 'WaterConnectionDetails' and 
currentdesignation='Commissioner' and currentState = 'Closure Approved By Commissioner' and additionalrule='CLOSECONNECTION';
