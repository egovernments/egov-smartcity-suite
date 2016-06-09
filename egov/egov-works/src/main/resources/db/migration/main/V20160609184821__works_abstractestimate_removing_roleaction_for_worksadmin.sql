-----------Deleting roleaction mappings to update Abstract Estimate for Works Administrator------------
delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksUpdateAbstractEstimate' and contextroot = 'egworks');
delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksAbstractEstimateSuccessPage' and contextroot = 'egworks');

--rollback insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksUpdateAbstractEstimate' and contextroot = 'egworks'));
--rollback insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'),(select id from eg_action where name ='WorksAbstractEstimateSuccessPage' and contextroot = 'egworks'));