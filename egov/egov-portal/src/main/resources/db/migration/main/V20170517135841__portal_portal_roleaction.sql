INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Business User') ,(select id FROM eg_action  WHERE name = 'CitizenInboxForm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Business User') ,(select id FROM eg_action  WHERE name = 'Official Home Page'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Business User') ,(select id FROM eg_action  WHERE name = 'OfficialsProfileEdit'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Business User') ,(select id FROM eg_action  WHERE name = 'OfficialChangePassword'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Business User') ,(select id FROM eg_action  WHERE name = 'RefreshCitizenInbox'));

--rollback delete from EG_ROLEACTION where ROLEID = (select id from eg_role where name ='Business User') and actionid in (select id FROM eg_action  WHERE name in( 'CitizenInboxForm','Official Home Page','RefreshCitizenInbox','OfficialChangePassword','OfficialsProfileEdit'));