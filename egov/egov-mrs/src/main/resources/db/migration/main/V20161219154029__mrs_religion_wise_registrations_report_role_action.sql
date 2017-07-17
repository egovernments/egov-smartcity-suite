
----------------------role action mapping-------------------------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Religion Wise Registrations Report', '/report/religion-wise-registrations-report', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 11, 'Religion Wise Registrations Report', true, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Religion Wise Registrations Report'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Religion Wise Registrations Report'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Print Religion Wise Registrations Report', '/report/print-religion-wise-details', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 11, 'Print Religion Wise Registrations Report', false, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Print Religion Wise Registrations Report'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Print Religion Wise Registrations Report'));

----------------------------feature action-----------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Religion Wise Registrations Report','Religion Wise Registrations Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Religion Wise Registrations Report') ,(select id FROM eg_feature WHERE name = 'Religion Wise Registrations Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Print Religion Wise Registrations Report') ,(select id FROM eg_feature WHERE name = 'Religion Wise Registrations Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Religion Wise Registrations Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'Religion Wise Registrations Report'));