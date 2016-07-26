-------Donation master-----

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'DonationMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Donation Master', 4);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyDonationMaster','/masters/donationMaster/edit',null,
(select id from eg_module where name='DonationMaster'),3,'Modify Donation','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyDonationMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='DonationMaster'),displayname='Create Donation',ordernumber=1 WHERE name= 'DonationMasterDetailsScreen';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='DonationMaster'),displayname='View Donation',ordernumber=2 WHERE name= 'viewDonationMaster';


-------Waterrates Header master-----

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'WaterRatesHeaderMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Monthly rent for Non-meter Master', 5);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifywaterRatesMaster','/masters/waterRatesMaster/edit',null,
(select id from eg_module where name='WaterRatesHeaderMaster'),3,'Modify Water Rates','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifywaterRatesMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='WaterRatesHeaderMaster'),displayname='Create Water Rates',ordernumber=1 WHERE name= 'WaterRatesDetailsMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='WaterRatesHeaderMaster'),displayname='View Water Rates',ordernumber=2 WHERE name= 'viewWaterRatesMaster';

-----propertyUsage Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'PropertyUsageMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Property Usage Master', 6);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyPropertyUsageMaster','/masters/propertyUsageMaster/edit',null,
(select id from eg_module where name='PropertyUsageMaster'),3,'Modify Property Usage','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyPropertyUsageMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyUsageMaster'),displayname='Create Property Usage',ordernumber=1 WHERE name= 'Property Usage';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyUsageMaster'),displayname='View Property Usage',ordernumber=2 WHERE name= 'PropertyUsageList';

-----propertyCategory Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'PropertyCategoryMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Property Category Master', 7);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyPropertyCategoryMaster','/masters/propertyCategoryMaster/edit',null,
(select id from eg_module where name='PropertyCategoryMaster'),3,'Modify Property Category','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyPropertyCategoryMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyCategoryMaster'),displayname='Create Property Category',ordernumber=1 WHERE name= 'Property Category';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyCategoryMaster'),displayname='View Property Category',ordernumber=2 WHERE name= 'PropertyCategoryList';

-----propertyPipeSize Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'PropertyPipeSizeMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Property PipeSize Master', 8);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifypropertyPipeSizeMaster','/masters/propertyPipeSizeMaster/edit',null,
(select id from eg_module where name='PropertyPipeSizeMaster'),3,'Modify Property PipeSize','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifypropertyPipeSizeMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyPipeSizeMaster'),displayname='Create Property PipeSize',ordernumber=1 WHERE name= 'Property Pipe Size';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PropertyPipeSizeMaster'),displayname='View Property PipeSize',ordernumber=2 WHERE name= 'PropertyPipeSizeList';

-----waterSourceType Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'WaterSourceTypeMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Water Source Master', 9);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyWaterSourceTypeMaster','/masters/waterSourceTypeMaster/edit',null,
(select id from eg_module where name='WaterSourceTypeMaster'),3,'Modify Water Source','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyWaterSourceTypeMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='WaterSourceTypeMaster'),displayname='Create Water Source',ordernumber=1 WHERE name= 'Water Source Type';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='WaterSourceTypeMaster'),displayname='View Water Source',ordernumber=2 WHERE name= 'WaterSourceTypeList';


-----meterCostMaster Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'MeterCostMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Meter Cost Master', 10);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyMeterCostMaster','/masters/meterCostMaster/edit',null,
(select id from eg_module where name='MeterCostMaster'),3,'Modify Meter Cost','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyMeterCostMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='MeterCostMaster'),displayname='Create Meter Cost',ordernumber=1 WHERE name= 'Meter Cost Master';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='MeterCostMaster'),displayname='View Meter Cost',ordernumber=2 WHERE name= 'MeterCostMasterList';

-----applicationProcessTime Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'ApplicationProcessTime', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Application Process Time', 11);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyApplicationProcessTime','/masters/applicationProcessTime/edit',null,
(select id from eg_module where name='ApplicationProcessTime'),3,'Modify Application Process Time','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyApplicationProcessTime'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='ApplicationProcessTime'),displayname='Create Application Process Time',ordernumber=1 WHERE name= 'ApplicationProcessTimeMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='ApplicationProcessTime'),displayname='View Application Process Time',ordernumber=2 WHERE name= 'ApplicationProcessTimeList';

-----documentNamesMaster Master-------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'DocumentNamesMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Document Names Master ', 12);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyDocumentNamesMaster','/masters/documentNamesMaster/edit',null,
(select id from eg_module where name='DocumentNamesMaster'),3,'Modify Document Names','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyDocumentNamesMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='DocumentNamesMaster'),displayname='Create Document Names',ordernumber=1 WHERE name= 'DocumentNamesMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='DocumentNamesMaster'),displayname='View Document Names',ordernumber=2 WHERE name= 'DocumentNamesList';
