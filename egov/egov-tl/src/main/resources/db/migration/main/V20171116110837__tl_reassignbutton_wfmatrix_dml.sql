--OLD WORKFLOW 
update eg_wf_matrix set validactions  ='Forward,Reject,Reassign' where currentstate='License Created' and additionalrule='NEWTRADELICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Reject,Reassign' where currentstate='First level fee collected' and additionalrule='NEWTRADELICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Reject,Reassign' where currentstate='License Created' and additionalrule='RENEWTRADELICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Reject,Reassign' where currentstate='First level fee collected' and additionalrule='RENEWTRADELICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Reassign' where currentstate='NEW' and additionalrule='CLOSURELICENSE' and objecttype='TradeLicense';

--NEW WORKFLOW
update eg_wf_matrix set validactions  ='Cancel,Reassign' where currentstate='License Created' and additionalrule='NEWLICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Cancel,Generate Provisional Certificate,Reassign' where currentstate='First Level Fee Collected' and additionalrule='NEWLICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Cancel,Reassign' where currentstate='Renewal Initiated' and additionalrule='RENEWLICENSE' and objecttype='TradeLicense';
update eg_wf_matrix set validactions  ='Forward,Cancel,Generate Provisional Certificate,Reassign' where currentstate='First Level Fee Collected' and additionalrule='NEWLICENSE' and objecttype='TradeLicense';


