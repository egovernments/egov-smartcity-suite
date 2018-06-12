
update eg_wf_matrix set forwardenabled =false where objecttype  ='TradeLicense' and additionalrule='CLOSURELICENSE' and
currentstate in('Sanitory Inspector Approved','Sanitary Supervisor Verified','Assistant Medical Officer of Health Approved',
'Municipal Health Officer Approved') and currentdesignation='Commissioner';