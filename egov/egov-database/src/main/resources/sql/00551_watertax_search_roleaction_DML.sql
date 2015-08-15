INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'ElasiticapplicationSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'watertaxappsearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCollectionView'));

--rollback DELETE FROM eg_roleaction  where actionid = (select id FROM eg_action where name='ElasiticapplicationSearch');
--rollback DELETE FROM eg_roleaction  where actionid = (select id FROM eg_action where name='watertaxappsearch');
--rollback DELETE FROM eg_roleaction  where actionid = (select id FROM eg_action where name='WaterTaxCollectionView');
