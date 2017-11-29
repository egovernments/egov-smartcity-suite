INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'Load Block By Ward'and contextroot = 'egi'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'Load Block By Ward'and contextroot = 'egi'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'Load Block By Ward'and contextroot = 'egi'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('Load Block By Ward') and contextroot = 'egi') and roleid in((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_role where name = 'Sewerage Tax Report Viewer'),(select id from eg_role where name = 'Sewerage Tax Administrator'));
