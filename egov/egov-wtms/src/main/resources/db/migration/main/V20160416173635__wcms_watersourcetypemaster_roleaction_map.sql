-----Inserting WaterSourceMaster to eg_action-----
Insert into EG_ACTION 
(id, name, url, queryparams, parentmodule, ordernumber, displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Water Source Type','/masters/waterSourceTypeMaster',null,
(select id from eg_module where name='WaterTaxMasters'),1,'Water Source Type','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='Water Source Type' and contextroot='wtms'));

-----Inserting WaterSourceMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application)values
 (nextval('SEQ_EG_ACTION'),'WaterSourceTypeList','/wtms/masters/waterSourceTypeMaster/list',null,
 (select id from eg_module where name='WaterTaxMasters'),1,'WaterSourceTypeList','false','wtms',0,1,now(),1,now(),
 (select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='WaterSourceTypeList' and contextroot='wtms'));