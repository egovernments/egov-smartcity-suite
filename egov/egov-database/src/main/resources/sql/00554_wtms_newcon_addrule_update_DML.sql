UPDATE eg_wf_matrix SET additionalrule='NEWCONNECTION' WHERE objecttype ='WaterConnectionDetails' AND additionalrule='NEW CONNECTION';

UPDATE eg_wf_matrix SET validactions='Generate WorkOrder' WHERE objecttype ='WaterConnectionDetails' AND currentstate='Commissioner Approved' 
AND additionalrule='NEWCONNECTION';
