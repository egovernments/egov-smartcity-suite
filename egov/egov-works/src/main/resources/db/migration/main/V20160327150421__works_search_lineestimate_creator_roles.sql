-------------- Role action mappings to view Line estimate screen ------------------
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksSearchLineEstimateForm' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'Search LineEstimate' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'Ajax Search Line Estimate Numbers' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'Ajax Search Admin Sanction Numbers' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchLineEstimateForm' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Search LineEstimate' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Line Estimate Numbers' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Admin Sanction Numbers' and contextroot = 'egworks');