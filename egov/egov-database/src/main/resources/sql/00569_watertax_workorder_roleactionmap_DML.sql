INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='workorderreport' and contextroot='wtms'),
(SELECT id FROM eg_role where name='ULB Operator'));
