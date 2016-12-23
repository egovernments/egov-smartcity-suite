
 ------------------------ageing report role action mapping--------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Marriage Registration Ageing Report', '/report/ageing-report', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 10, 'Ageing Report', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Marriage Registration Ageing Report'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Marriage Registration Ageing Report'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Ageing Report View Details', '/report/ageing-report/view/', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Reports'), 3, 'Ageing Report View Details', false, 'mrs', 0, 1,now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Ageing Report View Details'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Ageing Report View Details'));

 ------------------------ageing report feature action mapping--------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Marriage Registration Ageing Report','Marriage Registration Ageing Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Marriage Registration Ageing Report') ,(select id FROM eg_feature WHERE name = 'Marriage Registration Ageing Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ageing Report View Details') ,(select id FROM eg_feature WHERE name = 'Marriage Registration Ageing Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Marriage Registration Ageing Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'Marriage Registration Ageing Report'));

