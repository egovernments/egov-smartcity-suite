INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY',
 'WaterConnectionDetails', 'Created', NULL, NULL, NULL, 
 'NEW CONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 
'Clerk Approved Pending', 'Forward',
 NULL, NULL, '2015-08-01', '2099-04-01');


insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='WaterTaxCreateNewConnection'));


insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='connectiontypesajax'));

insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='WaterTaxCreateNewConnectionNewForm'));


insert into egeis_jurisdiction values(nextval('seq_egeis_jurisdiction'),
(select id from egeis_employee where id in(select id from eg_user where name='mahesh')),
(select id from EG_BOUNDARY_TYPE where name='Zone'),now(),now(),1,1,1,(select id from eg_boundary where boundarynum=1 and boundarytype in(select id from eg_boundary_type where name='Zone'))
);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'DEPARTMENTFORWORKFLOW', 
'Department for Workflow',
0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DEPARTMENTFORWORKFLOW')
 ,current_date, 'Revenue',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'ROLEFORNONEMPLOYEEINWATERTAX', 
'roles to create waterTax application ',
0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES ( 
nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ROLEFORNONEMPLOYEEINWATERTAX')
 ,current_date, 'CSC Operator',0);
