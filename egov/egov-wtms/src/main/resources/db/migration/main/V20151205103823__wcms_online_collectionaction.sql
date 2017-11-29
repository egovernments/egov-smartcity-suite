Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot
,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'WaterTaxCollectionOnlineView','/application/generatebillOnline',
null,(select id from eg_module where name='WaterTaxTransactions'),null,
'WaterTaxCollectionOnlineView','false','wtms',0,1,to_timestamp('2015-08-15 11:04:22.606759','null'),
1,to_timestamp('2015-08-15 11:04:22.606759','null'),(select id from eg_module where name='Water Tax Management'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Citizen'),(select id FROM eg_action 
 WHERE url = '/application/generatebillOnline' and CONTEXTROOT='wtms'));
