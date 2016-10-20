INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Registration Unit', true, 'mrs', (select id from eg_module where name='MR-Masters'), 'Registration Unit', 4);


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Create Registration unit', '/masters/mrregistrationunit/new', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 1, 'Create Registration unit', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Create Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Create Registration unit'));



INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Create-Registration unit', '/masters/mrregistrationunit/create', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 1, 'Create-Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Create-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Create-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Result-Registration unit', '/masters/mrregistrationunit/result', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 2, 'Result-Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Result-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Result-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search and View-Registration unit', '/masters/mrregistrationunit/search/view', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 2, 'View Registration unit', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search and View-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Search and View-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'View-Registration unit', '/masters/mrregistrationunit/view', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 2, 'View-Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'View-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'View-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search and View Result Registration unit', '/masters/mrregistrationunit/ajaxsearch/view', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 2, 'Search and View Result Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search and View Result Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Search and View Result Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search and Edit-Registration unit', '/masters/mrregistrationunit/search/edit', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 3, 'Update Registration unit', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search and Edit-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Search and Edit-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search and Edit Result Registration unit', '/masters/mrregistrationunit/ajaxsearch/edit', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 3, 'Search and Edit Result Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search and Edit Result Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Search and Edit Result Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Edit-Registration unit', '/masters/mrregistrationunit/edit', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 2, 'Edit-Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Edit-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Edit-Registration unit'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'update-Registration unit', '/masters/mrregistrationunit/update', NULL, (SELECT id FROM eg_module WHERE name = 'Registration Unit'), 3, 'update-Registration unit', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'update-Registration unit'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'update-Registration unit'));


alter table egmrs_registration add column RegistrationUnit bigint ;

alter table egmrs_registration add CONSTRAINT fk_reg_registrationunit FOREIGN KEY (RegistrationUnit) REFERENCES egmrs_registrationunit (id);