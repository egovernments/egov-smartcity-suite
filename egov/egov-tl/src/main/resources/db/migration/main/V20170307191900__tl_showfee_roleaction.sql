INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'paylicensefees','/integration/licenseBillCollect-collectfees.action',null,(select id from EG_MODULE where name = 'Trade License'),1,'paylicensefees',FALSE, 'tl',0,1,now(),1,now(),(SELECT id FROM eg_module WHERE name = 'Trade License' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Collection Operator'), (SELECT id FROM eg_action WHERE name ='paylicensefees'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE name ='paylicensefees'));
