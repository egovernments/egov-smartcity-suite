----- Marriage Registration status ------------------------------------

CREATE SEQUENCE SEQ_EGMRS_REGISTRATIONNO; 

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Created',now(),'CREATED',1);
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Approved',now(),'APPROVED',2);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Registered',now(),'REGISTERED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Certificate Re_Issued',now(),'CERTIFICATEREISSUED',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Rejected',now(),'REJECTED',5);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'Marriage Registration','Cancelled',now(),'CANCELLED',6);

------------------ wf_matrix -------------------------

update eg_wf_matrix set objecttype='MarriageRegistration'  where objecttype='Registration';

update eg_wf_types set type='MarriageRegistration', displayname='Marriage Registration' 
where type='Registration' and module=(SELECT id FROM eg_module WHERE name='Marriage Registration');

update eg_wf_matrix set validactions='Forward,Print Rejection Certificate,Cancel Registration' where objecttype='MarriageRegistration' and  validactions='Forward,Print Rejection Certificate, Close Registration' and additionalrule='MARRIAGE REGISTRATION';

update eg_wf_matrix set nextaction='Certificate Print Pending' where nextstate='Assistant Engineer Approved' and nextaction='Fee Collection Pending' and nextdesignation='Revenue Clerk' and nextstatus='Assistant Engineer Approved' and objecttype='MarriageRegistration' and additionalrule='MARRIAGE REGISTRATION';

Delete from eg_wf_matrix where objecttype='MarriageRegistration' and currentstate='Assistant Engineer Approved' and additionalrule='MARRIAGE REGISTRATION' and nextstate='Fee Collected' and nextaction='Certificate Print Pending';

update eg_wf_matrix set pendingactions='Certificate Print Pending', currentstate='Assistant Engineer Approved' where currentstate='Fee Collected' and pendingactions is null and nextstate='END' and objecttype='MarriageRegistration' and additionalrule='MARRIAGE REGISTRATION';

