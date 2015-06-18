
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'WaterTaxCreateNewConnection', '/application/newConnection-newform.action', null, (select id from eg_module where name='WaterTaxApplication'), 1, 'New Connection', true, 'wtms', 0, 1, now(), 1, now());


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Data Entry Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCreateNewConnection'));


--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','Data Entry Operator')) and actionid in (select id FROM eg_action  WHERE contextroot='wtms');
--rollback delete from eg_action where contextroot='wtms';
