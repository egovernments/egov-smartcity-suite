INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Collect Fee', '/registration/collectmrfee/', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 7, 'Collect Fee', true, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'Collect Fee'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Collection Operator'), (SELECT id FROM eg_action WHERE name = 'Collect Fee'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Collection Operator'),(SELECT id FROM eg_action WHERE name ='MarriageFeeBill'));

update eg_action set url = '/registration/searchApproved' where name='Search And Show Marriage Registration Results';
