---------------Map the Action urls to the Super User role--------------------
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'WorksLineEstimateSuccess' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksLineEstimateSuccess' and contextroot = 'egworks');