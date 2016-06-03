insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksAddEstimateTemplateToEstimate' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksEstimateTemplateSearch' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksEstimateTemplateSearchResult' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksEstimateSubTypeAjax' and contextroot = 'egworks'));




--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksAddEstimateTemplateToEstimate' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksEstimateTemplateSearch' and contextroot = 'egworks');


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksEstimateTemplateSearchResult' and contextroot = 'egworks');


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksEstimateSubTypeAjax' and contextroot = 'egworks');

