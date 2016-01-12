Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'CategoryMaster','/masters/categoryMaster',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'Category Master','true','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='CategoryMaster'));