---------------------START---------------------------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RefreshCitizenInbox','/home/refreshInbox',null,(select id from eg_module where name='EGI-COMMON'),0,'Refresh Citizen Inboxe','false','portal',0,1,to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Administration' and parentmodule is NULL));
INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER, DISPLAYNAME, ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDDATE,LASTMODIFIEDBY,APPLICATION) Values (nextval('SEQ_EG_ACTION'), 'CitizenInboxForm', '/home',NULL, (SELECT id FROM eg_module WHERE name='EGI-COMMON'), 0, 'User Login', false,'portal',0,1,now(),now(),1,(SELECT id FROM eg_module WHERE name='Administration' and parentmodule is NULL));

INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'RefreshCitizenInbox'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'OfficialChangePassword'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'RemoveFavourite'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'CitizenInboxForm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'AddFavourite'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'Official Home Page'));
---------------------END-----------------------------
