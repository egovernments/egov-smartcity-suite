--view feematrix
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'feematrix-view','/feematrix/view-feematrix',null,(select id from EG_MODULE where name = 'Trade License Fee Matrix'),3,'View Fee Matrix',true,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='feematrix-view'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='feematrix-view'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,
VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'feematrix-resultview','/feematrix/viewresult',null,(select id from EG_MODULE where name = 'Trade License Fee Matrix'),
3,'feematrix-resultview',false,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='feematrix-resultview'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='feematrix-resultview'));

