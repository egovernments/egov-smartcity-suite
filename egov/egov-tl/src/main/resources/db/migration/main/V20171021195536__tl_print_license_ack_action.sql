
--print license acknowledgment
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'License Acknowledgement',
'/license/acknowledgement',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'License Acknowledgement','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='License Acknowledgement'),
(select id from eg_feature  where name='Search Trade License'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='License Acknowledgement'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='Search Trade License');
