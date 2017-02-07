INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'PUBLIC HEALTH AND SANITATION', 'TradeLicense','NEW', NULL, NULL, 
'CSC Operator', 'NEWTRADELICENSE', 'License Created',
 'First level Collection pending', 'Revenue Clerk,Junior Assistant', 'Clerk Initiated',
 'Forward', NULL, NULL, '2015-04-01', '2099-04-01',0);

INSERT INTO eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,
currentdesignation,additionalrule,nextstate,nextaction,
nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version) VALUES 
(nextval('seq_eg_wf_matrix'), 'PUBLIC HEALTH AND SANITATION', 'TradeLicense','NEW', NULL, NULL, 
'CSC Operator', 'RENEWTRADELICENSE', 'License Created',
 'First level Collection pending', 'Revenue Clerk,Junior Assistant', 'Clerk Initiated',
 'Forward', NULL, NULL, '2015-04-01', '2099-04-01',0);

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='Create New License' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='newTradeLicense-create' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='SearchAjax-PopulateData' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='New Trade License Before Renew' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='NewTradeLicense-renewal' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));


