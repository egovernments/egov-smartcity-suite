INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'View Complaint'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintRegisteration'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintSave'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintTypeAjax'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintLocationRequired'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'ComplaintLocations'));

