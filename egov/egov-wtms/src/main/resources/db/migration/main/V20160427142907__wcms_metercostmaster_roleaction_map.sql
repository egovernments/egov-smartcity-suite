-----Inserting MeterCostMaster to eg_action-----
Insert into EG_ACTION 
(id, name, url, queryparams, parentmodule, ordernumber, displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Meter Cost Master','/masters/meterCostMaster',null,
(select id from eg_module where name='WaterTaxMasters'),1,'Meter Cost Master','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='Meter Cost Master' and contextroot='wtms'));

-----Inserting MeterCostMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application)values
 (nextval('SEQ_EG_ACTION'),'MeterCostMasterList','/wtms/masters/meterCostMaster/list',null,
 (select id from eg_module where name='WaterTaxMasters'),1,'MeterCostMasterList','false','wtms',0,1,now(),1,now(),
 (select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='MeterCostMasterList' and contextroot='wtms'));