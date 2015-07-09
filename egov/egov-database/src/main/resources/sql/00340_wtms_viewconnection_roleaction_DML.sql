INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='View Water Connection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='View Water Connection' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));
