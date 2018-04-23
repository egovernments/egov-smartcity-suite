
--update closure workflow matrix
Update eg_wf_matrix set forwardenabled =false where objecttype  ='TradeLicense' and additionalrule='EXTERNALCLOSUREAPPLICATION';

update eg_wf_matrix set forwardenabled =true where objecttype  ='TradeLicense' and additionalrule='CLOSURELICENSE' and 
currentstate in('Sanitory Inspector Approved','Sanitary Supervisor Verified','Assistant Medical Officer of Health Approved',
'Municipal Health Officer Approved');

--Update closure feature name
UPDATE EG_FEATURE SET name ='Create Closure Application' where name ='Closure';

UPDATE EG_FEATURE SET name ='Closure Application Approval' where name ='License Closure Approval';