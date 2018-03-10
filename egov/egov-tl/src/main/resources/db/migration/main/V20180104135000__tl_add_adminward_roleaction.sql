alter table egtl_license ADD column adminward bigint;

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='getWardsByLocality'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='getWardsByLocality'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'SYSTEM'), (SELECT id FROM eg_action WHERE name ='getWardsByLocality'));


