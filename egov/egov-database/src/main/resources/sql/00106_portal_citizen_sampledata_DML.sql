
INSERT INTO eg_role(id, name, description, localname, localdescription,createddate,createdby,lastmodifiedby,lastmodifieddate,version) VALUES (nextval('seq_eg_role'), 'Citizen User', 'Citizen User', null, null, current_date, 1,null,null,0);


INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CITIZEN USER') ,(select id FROM eg_action  WHERE name = 'LoginForm'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CITIZEN USER') ,(select id FROM eg_action  WHERE name = 'CitizenInboxForm'));
