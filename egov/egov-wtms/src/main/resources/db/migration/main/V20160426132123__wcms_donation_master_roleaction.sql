update eg_action set url ='/masters/donationMaster/' where name  ='DonationMasterDetailsScreen' and contextroot ='wtms';

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'viewDonationMaster','/masters/donationMaster/list',null,(select 
id from eg_module where name='WaterTaxMasters'),null,'viewDonationMaster','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='viewDonationMaster'));
