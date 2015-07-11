INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'WaterTaxChangeOfUseApplication', '/application/changeOfUse/', null, (select id from eg_module where name='WaterTaxApplication'), 1, 'Change Of Use', false, 'wtms', 0, 1, now(), 1, now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxChangeOfUseApplication'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxChangeOfUseApplication'));



INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_eg_action'), 'WaterTaxCreateChangeOfUseApplication', '/application/changeOfUse-create', null, (select id from eg_module where name='WaterTaxApplication'), 1, 'Create Change Of Use', false, 'wtms', 0, 1, now(), 1, now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxCreateChangeOfUseApplication'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxCreateChangeOfUseApplication'));


--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator')) and actionid in (select id FROM eg_action  WHERE name='WaterTaxCreateChangeOfUseApplication' and contextroot='wtms');
--rollback delete from eg_action where name='WaterTaxCreateChangeOfUseApplication' and contextroot='wtms';
--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator')) and actionid in (select id FROM eg_action  WHERE name='WaterTaxChangeOfUseApplication' and contextroot='wtms');
--rollback delete from eg_action where name='WaterTaxChangeOfUseApplication' and contextroot='wtms';
