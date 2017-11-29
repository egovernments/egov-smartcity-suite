
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'STDailyCollectionReport', 
'/reports/dailySTCollectionReport/search/', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Daily Collection Report', true, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
 
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'STDailyCollectionReport'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'STDailyCollectionReport'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'STDailyCollectionReport'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'STDailyCollectionReport'));
