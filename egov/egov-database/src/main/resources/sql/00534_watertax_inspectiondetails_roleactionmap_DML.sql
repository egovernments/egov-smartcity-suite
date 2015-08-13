INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB OPERATOR') ,(select id FROM eg_action  WHERE name = 'connectiontypesajax' and contextroot='wtms'));

--rollback DELETE FROM EG_ROLEACTION WHERE ROLEID = (select id from eg_role where UPPER(name) LIKE 'ULB OPERATOR') and ACTIONID IN (select id FROM eg_action  WHERE name = 'connectiontypesajax' and contextroot='wtms');

