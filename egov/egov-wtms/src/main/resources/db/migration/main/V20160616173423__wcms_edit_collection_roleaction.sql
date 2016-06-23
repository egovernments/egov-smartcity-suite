Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'EditCollection','/search/waterSearch/commonSearch/', 'applicationType=EDITCOLLECTION',(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,
'Edit Collection','t','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action WHERE NAME = 'EditCollection' and CONTEXTROOT='wtms'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values
(nextval('SEQ_EG_ACTION'),'editdataEntryCollection','/application/editCollection/',null,(select id from eg_module where name='WaterTaxTransactions'),null,'WaterTaxConnectionDataEntry-editcollection','false','wtms',0,1,to_timestamp('2015-08-19 10:45:51.224841','null'),1,to_timestamp('2015-08-19 10:45:51.224841','null'),(select id from eg_module where name='Water Tax Management'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) = 'SUPER USER'),(select id FROM eg_action WHERE NAME = 'editdataEntryCollection' and CONTEXTROOT='wtms'));