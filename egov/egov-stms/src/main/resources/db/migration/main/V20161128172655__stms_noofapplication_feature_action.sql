DELETE FROM eg_roleaction where actionid in (select id from eg_action where name in ('Load Block By Ward') and contextroot = 'egi') and roleid in (select id from eg_role where name = 'Super User');

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'Load Block By Ward'and contextroot = 'egi'));

INSERT INTO EG_FEATURE (id, name, description, module) VALUES (nextval('seq_eg_feature'), 'Search number of sewerage applications', 'Search number of sewerage applications report', (select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO eg_feature_action (action,feature) values ((select id from eg_action where name = 'SewerageNoOfConnectionReportSearch' and contextroot = 'stms'), (select id from eg_feature where name = 'Search number of sewerage applications'));
INSERT INTO eg_feature_action (action,feature) values ((select id from eg_action where name = 'SewerageNoOfConnectionReportView' and contextroot = 'stms'), (select id from eg_feature where name = 'Search number of sewerage applications'));
INSERT INTO eg_feature_action (action,feature) values ((select id from eg_action where name = 'Load Block By Ward' and contextroot = 'egi'), (select id from eg_feature where name = 'Search number of sewerage applications'));

INSERT INTO eg_feature_role (feature,role)  values  ((select id from eg_feature  where name = 'Search number of sewerage applications'), (select id from eg_role where name = 'Super User'));
INSERT INTO eg_feature_role  (feature,role)  values  ((select id from eg_feature  where name = 'Search number of sewerage applications'), (select id from eg_role where name = 'Sewerage Tax Administrator'));
INSERT INTO eg_feature_role (feature,role)  values  ((select id from eg_feature  where name = 'Search number of sewerage applications'), (select id from eg_role where name = 'Sewerage Tax Report Viewer'));
INSERT INTO eg_feature_role  (feature,role)  values  ((select id from eg_feature  where name = 'Search number of sewerage applications'), (select id from eg_role where name = 'Sewerage Tax Creator'));

-- rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('Load Block By Ward') and contextroot = 'egi') and roleid in (select id from eg_role where name = 'Super User');

-- rollback delete from eg_feature where name='Search number of sewerage applications'  and module = (select id from eg_module where name='Sewerage Tax Management');
-- rollback delete from eg_feature_action  where action = (select id from eg_action where name = 'SewerageNoOfConnectionReportSearch' and contextroot = 'stms') and feature = (select id from eg_feature where name = 'Search number of sewerage applications');
-- rollback delete from eg_feature_action  where action = (select id from eg_action where name = 'SewerageNoOfConnectionReportView' and contextroot = 'stms') and  feature = (select id from eg_feature where name = 'Search number of sewerage applications');
-- rollback delete from eg_feature_action  where action = (select id from eg_action where name = 'Load Block By Ward' and contextroot = 'egi') and  feature = (select id from eg_feature where name = 'Search number of sewerage applications');
-- rollback delete from eg_feature_role  where feature  = (select id from eg_feature  where name = 'Search number of sewerage applications') and role in ((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_role where name = 'Sewerage Tax Creator'));

