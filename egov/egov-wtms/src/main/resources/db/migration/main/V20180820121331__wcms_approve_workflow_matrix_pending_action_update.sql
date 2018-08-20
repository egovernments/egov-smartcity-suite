
update eg_wf_matrix set pendingactions ='Commissioner Approval Pending' where objecttype ='WaterConnectionDetails' and currentstate='Application Approval Pending' and additionalrule='NEWCONNECTION' and currentdesignation='Commissioner' and pendingactions is null;

update eg_wf_matrix set pendingactions ='Commissioner Approval Pending' where objecttype ='WaterConnectionDetails' and currentstate='Application Approval Pending' and additionalrule='ADDNLCONNECTION' and currentdesignation='Commissioner' and pendingactions is null;

update eg_wf_matrix set pendingactions ='Commissioner Approval Pending' where objecttype ='WaterConnectionDetails' and currentstate='Application Approval Pending' and additionalrule='CHANGEOFUSE' and currentdesignation='Commissioner' and pendingactions is null;
