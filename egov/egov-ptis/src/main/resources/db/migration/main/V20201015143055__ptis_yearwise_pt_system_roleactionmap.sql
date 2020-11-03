delete from eg_roleaction where actionid = (select id from eg_action where name = 'YearwiseDCBReportResult') and roleid = (select id from eg_role where name = 'SYSTEM');

delete from eg_roleaction where actionid = (select id from eg_action where name = 'YearwiseDCBReport') and roleid = (select id from eg_role where name = 'SYSTEM');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'YearwiseDCBReportResult'),id from eg_role where name = 'SYSTEM';
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'YearwiseDCBReport'),id from eg_role where name = 'SYSTEM';
