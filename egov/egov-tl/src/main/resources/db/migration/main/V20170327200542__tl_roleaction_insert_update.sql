
update eg_action set url ='/legacylicense/create' where name='Enter Trade License Action' and contextroot='tl'  ;

update eg_action set url='/legacylicense/update' where name='Modify-Legacy-License' and contextroot ='tl';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'View License','/legacylicense/view/',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'Viewlicense',FALSE, 'tl',0,1,now(),1,now(),(SELECT id FROM eg_module WHERE name = 'Trade License' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='View License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='View License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE name ='View License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLAdmin'), (SELECT id FROM eg_action WHERE name ='View License'));

INSERT INTO EG_FEATURE_ACTION (ACTION ,FEATURE) VALUES((SELECT id FROM eg_action WHERE name = 'View License'),(SELECT id FROM eg_feature WHERE name ='Create Legacy License'));

INSERT INTO EG_FEATURE_ACTION (ACTION ,FEATURE) VALUES((SELECT id FROM eg_action WHERE name = 'View License'),(SELECT id FROM eg_feature WHERE name ='Modify Legacy License'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'OldLicenseNumber','/legacylicense/old-licenseno-is-unique',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'oldlicensenumber',FALSE, 'tl',0,1,now(),1,now(),(SELECT id FROM eg_module WHERE name = 'Trade License' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='OldLicenseNumber'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLApprover'), (SELECT id FROM eg_action WHERE name ='OldLicenseNumber'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE name ='OldLicenseNumber'));

INSERT INTO EG_FEATURE_ACTION (ACTION ,FEATURE) VALUES((SELECT id FROM eg_action WHERE name = 'OldLicenseNumber'),(SELECT id FROM eg_feature WHERE name ='Create Legacy License'));

INSERT INTO EG_FEATURE_ACTION (ACTION ,FEATURE) VALUES((SELECT id FROM eg_action WHERE name = 'OldLicenseNumber'),(SELECT id FROM eg_feature WHERE name ='Create Legacy License'));