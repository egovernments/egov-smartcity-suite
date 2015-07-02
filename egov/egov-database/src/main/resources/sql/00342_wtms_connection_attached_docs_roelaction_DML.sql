INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='File Download' and contextroot='egi'),
(SELECT id FROM eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='File Download' and contextroot='egi'),
(SELECT id FROM eg_role where name='Water Tax Approver'));
