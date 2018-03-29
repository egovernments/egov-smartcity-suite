ALTER TABLE egmrs_registration  ALTER COLUMN zone DROP NOT NULL;
ALTER TABLE egmrs_applicant ALTER COLUMN officeaddress DROP NOT NULL;
ALTER TABLE egmrs_witness ALTER COLUMN firstname DROP NOT NULL;
ALTER TABLE egmrs_witness ALTER COLUMN age DROP NOT NULL;
ALTER TABLE egmrs_witness ALTER COLUMN residenceaddress DROP NOT NULL;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'MarriageRegistration', 'MarriageAPI NEW', NULL, NULL, NULL, 'MARRIAGE REGISTRATION', 'Revenue Clerk Approved', 'Approver Approval Pending', 'Commissioner', 'CREATED','Forward', NULL, NULL, '2016-04-01', '2099-04-01');