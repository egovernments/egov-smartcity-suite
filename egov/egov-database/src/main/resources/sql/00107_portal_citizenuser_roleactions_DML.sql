INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CITIZEN USER') ,(select id FROM eg_action  WHERE name = 'View Complaint'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CITIZEN USER') ,(select id FROM eg_action  WHERE name = 'ComplaintRegisteration'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CITIZEN USER') ,(select id FROM eg_action  WHERE name = 'ComplaintSave'));


