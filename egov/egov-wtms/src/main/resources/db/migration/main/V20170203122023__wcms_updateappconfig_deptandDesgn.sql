update  eg_appconfig_values set value='Junior Assistant,Senior Assistant' where key_id in(select id from eg_appconfig where key_name='CLERKDESIGNATIONFORCSCOPERATOR');


update  eg_appconfig_values set value='Engineering' where key_id
 in(select id from eg_appconfig where key_name='DEPARTMENTFORWORKFLOW');
 
  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, null, 
 'CLOSECONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Senior Assistant,Junior Assistant', 
 'Clerk Approved Pending', 'Forward', NULL, NULL, now(), '2099-04-01');
 
  INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, 
pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation,
 nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('SEQ_EG_WF_MATRIX'), 
 'ANY', 'WaterConnectionDetails', 'Created', NULL, NULL, null, 
 'RECONNECTION', 'NEW', 'Revenue Clerk approval pending', 'Senior Assistant,Junior Assistant', 
 'Clerk Approved Pending', 'Forward', NULL, NULL, now(), '2099-04-01');

 
  insert into eg_roleaction(roleid,actionid)values((select id from eg_role where name='CSC Operator'),
 (select id from eg_action where name like '%createClosureConnection%' and queryparams='applicationType=CLOSURECONNECTION'));
 
 
 insert into eg_roleaction(roleid,actionid)values((select id from eg_role where name='CSC Operator'),
 (select id from eg_action where name like '%createReConnection%' and queryparams='applicationType=RECONNECTION'));
