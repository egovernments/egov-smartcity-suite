
--pgr roleaction mapping for functionarywise report

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='Functionarywise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='Functionarywise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='Functionarywise report Grand Total'));