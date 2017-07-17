INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'View-Fee', '/masters/fee/view', NULL, (SELECT id FROM eg_module WHERE name = 'Marriage Fee'), 3, 'View-Fee', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'View-Fee'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'View-Fee'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'View-Act', '/masters/act/view', NULL, (SELECT id FROM eg_module WHERE name = 'Marriage Act'), 3, 'View-Act', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'View-Act'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'View-Act'));
