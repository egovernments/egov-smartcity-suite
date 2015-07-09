INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'UpdateWaterConnectionApplication', '/application/update/', 
null,(SELECT id FROM eg_module WHERE name='WaterTaxApplication'),  'Update Water Connection Application', 'false', 'wtms');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='UpdateWaterConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='UpdateWaterConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='UpdateWaterConnectionApplication' and contextroot='wtms'),
(SELECT id FROM eg_role where name='Water Tax Approver'));

--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator','Water Tax Approver')) and actionid in (select id FROM eg_action  WHERE name='UpdateWaterConnectionApplication' and contextroot='wtms');
--rollback delete from eg_action where name='UpdateWaterConnectionApplication' and contextroot='wtms';