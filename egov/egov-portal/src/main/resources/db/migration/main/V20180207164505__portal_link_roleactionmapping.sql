INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Link Service','/citizen/searchforlinking',null,(SELECT id FROM eg_module  WHERE name = 'Portal Services'),1,'Link Service',false, 'portal',0,1,now(),1,now(),(SELECT id FROM eg_module  WHERE name = 'My Services'));

INSERT INTO eg_roleaction(roleid,actionid) VALUES ((SELECT id FROM eg_role WHERE name='CITIZEN'), (SELECT id FROM eg_action WHERE name='Link Service'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Link Service search','/citizen/searchresult',null,(SELECT id FROM eg_module  WHERE name = 'Portal Services'),1,'Link Service search',false, 'portal',0,1,now(),1,now(),(SELECT id FROM eg_module  WHERE name = 'My Services'));

INSERT INTO eg_roleaction(roleid,actionid) VALUES ((SELECT id FROM eg_role WHERE name='CITIZEN'), (SELECT id FROM eg_action WHERE name='Link Service search'));

UPDATE egp_portalservice SET code='Link Services',url='/portal/citizen/searchforlinking', name ='Link Services' WHERE code='Link Application';

INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'CITIZEN'), (select id from eg_action where name ='View DCB Property Display'));

ALTER table egp_portallink  ADD COLUMN viewDcbURL character varying(250);

ALTER table egp_portallink  ALTER COLUMN applicantname drop not null;