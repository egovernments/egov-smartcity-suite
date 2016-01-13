INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'HoardingSuccess'));

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','CANCELLED',now(),'CANCELLED',1);

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','REJECTED',now(),'REJECTED',1);

update egw_status set description='Advertisement Tax Collection Pending',code='ADTAXAMTPAYMENTPENDING' where code='ADTAXAMOUNTPAID';

delete from eg_wf_matrix where objecttype='AdvertisementPermitDetail';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'NEW', NULL, NULL, 'Assistant engineer', 'CREATEADVERTISEMENT', 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Created', 'CREATED', 'Commissioner approval pending', 'Commissioner', 'CREATEADVERTISEMENT', 'Commissioner Approved', 'Collection pending', 'Assistant engineer', 'APPROVED', 'Approve,Reject', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Commissioner Approved', 'APPROVED', 'Collection pending', 'Assistant engineer', 'CREATEADVERTISEMENT', 'Amount Collection done', 'Print permit order pending', 'Assistant engineer', 'ADTAXAMTPAYMENTPAID', 'Close', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Amount Collection done', 'ADTAXAMTPAYMENTPAID', 'Print permit order pending', 'Assistant engineer', 'CREATEADVERTISEMENT', 'END', 'END', null, 'ADTAXPERMITGENERATED', 'GENERATE PERMIT ORDER', NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'Advertisement', 'Rejected', 'REJECTED', NULL, 'Assistant engineer', 'CREATEADVERTISEMENT', 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward,Reject', NULL, NULL, '2015-08-01', '2099-04-01');

