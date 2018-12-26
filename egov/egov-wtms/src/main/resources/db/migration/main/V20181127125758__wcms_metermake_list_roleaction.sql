

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'GetActiveMeterMakeList', '/application/execute-update/search-meter', null,(select id from eg_module where name='WaterTaxTransactions'), 3, 'Get Active Water Meter Make List', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Connection Executor'),
(select id from eg_action where name='GetActiveMeterMakeList'));

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='GetActiveMeterMakeList'));

