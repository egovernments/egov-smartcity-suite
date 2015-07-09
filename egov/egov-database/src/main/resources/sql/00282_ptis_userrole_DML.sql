UPDATE eg_userrole SET roleid=(SELECT id FROM eg_role WHERE name='CSC Operator') WHERE roleid=(SELECT id FROM eg_role WHERE name='Data Entry Operator');
DELETE FROM eg_roleaction where roleid in (SELECT id FROM eg_role WHERE name='Data Entry Operator');
DELETE FROM eg_role WHERE name='Data Entry Operator';
--UPDATE eg_userrole SET roleid=(SELECT id FROM eg_role WHERE name='Data Entry Operator') WHERE roleid=(SELECT id FROM eg_role WHERE name='CSC Operator');