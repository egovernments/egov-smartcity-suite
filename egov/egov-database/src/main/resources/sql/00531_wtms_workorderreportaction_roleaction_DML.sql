INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot,application) VALUES (nextval('seq_eg_action'), 'workorderreportview', '/application/workorder/view',null,
(SELECT id FROM eg_module WHERE name='WaterTaxTransactions'),  'workorderreportview', 'false', 'wtms',
(select id from eg_module where name='Water Tax Management'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, displayname, 
enabled, contextroot,application) VALUES (nextval('seq_eg_action'), 'workorderreport', '/application/workorder',null,
(SELECT id FROM eg_module WHERE name='WaterTaxTransactions'),  'workorderreport', 'false', 'wtms',
(select id from eg_module where name='Water Tax Management'));


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreport' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreportview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreportview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreportview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreport' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));

