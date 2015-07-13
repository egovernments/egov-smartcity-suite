INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'WaterTaxCollectionView', '/application/generatebill', null, (select id from eg_module where name='WaterTaxApplication'), null, 'WaterTaxCollectionView', false, 'wtms', 0, 1, now(), 1, now());


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxCollectionView'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCollectionView'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='WaterTaxCollectionView' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));


--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator','Water Tax Approver')) and actionid in (select id FROM eg_action  WHERE name='WaterTaxCollectionView' and contextroot='wtms');
--rollback delete from eg_action where name='WaterTaxCollectionView' and contextroot='wtms';

