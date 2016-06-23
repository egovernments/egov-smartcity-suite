INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES (nextval('seq_eg_action'), 'pgrkibanadashboard', '/pgr', NULL, (select id from eg_module where name='Dashboard' and parentmodule is null), 3, 'Grievances Dashboard', true, 'dashboard', 0, 1, '2015-08-28 10:43:35.552035', 1, '2015-08-28 10:43:35.552035', (select id from eg_module where name='Dashboard' and parentmodule is null));


INSERT INTO EG_ROLEACTION (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name='pgrkibanadashboard'));
