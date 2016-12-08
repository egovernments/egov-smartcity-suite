INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'viewclosurelicense','/viewtradelicense/viewTradeLicense-closure',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'viewclosurelicense',FALSE, 'tl',0,1,now(),1,now(),(SELECT id FROM eg_module WHERE name = 'Trade License' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='viewclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='viewclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLAdmin'), (SELECT id FROM eg_action WHERE name ='viewclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE name ='viewclosurelicense'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'updateclosurelicense','/viewtradelicense/viewTradeLicense-cancelLicense.action',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'updateclosurelicense',FALSE, 'tl',0,1,now(),1,now(),(SELECT id FROM eg_module WHERE name = 'Trade License' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='updateclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='updateclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLAdmin'), (SELECT id FROM eg_action WHERE name ='updateclosurelicense'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE name ='updateclosurelicense'));
