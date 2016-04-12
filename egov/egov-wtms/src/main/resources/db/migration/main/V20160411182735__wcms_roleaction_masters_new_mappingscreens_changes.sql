-----Inserting PropertyUsageMaster to eg_action-----
Insert into EG_ACTION 
(id, name, url, queryparams, parentmodule, ordernumber, displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Property Usage','/masters/propertyUsageMaster',null,
(select id from eg_module where name='WaterTaxMasters'),1,'Property Usage','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),(select id from eg_action where name='Property Usage' and contextroot='wtms'));

-----Inserting PropertyUsageMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'PropertyUsageList','/wtms/masters/propertyUsageMaster/list',null,
(select id from eg_module where name='WaterTaxMasters'),1,'PropertyUsageList','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),(select id from eg_action where name='PropertyUsageList' and contextroot='wtms'));

-----Inserting PropertyCategoryMaster to eg_action-----
Insert into EG_ACTION 
(id, name, url, queryparams, parentmodule, ordernumber, displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Property Category','/masters/propertyCategoryMaster',null,
(select id from eg_module where name='WaterTaxMasters'),1,'Property Category','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),(select id from eg_action where name='Property Category' and contextroot='wtms'));

-----Inserting PropertyCategoryMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'PropertyCategoryList','/wtms/masters/propertyCategoryMaster/list',null,
(select id from eg_module where name='WaterTaxMasters'),1,'PropertyCategoryList','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='PropertyCategoryList' and contextroot='wtms'));


-----Inserting PropertyPipeSizeMaster to eg_action-----
Insert into EG_ACTION 
(id, name, url, queryparams, parentmodule, ordernumber, displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Property Pipe Size','/masters/propertyPipeSizeMaster',null,
(select id from eg_module where name='WaterTaxMasters'),1,'Property Pipe Size','true','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values 
((select id from eg_role where name='Super User'),(select id from eg_action where name='Property Pipe Size' and contextroot='wtms'));

-----Inserting PropertyPipeSizeMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application)values
 (nextval('SEQ_EG_ACTION'),'PropertyPipeSizeList','/wtms/masters/propertyPipeSizeMaster/list',null,
 (select id from eg_module where name='WaterTaxMasters'),1,'PropertyPipeSizeList','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='PropertyPipeSizeList' and contextroot='wtms'));

-----Inserting PipeSizeMasterList to eg_action-----

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,
lastmodifieddate,application)values
 (nextval('SEQ_EG_ACTION'),'PipeSizeList','/wtms/masters/pipesizeMaster/list',null,
 (select id from eg_module where name='WaterTaxMasters'),1,'List H.S.C Pipe Size Master','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='PipeSizeList' and contextroot='wtms'));

