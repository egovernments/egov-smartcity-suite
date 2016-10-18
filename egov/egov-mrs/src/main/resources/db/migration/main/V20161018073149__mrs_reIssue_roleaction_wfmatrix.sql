--------------------------- eg_action ----------------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'generateReIssueCertificate', '/certificate/reissue', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 9, 'Generate ReIssue Certificate', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'editMrgCertificateReIssue', '/reissue/workflow', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 10, 'Edit Marriage Certificate ReIssue', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

--------------------------- EG_ROLEACTION ----------------------------
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'generateReIssueCertificate'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'editMrgCertificateReIssue'));

--------------------------- eg_wf_matrix  ----------------------------
update eg_wf_matrix set validactions='Forward,Cancel ReIssue' where objecttype='ReIssue' and  validactions='Forward,Print Rejection Certificate,Cancel ReIssue' and additionalrule='MARRIAGE REGISTRATION';


