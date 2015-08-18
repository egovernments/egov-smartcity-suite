INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, enabled, contextroot,application) 
VALUES (nextval('seq_eg_action'), 'watertaxestimationnotice', '/application/estimationNotice', null, 
(SELECT id FROM eg_module WHERE name='WaterTaxTransactions'),  'watertaxestimationnotice', 'false', 'wtms',
(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnotice' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnotice' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, enabled, contextroot,application) 
VALUES (nextval('seq_eg_action'), 'watertaxestimationnoticeview', '/application/estimationNotice/view/',null,
(SELECT id FROM eg_module WHERE name='WaterTaxTransactions'),  'watertaxestimationnoticeview', 'false', 'wtms',
(select id from eg_module where name='Water Tax Management'));


INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnoticeview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnoticeview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnoticeview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));
INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='watertaxestimationnoticeview' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));

--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator','ULB Operator','Water Tax Approver')) and actionid in (select id FROM eg_action  WHERE name='watertaxestimationnoticeview' and contextroot='wtms');
--rollback delete from eg_action where name='watertaxestimationnoticeview' and contextroot='wtms';

--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','ULB Operator')) and actionid in (select id FROM eg_action  WHERE name='watertaxestimationnotice' and contextroot='wtms');
--rollback delete from eg_action where name='watertaxestimationnotice' and contextroot='wtms';
