INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'Edit Demand Update Form', '/edit/editDemand-update', null,(select id from eg_module where name='Existing property'), 1, 'Edit Demand Update Form', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Edit Demand Update Form' and contextroot='ptis'),
(SELECT id FROM eg_role WHERE name='CSC Operator'));