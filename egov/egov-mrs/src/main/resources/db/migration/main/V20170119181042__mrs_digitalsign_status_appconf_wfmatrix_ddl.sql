---------- egw_status -----------
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Digital Signature Done',now(),'DIGITALSIGNED',7);

---------- eg_appconfig ----------- 
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'DIGITALSIGN_IN_WORKFLOW', 'Digital Signature in MR',0, (select id from eg_module where name='Marriage Registration')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DIGITALSIGN_IN_WORKFLOW' and module= (select id from eg_module where name='Marriage Registration')), current_date, 'NO',0);

------------- workflow matrix with digital sign ----------------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'Registration', 'Application Approved', NULL, 'Digital Signature Pending', 'Commissioner',
'MARRIAGE REGISTRATION','Digital Signed', 'Certificate Print Pending', 'Revenue Clerk', 'DIGITALSIGNED', 'Sign,Preview',
NULL, NULL, '2016-04-01', '2099-04-01');

update eg_wf_matrix set nextaction = 'Digital Signature Pending', nextdesignation='Commissioner', pendingactions='Commisioner Approval Pending_DigiSign'
where nextaction='Certificate Print Pending' and nextdesignation='Revenue Clerk' and currentdesignation='Assistant Engineer'
and currentstate='Revenue Clerk Approved' and objecttype='Registration' and additionalrule='MARRIAGE REGISTRATION';

update eg_wf_matrix set currentstate='Digital Signed' 
where currentstate='Application Approved' and nextaction='END' AND nextstate='END' and currentdesignation='Commissioner'
and objecttype='Registration' and additionalrule='MARRIAGE REGISTRATION';

------------- workflow matrix without digi sign -------------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Revenue Clerk Approved', NULL, 'Commisioner Approval Pending_PrintCert', 'Assistant Engineer',
'MARRIAGE REGISTRATION','Application Approved', 'Certificate Print Pending', 'Revenue Clerk', 'APPROVED', 'Approve,Reject',
NULL, NULL, '2016-04-01', '2099-04-01');

 INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Application Approved', NULL, 'Certificate Print Pending', 'Commissioner',
 'MARRIAGE REGISTRATION','END', 'END', null, null, 'Print Certificate',
  NULL, NULL, '2016-04-01', '2099-04-01');
