---------------------START---------------------------
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'OfficialsProfileEdit'));
---------------------END-----------------------------
