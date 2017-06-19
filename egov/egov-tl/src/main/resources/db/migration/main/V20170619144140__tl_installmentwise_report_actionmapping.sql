
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'Year Wise Report Gand Total','/report/dcb/yearwise/grand-total',null,(select id from eg_module where name='Trade License Reports'),1,'Year Wise Report Gand Total',FALSE, 'tl',0,1,now(),1,now(),(select id from eg_module  where name='Trade License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLCreator'),(SELECT id FROM eg_action WHERE name ='Year Wise Report Gand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='Year Wise Report Gand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),(SELECT id FROM eg_action WHERE name ='Year Wise Report Gand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='Year Wise Report Gand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='Year Wise Report Gand Total'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Download Year Wise Report','/report/dcb/yearwise/download',null,(select id from eg_module where name='Trade License Reports'),1,'Download Year Wise Report',FALSE, 'tl',0,1,now(),1,now(),(select id from eg_module  where name='Trade License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLCreator'),(SELECT id FROM eg_action WHERE name ='Download Year Wise Report'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='Download Year Wise Report'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),(SELECT id FROM eg_action WHERE name ='Download Year Wise Report'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='Download Year Wise Report'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='Download Year Wise Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Download Year Wise Report'),(select id FROM EG_FEATURE
WHERE name  ='License Yearwise DCB Report'));
 
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Year Wise Report Gand Total'),(select id FROM EG_FEATURE
WHERE name  ='License Yearwise DCB Report'));