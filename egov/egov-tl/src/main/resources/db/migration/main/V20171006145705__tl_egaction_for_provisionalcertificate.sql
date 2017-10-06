
--added action for provisional certificate and roleaction mapping
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Provisional Certificate',
'/viewtradelicense/generate-provisional-certificate.action',null,(select id from EG_MODULE where name = 'Trade License'),1,
'Generate Provisional Certificate','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Generate Provisional Certificate'),
(select id from eg_feature  where name='Print Certificate'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Generate Provisional Certificate'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='View Trade License Generate Certificate');
