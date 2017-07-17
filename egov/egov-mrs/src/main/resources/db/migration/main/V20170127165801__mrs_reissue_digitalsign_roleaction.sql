--------------- eg_roleaction -----------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'View ReIssue Certificate', '/reissue/viewCertificate/', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 8, 'View ReIssue Certificate', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'View ReIssue Certificate'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'ReIssue Workflow Transition for DigiSign', '/reissue/digiSignWorkflow', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 9, 'ReIssue Workflow Transition for DigiSign', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'ReIssue Workflow Transition for DigiSign'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Download Digitally Signed ReIssue Certificate', '/reissue/downloadSignedCertificate', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 10, 'Download Digitally Signed ReIssue Certificate', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'Download Digitally Signed ReIssue Certificate'));

