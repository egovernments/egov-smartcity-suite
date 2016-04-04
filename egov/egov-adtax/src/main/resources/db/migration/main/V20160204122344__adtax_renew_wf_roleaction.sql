INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'advertisementrenewalcreate', '/advertisement/renewal/', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 4, 'Advertisement Renewal Create', false, 'adtax', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Approver') ,(select id FROM eg_action  WHERE name = 'advertisementrenewalcreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Advertisement Tax Creator') ,(select id FROM eg_action  WHERE name = 'advertisementrenewalcreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'advertisementrenewalcreate'));


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'NEW', NULL, NULL, 'Assistant engineer', 'RENEWADVERTISEMENT', 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Created', 'CREATED', 'Commissioner approval pending', 'Commissioner', 'RENEWADVERTISEMENT', 'Commissioner Approved', 'Collection pending', 'Assistant engineer', 'APPROVED', 'Approve,Reject', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Commissioner Approved', 'APPROVED', 'Collection pending', 'Assistant engineer', 'RENEWADVERTISEMENT', 'Amount Collection done', 'Print permit order pending', 'Assistant engineer', 'ADTAXAMTPAYMENTPAID', 'Close', NULL, NULL, '2015-08-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Amount Collection done', 'ADTAXAMTPAYMENTPAID', 'Print permit order pending', 'Assistant engineer', 'RENEWADVERTISEMENT', 'END', 'END', null, 'ADTAXPERMITGENERATED', 'GENERATE PERMIT ORDER', NULL, NULL, '2015-08-01', '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('eg_wf_matrix_seq'), 'ANY', 'AdvertisementPermitDetail', 'Rejected', 'REJECTED', NULL, 'Assistant engineer', 'RENEWADVERTISEMENT', 'Created', 'Commissioner approval pending', 'Commissioner', 'CREATED', 'Forward,Reject', NULL, NULL, '2015-08-01', '2099-04-01');

update eg_action set enabled =false where name='renewalsearch';
