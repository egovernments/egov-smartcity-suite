INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Edit Demand Form' and contextroot='ptis'),
(SELECT id FROM eg_role WHERE name='CSC Operator'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Edit Demand submit', '/edit/editDemand.action', null,(select id from eg_module where name='Existing property'), 1, 'Edit Demand submit', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Edit Demand submit' and contextroot='ptis'),
(SELECT id FROM eg_role WHERE name='CSC Operator'));
