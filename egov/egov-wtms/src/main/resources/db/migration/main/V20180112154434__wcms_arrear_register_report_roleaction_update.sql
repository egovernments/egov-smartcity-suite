
delete from eg_roleaction where actionid in (select id from eg_action where name in ('waterchargearrearReportResult') and contextroot = 'wtms') and roleid in(select id from eg_role where name = 'Water Tax Report Viewer');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='waterchargearrearReportResult'));
