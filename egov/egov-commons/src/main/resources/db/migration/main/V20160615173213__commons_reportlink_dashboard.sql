
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES  (nextval('seq_eg_action'), 'ERP-Reports', '/erpReports', NULL, (select id from eg_module where name='Dashboard' and parentmodule is null), 1, 'ERP Reports', true, 'dashboard', 0, 1, now(), 1, now(), (select id from eg_module where name='Dashboard' and parentmodule is null));

INSERT INTO EG_ROLEACTION (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'), (select id from eg_action where name='ERP-Reports'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name='ERP-Reports'));
