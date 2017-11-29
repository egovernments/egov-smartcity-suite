

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'WaterRatesDetailsMaster','/masters/waterRatesMaster',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'Monthly rent for Non-meter Master','true','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator')
,(select id from eg_action where name='WaterRatesDetailsMaster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='WaterRatesDetailsMaster'));



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'ajaxWaterRatesamster','/ajax-WaterRatescombination',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'ajaxWaterRatesamster','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator')
,(select id from eg_action where name='ajaxWaterRatesamster'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='ajaxWaterRatesamster'));
