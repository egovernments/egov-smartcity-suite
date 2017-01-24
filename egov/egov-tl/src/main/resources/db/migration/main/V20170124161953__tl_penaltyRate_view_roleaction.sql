-------view Penalty Rate-------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'penaltyratesview', '/penaltyRates/view', NULL, 
 (select id from eg_module where name='License Penalty Rates'), 5, 'View Penalty Rate', true, 'tl', 0, 1, now(), 1, now(), 
 (select id from eg_module where name='Trade License' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'penaltyratesview'));

--------search and view Penalt Rates-----------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, 
version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'penaltyratessearchview', '/penaltyRates/searchview', NULL, (select id from eg_module where name='License Penalty Rates'), 6, 
 'Search View Penalty Rate', false, 'tl', 0, 1, now(), 1, now(), 
 (select id from eg_module where name='Trade License' ));
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,
 (select id FROM eg_action  WHERE name = 'penaltyratessearchview'));



------TLCreator ROLEACTION------
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLCreator') ,
 (select id FROM eg_action  WHERE name = 'penaltyratesview'));
   INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLCreator') ,
 (select id FROM eg_action  WHERE name = 'penaltyratessearchview'));



-----TLApprover roleaction------
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLApprover') ,
 (select id FROM eg_action  WHERE name = 'penaltyratesview'));
   INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLApprover') ,
 (select id FROM eg_action  WHERE name = 'penaltyratessearchview'));

-----TLAdmin roleaction-----
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLAdmin') ,
 (select id FROM eg_action  WHERE name = 'penaltyratesview'));

   INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'TLAdmin') ,
 (select id FROM eg_action  WHERE name = 'penaltyratessearchview'));

----Collection Operator-----
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Collection Operator') ,
 (select id FROM eg_action  WHERE name = 'penaltyratesview'));
  INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Collection Operator') ,
 (select id FROM eg_action  WHERE name = 'penaltyratessearchview')