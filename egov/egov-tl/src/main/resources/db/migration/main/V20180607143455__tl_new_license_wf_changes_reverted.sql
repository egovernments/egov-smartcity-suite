delete from eg_feature_action  WHERE  feature  in(select id from eg_feature  where name in ('Create License Application','New License Application Approval'));

delete from eg_feature_role  where feature in(select id from eg_feature  where name in ('Create License Application','New License Application Approval'));

delete from eg_feature_action  WHERE  feature  in(select id from eg_feature  where name='Print Certificate') and action in(select id from eg_action where name in('License Final Certificate','License Provisional Certificate'));

delete from eg_feature_action  WHERE  feature  in(select id from eg_feature  where name='Collect Fees') and action in(select id from eg_action where name in('License Fee Collection','License Fee Verification'));

delete from eg_feature  where name in ('Create License Application','New License Application Approval');

delete from eg_roleaction where actionid  in (select id from eg_action where name in('Create New License Application',
'Show License Application for Approver','Forward New License Application','Approve New License Application',
'Reject New License Application','Cancel New License Application','License Application Certificate Preview',
'Digitaly Sign New License Application','License Application Success Acknowledgment','License Application Provisional Certificate',
'License Fee Verification','License Fee Collection','License Final Certificate','License Provisional Certificate',
'Print License Acknowledgement') and contextroot='tl');

delete from eg_action where name in('Create New License Application','Show License Application for Approver',
'Forward New License Application','Approve New License Application','Reject New License Application','Cancel New License Application',
'License Application Certificate Preview','Digitaly Sign New License Application','License Application Success Acknowledgment',
'License Application Provisional Certificate','License Fee Verification','License Fee Collection','License Final Certificate',
'License Provisional Certificate','Print License Acknowledgement') and contextroot='tl';

delete from EGP_PORTALSERVICE where name='Apply for New License' and module in(select id from eg_module where name='Trade License');

update egp_portalservice set isactive =true where code ='Create New License' and module in(select id from eg_module where name='Trade License');

update eg_action set enabled =true where name='Create New License' and contextroot='tl';

update eg_feature  set enabled =true where name in('Create New License','License Approval') and module=(select id from eg_module where name='Trade License');

update eg_wf_matrix set currentstate='New' where objecttype='TradeLicense' and additionalrule ='CSCOPERATORNEWLICENSE' and department  ='PUBLIC HEALTH AND SANITATION' ;

update eg_wf_matrix set currentstate='New' where objecttype='TradeLicense' and additionalrule ='CSCOPERATORRENEWLICENSE' and department  ='PUBLIC HEALTH AND SANITATION' ;



