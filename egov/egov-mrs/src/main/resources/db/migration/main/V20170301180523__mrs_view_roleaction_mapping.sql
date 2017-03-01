INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'Common View For New Marriage Registration Application', '/registration/viewapplication', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 10, 'Common View For New Marriage Registration Application', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Common View For New Marriage Registration Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Common View For New Marriage Registration Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'Common View For New Marriage Registration Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'Common View For New Marriage Registration Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Common View For New Marriage Registration Application'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Common View For New Marriage Registration Application') ,(select id FROM eg_feature WHERE name = 'Search Marriage Registration'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),
 'Common View For Reissue Marriage Certificate Application', '/reissue/viewapplication', NULL, 
(SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 12, 'Common View For Reissue Marriage Certificate Application', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Common View For Reissue Marriage Certificate Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'Common View For Reissue Marriage Certificate Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'Common View For Reissue Marriage Certificate Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'Common View For Reissue Marriage Certificate Application'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'CSC Operator'), (SELECT id FROM eg_action WHERE name = 'Common View For Reissue Marriage Certificate Application'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Common View For Reissue Marriage Certificate Application') ,(select id FROM eg_feature WHERE name = 'View Reissue'));
