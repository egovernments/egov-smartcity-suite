-------------workflow matrix with digi sign-----------------
Delete from eg_wf_matrix where objecttype='Registration' and currentstate='Application Approved' and 
pendingactions='Digital Signature Pending' and additionalrule='MARRIAGE REGISTRATION' and nextstate='Digital Signed'
and nextaction='Certificate Print Pending' and nextstatus='DIGITALSIGNED';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, 
additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) 
VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'Application Approved', NULL, 'Digital Signature Pending', 'Commissioner',
'MARRIAGE REGISTRATION','Digital Signed', 'Certificate Print Pending', 'Revenue Clerk', 'DIGITALSIGNED', 'Sign,Preview',
NULL, NULL, '2016-04-01', '2099-04-01');

update eg_wf_matrix set nextaction = 'Digital Signature Pending', nextdesignation='Commissioner', pendingactions='Commisioner Approval Pending_DigiSign'
where nextaction='Certificate Print Pending' and nextdesignation='Revenue Clerk' and currentdesignation='Assistant Engineer'
and currentstate='Revenue Clerk Approved' and objecttype='MarriageRegistration' and additionalrule='MARRIAGE REGISTRATION';

update eg_wf_matrix set currentstate='Digital Signed' 
where currentstate='Application Approved' and nextaction='END' AND nextstate='END' and currentdesignation='Commissioner'
and objecttype='MarriageRegistration' and additionalrule='MARRIAGE REGISTRATION';

