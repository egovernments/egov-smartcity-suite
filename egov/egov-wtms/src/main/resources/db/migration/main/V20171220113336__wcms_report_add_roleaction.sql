
Delete from eg_roleaction where actionid=(select id from eg_action where name='waterchargearrearReport') and roleid=(select id from eg_role where name='Water Tax Report Viewer');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='waterchargearrearReport'));

Delete from eg_roleaction where actionid=(select id from eg_action where name='WaterTaxConnectionReportWardWise') and roleid=(select id from eg_role where name='Water Tax Report Viewer');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='WaterTaxConnectionReportWardWise'));


