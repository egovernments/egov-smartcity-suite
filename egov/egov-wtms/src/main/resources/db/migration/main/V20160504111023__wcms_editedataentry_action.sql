Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,
CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'modifydataentryconnectiondetails','/search/waterSearch/commonSearch/', 'applicationType=DATAENTRYEDIT',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,'Modify Connection Details','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


insert into eg_roleaction (roleid,actionid)values((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='modifydataentryconnectiondetails'
and contextroot='wtms'));

insert into eg_roleaction (roleid,actionid)values((select id from eg_role where name='Super User'),(select id from eg_action where name='modifydataentryconnectiondetails'
and contextroot='wtms'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,
CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'editdatentryForm','/application/newConnection-editExisting/', null,(select id from EG_MODULE where name =
 'WaterTaxTransactions'),null,'editdatentryForm','f','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


insert into eg_roleaction (roleid,actionid)values((select id from eg_role where name='Property Administrator'),
(select id from eg_action where url='/application/newConnection-editExisting/'
and contextroot='wtms'));

insert into eg_roleaction (roleid,actionid)values((select id from eg_role where name='Super User'),
(select id from eg_action where url='/application/newConnection-editExisting/'
and contextroot='wtms'));
