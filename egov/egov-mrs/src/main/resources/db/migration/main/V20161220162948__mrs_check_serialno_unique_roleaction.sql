INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'Validate Serial No is Unique', '/registration/checkunique-serialno', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 2, 'Validate Serial No is Unique', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Validate Serial No is Unique'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Validate Serial No is Unique'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Validate Serial No is Unique') ,(select id FROM eg_feature WHERE name = 'Create data entry'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Validate Serial No is Unique') ,(select id FROM eg_feature WHERE name = 'Update Marriage Registration'));