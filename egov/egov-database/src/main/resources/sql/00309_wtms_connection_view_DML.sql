INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'View Water Connection', '/application/view/', 
null,(SELECT id FROM eg_module WHERE name='WaterTaxApplication'),  'View Water Connection', 'false', 'wtms');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='View Water Connection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));
