---------START-----------
INSERT INTO eg_roleaction (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Works Creator'),(SELECT id FROM eg_action WHERE name ='WorksAjaxGetWard' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksAjaxGetWard' and contextroot = 'egworks');
------------END---------------