
alter table egtl_license  ADD column closed boolean default false;

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Closure Endorsement Notice',
'/license/closure/endorsementnotice',null,(select id from EG_MODULE where name = 'Trade License'),1,
'Generate Closure Endorsement Notice','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Generate Closure Endorsement Notice'),
(select id from eg_feature  where name='Search Trade License'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Generate Closure Endorsement Notice'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='Search Trade License');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Closure DigitalSigned',
'/license/closure/digisign-transition',null,(select id from EG_MODULE where name = 'Trade License'),1,
'Closure DigitalSigned','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Closure DigitalSigned'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Closure DigitalSigned'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='updateclosurelicense');


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Download Closure Endorsement Notice',
'/license/closure/download-endorsementnotice',null,(select id from EG_MODULE where name = 'Trade License'),1,
'Download Closure Endorsement Notice','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Download Closure Endorsement Notice'),
(select id from eg_feature  where name='License Closure Approval'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Download Closure Endorsement Notice'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='updateclosurelicense');
