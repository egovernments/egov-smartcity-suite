-----UsageType master-------------
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'UsageTypeMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Usage Type Master', 1);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyUsageTypeMaster','/masters/usageTypeMaster/edit',null,
(select id from eg_module where name='UsageTypeMaster'),3,'Modify Usage Type','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyUsageTypeMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='UsageTypeMaster'),displayname='Create Usage Type',ordernumber=1 WHERE name= 'UsageTypeMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='UsageTypeMaster'),displayname='View Usage Type',ordernumber=2 WHERE name= 'viewUsageTypeMaster';

-------Category master------
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'CategoryMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Category Master', 2);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyCategoryMaster','/masters/categoryMaster/edit',null,
(select id from eg_module where name='CategoryMaster'),3,'Modify Category','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyCategoryMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='CategoryMaster'),displayname='Create Category',ordernumber=1 WHERE name= 'CategoryMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='CategoryMaster'),displayname='View Category',ordernumber=2 WHERE name= 'CategoryMasterList';

-----pipesize master------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'PipeSizeMaster', true, NULL,(select id from eg_module where name='WaterTaxMasters'), 'Pipe Size Master', 3);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'modifyPipeSizeMaster','/masters/pipesizeMaster/edit',null,
(select id from eg_module where name='PipeSizeMaster'),3,'Modify Pipe Size','true','wtms',0,1,now(),1,now(),
(select id from eg_module where name='Water Tax Management'));

Insert INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='modifyPipeSizeMaster'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PipeSizeMaster'),displayname='Create Pipe Size',ordernumber=1 WHERE name= 'PipeSizeMaster';

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='PipeSizeMaster'),displayname='View Pipe Size',ordernumber=2 WHERE name= 'PipeSizeList';