--Add new column to capture uid
ALTER TABLE egtl_license add column uid varchar(64);
ALTER TABLE egtl_license ADD CONSTRAINT uk_license_uid UNIQUE (uid);
UPDATE egtl_license set uid = to_char(current_timestamp,'ddmmyyyyhhmmssmsus') || id;
ALTER TABLE egtl_license alter column uid set not null;

--Action and roleaction mapping
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Show license application',
'/license/show/',null,(select id from eg_module  where name = 'Trade License'),1,
'Show License Application',false,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Show license application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='View License Application');

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License provisional certificate',
'/license/generate-provisionalcertificate/',null,(select id from eg_module  where name = 'Trade License'),1,
'License provisional certificate',false,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='License provisional certificate'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Generate Provisional Certificate');



