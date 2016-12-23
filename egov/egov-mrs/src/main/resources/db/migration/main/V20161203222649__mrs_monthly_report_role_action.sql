
-----------------------------------role action mapping----------------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Monthly Marriage Applications Count', '/report/monthly-applications-count', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 8, 'Monthly Marriage Applications Count', false, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Monthly Marriage Applications Count'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Monthly Marriage Applications Count'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Show Marriage Applications Details', '/report/show-applications-details', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 9, 'Show Marriage Applications Details', false, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Show Marriage Applications Details'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Show Marriage Applications Details'));

-----------------------------------feature action mapping----------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Monthly Marriage Applications Count') ,(select id FROM eg_feature WHERE name = 'month wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Show Marriage Applications Details') ,(select id FROM eg_feature WHERE name = 'month wise Report'));
