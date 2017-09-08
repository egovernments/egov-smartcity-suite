INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled,contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage base register report download', '/reports/seweragebaseregister/download', null, (select id from eg_module where name = 'SewerageReports'), 6, 'sewerage Base Register report download', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='SYSTEM') , (select id from eg_action where name='Sewerage base register report download'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage base register report download'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage base register report download'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage base register report download'));



INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled,contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage base register Grand Total', '/reports/seweragebaseregister/grand-total', null, (select id from eg_module where name = 'SewerageReports'), 7, 'Sewerage Base Register Total', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='SYSTEM') , (select id from eg_action where name='Sewerage base register Grand Total'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage base register Grand Total'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage base register Grand Total'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage base register Grand Total'));
