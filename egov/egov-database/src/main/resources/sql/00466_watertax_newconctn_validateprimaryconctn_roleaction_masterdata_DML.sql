INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
    VALUES (nextval('seq_eg_action'), 'WaterTaxAjaxCheckPrimaryConnection', '/ajaxconnection/check-primaryconnection-exists', null, (select id from eg_module where name='WaterTaxTransactions'), 1, 'WaterTaxAjaxCheckPrimaryConnection', false, 'wtms', 0, 1, now(), 1, now(), (SELECT id FROM eg_module md WHERE md.parentmodule IS NULL AND md.contextroot = 'wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection'));

--rollback delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','CSC Operator', 'ULB Operator')) and actionid in (select id FROM eg_action  WHERE name='WaterTaxAjaxCheckPrimaryConnection' and contextroot='wtms');
--rollback delete from eg_action where name='WaterTaxAjaxCheckPrimaryConnection' and contextroot='wtms';
