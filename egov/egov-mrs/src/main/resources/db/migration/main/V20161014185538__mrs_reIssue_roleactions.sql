INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'calculateMarriageFee', '/registration/calculatemarriagefee', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 2, 'calculate Marriage Fee', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'calculateMarriageFee'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'calculateMarriageFee'));

-------------

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'Re Issue Marriage Certifiate', '/registration/reissuecertificate', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 7, 'Re Issue Marriage Certifiate', true, 'mrs', 0, 1, now(), 1, now(),
 (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Re Issue Marriage Certifiate'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'Re Issue Marriage Certifiate'));

----------------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Search register status MR records', '/registration/searchregisteredrecord', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 8, 'Search register status records', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Search register status MR records'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'Search register status MR records'));

---------------------------------

alter table egmrs_registration alter column feeCriteria set not null;
update eg_action set enabled =false where name='CreateReIssue' and contextroot='mrs';