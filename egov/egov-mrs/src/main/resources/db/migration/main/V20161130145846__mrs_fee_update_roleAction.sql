
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Update Fee', '/masters/fee/update', NULL, (SELECT id FROM eg_module WHERE name = 'Marriage Fee'), 1, 'Update Fee', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Update Fee'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Update Fee'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Fee') ,(select id FROM eg_feature WHERE name = 'Update Fee'));
