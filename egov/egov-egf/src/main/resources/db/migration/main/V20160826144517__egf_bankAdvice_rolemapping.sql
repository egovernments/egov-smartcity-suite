
INSERT INTO eg_roleaction ( actionid,roleid) select (select id from eg_action where name='bankAdviceReportExportExcel'),id from eg_role where name in('Financial Report Viewer','Payment Creator','ERP Report Viewer');


