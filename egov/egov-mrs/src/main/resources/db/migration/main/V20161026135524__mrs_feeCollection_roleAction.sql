---------------- eg_action --------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search approved ReIssue', '/registration/collectmrreissuefeeajaxsearch', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 6, 'Search approved ReIssue', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'MarriageReIssueFeeBill', '/collection/reissuebill', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 5, 'Marriage ReIssue Certificate Fee Bill', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

---------------- EG_ROLEACTION --------------------
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search approved ReIssue'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Collection Operator'),(SELECT id FROM eg_action WHERE name ='Search approved ReIssue'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'MarriageReIssueFeeBill'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Collection Operator'), (SELECT id FROM eg_action WHERE name = 'MarriageReIssueFeeBill'));
