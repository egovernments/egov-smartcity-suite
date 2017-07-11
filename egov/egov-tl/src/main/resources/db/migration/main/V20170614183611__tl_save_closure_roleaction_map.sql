INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'saveclosure','/viewtradelicense/saveclosure.action',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'saveclosure',false,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='saveclosure'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='saveclosure'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLAdmin'), (SELECT id FROM eg_action WHERE name ='saveclosure'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'CSC Operator'), (SELECT id FROM eg_action WHERE name ='saveclosure'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='saveclosure'),(select id FROM EG_FEATURE
WHERE name  ='Closure'));


