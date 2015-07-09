INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'Addtional Water Connection', '/application/addconnection/', 
null,(SELECT id FROM eg_module WHERE name='WaterTaxApplication'),  'Addtional Water Connection', 'false', 'wtms');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Addtional Water Connection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'WaterTaxAddConnection', '/application/addConnection-create', 
null,(SELECT id FROM eg_module WHERE name='WaterTaxApplication'),  'WaterTaxAddConnection', 'false', 'wtms');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='WaterTaxAddConnection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Addtional Water Connection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));
