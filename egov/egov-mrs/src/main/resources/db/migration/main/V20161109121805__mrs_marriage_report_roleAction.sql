INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'RegistrationdatewiseReport', '/report/datewiseregistration', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 5, 'Date wise Report', true, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationdatewiseReport'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'RegistrationdatewiseReport'));




INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'RegistrationmonthwiseReport', '/report/monthwiseregistration', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 6, 'Monthly Report', true, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationmonthwiseReport'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'RegistrationmonthwiseReport'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'RegistrationreligionwiseReport', '/report/religionwiseregistration', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 7, 'Religion Wise Report', true, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationreligionwiseReport'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'RegistrationreligionwiseReport'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'RegistrationactwiseReport', '/report/actwiseregistration', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 8, 'Act Wise Report', true, 'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'RegistrationactwiseReport'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'RegistrationactwiseReport'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES  (nextval('seq_eg_action'), 'Act Wise Report View Details', '/report/act-wise/view/', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 9, 'Act Wise Report View Details', false, 'mrs', 0, 1, now(),  1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Act Wise Report View Details'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Act Wise Report View Details'));


UPDATE eg_action SET enabled= false WHERE name= 'Status at the time Marriage Report View Details';