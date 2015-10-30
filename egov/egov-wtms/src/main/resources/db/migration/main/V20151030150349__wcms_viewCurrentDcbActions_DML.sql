
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby
,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'viewMeteredConnectionDcb','/viewDcb/consumerCodeWis/',null,
(select id from eg_module where name='WaterTaxReports'),1,'viewMeteredConnectionDcb',
'false','wtms',0,1,to_timestamp('2015-08-19 17:14:12.454451','null'),
1,to_timestamp('2015-08-19 17:14:12.454451','null'),
(select id from eg_module where name='Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='viewMeteredConnectionDcb'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='viewMeteredConnectionDcb'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='viewMeteredConnectionDcb'));