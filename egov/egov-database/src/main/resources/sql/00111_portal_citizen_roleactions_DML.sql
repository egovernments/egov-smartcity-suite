DELETE FROM EG_ROLEACTION_MAP WHERE ROLEID IN (select id from eg_role where UPPER(name) LIKE 'CITIZEN USER');

DELETE FROM eg_userrole where roleid   IN (select id from eg_role where UPPER(name) LIKE 'CITIZEN USER');

DELETE FROM  eg_role  where name ='Citizen User';

INSERT INTO EG_USERROLE (ROLEID, USERID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_user  WHERE  username ='citizenUser'));

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID IN (select id FROM eg_action  WHERE name = 'ComplaintRegisteration') AND ROLEID IN (select id from eg_role where UPPER(name) LIKE 'SUPERUSER');

update eg_user set username = '9999999999', mobilenumber = '9999999999' where username ='citizenUser';

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'View Complaint'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintRegisteration'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintSave'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintTypeAjax'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintLocationRequired'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintLocations'));

