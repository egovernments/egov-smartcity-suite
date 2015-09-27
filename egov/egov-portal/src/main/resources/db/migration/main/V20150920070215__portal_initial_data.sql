---------------------START---------------------------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'RefreshCitizenInbox','/home/refreshInbox',null,(select id from eg_module where name='EGI-COMMON'),0,'Refresh Citizen Inboxe','false','portal',0,1,to_timestamp('2015-08-15 11:02:31.028333','null'),1,to_timestamp('2015-08-15 11:02:31.028333','null'),(select id from eg_module where name='Administration'));


INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'RefreshCitizenInbox'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'OfficialChangePassword'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'RemoveFavourite'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'CitizenInboxForm'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'AddFavourite'));
INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'Official Home Page'));
---------------------END-----------------------------
