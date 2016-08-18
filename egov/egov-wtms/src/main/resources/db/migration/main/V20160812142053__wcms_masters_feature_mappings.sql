


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create UsageTypeMaster','Create UsageTypeMaster',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View UsageTypeMaster','View UsageTypeMaster',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify UsageTypeMaster','Modify UsageTypeMaster',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UsageTypeMaster') ,(select id FROM eg_feature WHERE name = 'Create UsageTypeMaster'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewUsageTypeMaster') ,(select id FROM eg_feature WHERE name = 'View UsageTypeMaster'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyUsageTypeMaster') ,(select id FROM eg_feature WHERE name = 'Modify UsageTypeMaster'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create UsageTypeMaster'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create UsageTypeMaster'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify UsageTypeMaster'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify UsageTypeMaster'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View UsageTypeMaster'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View UsageTypeMaster'));


---

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Category Master','Create Category Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Category Master','View Category Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Category Master','Modify Category Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CategoryMaster') ,(select id FROM eg_feature WHERE name = 'Create Category Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CategoryMasterList') ,(select id FROM eg_feature WHERE name = 'View Category Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyCategoryMaster') ,(select id FROM eg_feature WHERE name = 'Modify Category Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Category Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Category Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Category Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Category Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Category Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Category Master'));

---



INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create PipeSize Master','Create PipeSize Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View PipeSize Master','View PipeSize Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify PipeSize Master','Modify PipeSize Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PipeSizeMaster') ,(select id FROM eg_feature WHERE name = 'Create PipeSize Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PipeSizeList') ,(select id FROM eg_feature WHERE name = 'View PipeSize Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyPipeSizeMaster') ,(select id FROM eg_feature WHERE name = 'Modify PipeSize Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create PipeSize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create PipeSize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify PipeSize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify PipeSize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View PipeSize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View PipeSize Master'));

--



INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Donation Master','Create Donation Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Donation Master','View Donation Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Donation Master','Modify Donation Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DonationMasterDetailsScreen') ,(select id FROM eg_feature WHERE name = 'Create Donation Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewDonationMaster') ,(select id FROM eg_feature WHERE name = 'View Donation Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyDonationMaster') ,(select id FROM eg_feature WHERE name = 'Modify Donation Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View Donation Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Donation Master'));

--


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterRatedNon Master','Create WaterRatedNon Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View WaterRatedNon Master','View WaterRatedNon Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify WaterRatedNon Master','Modify WaterRatedNon Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterRatesDetailsMaster') ,(select id FROM eg_feature WHERE name = 'Create WaterRatedNon Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'viewWaterRatesMaster') ,(select id FROM eg_feature WHERE name = 'View WaterRatedNon Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifywaterRatesMaster') ,(select id FROM eg_feature WHERE name = 'Modify WaterRatedNon Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create WaterRatedNon Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create WaterRatedNon Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify WaterRatedNon Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify WaterRatedNon Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View WaterRatedNon Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View WaterRatedNon Master'));

---


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create PropertyUsage Master','Create PropertyUsage Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View PropertyUsage Master','View PropertyUsage Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify PropertyUsage Master','Modify PropertyUsage Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Property Usage') ,(select id FROM eg_feature WHERE name = 'Create PropertyUsage Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PropertyUsageList') ,(select id FROM eg_feature WHERE name = 'View PropertyUsage Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyPropertyUsageMaster') ,(select id FROM eg_feature WHERE name = 'Modify PropertyUsage Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create PropertyUsage Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create PropertyUsage Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify PropertyUsage Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify PropertyUsage Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View PropertyUsage Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View PropertyUsage Master'));


--

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create PropertyCategory Master','Create PropertyCategory Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View PropertyCategory Master','View PropertyCategory Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify PropertyCategory Master','Modify PropertyCategory Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Property Category') ,(select id FROM eg_feature WHERE name = 'Create PropertyCategory Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PropertyCategoryList') ,(select id FROM eg_feature WHERE name = 'View PropertyCategory Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyPropertyCategoryMaster') ,(select id FROM eg_feature WHERE name = 'Modify PropertyCategory Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create PropertyCategory Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create PropertyCategory Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify PropertyCategory Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify PropertyCategory Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View PropertyCategory Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View PropertyCategory Master'));

--


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create PropertyPipesize Master','Create PropertyPipesize Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View PropertyPipesize Master','View PropertyPipesize Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify PropertyPipesize Master','Modify PropertyPipesize Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Property Pipe Size') ,(select id FROM eg_feature WHERE name = 'Create PropertyPipesize Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PropertyPipeSizeList') ,(select id FROM eg_feature WHERE name = 'View PropertyPipesize Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifypropertyPipeSizeMaster') ,(select id FROM eg_feature WHERE name = 'Modify PropertyPipesize Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create PropertyPipesize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create PropertyPipesize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify PropertyPipesize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify PropertyPipesize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View PropertyPipesize Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View PropertyPipesize Master'));


--


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create WaterSourceType Master','Create WaterSourceType Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View WaterSourceType Master','View WaterSourceType Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify WaterSourceType Master','Modify WaterSourceType Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Water Source Type') ,(select id FROM eg_feature WHERE name = 'Create WaterSourceType Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterSourceTypeList') ,(select id FROM eg_feature WHERE name = 'View WaterSourceType Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyWaterSourceTypeMaster') ,(select id FROM eg_feature WHERE name = 'Modify WaterSourceType Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create WaterSourceType Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create WaterSourceType Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify WaterSourceType Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify WaterSourceType Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View WaterSourceType Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View WaterSourceType Master'));


---

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create MeterCost Master','Create MeterCost Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View MeterCost Master','View MeterCost Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify MeterCost Master','Modify MeterCost Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Meter Cost Master') ,(select id FROM eg_feature WHERE name = 'Create MeterCost Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'MeterCostMasterList') ,(select id FROM eg_feature WHERE name = 'View MeterCost Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyMeterCostMaster') ,(select id FROM eg_feature WHERE name = 'Modify MeterCost Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create MeterCost Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create MeterCost Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify MeterCost Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify MeterCost Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View MeterCost Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View MeterCost Master'));

---


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create ApplicationProces Master','Create ApplicationProces Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View ApplicationProces Master','View ApplicationProces Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify ApplicationProces Master','Modify ApplicationProces Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ApplicationProcessTimeMaster') ,(select id FROM eg_feature WHERE name = 'Create ApplicationProces Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ApplicationProcessTimeList') ,(select id FROM eg_feature WHERE name = 'View ApplicationProces Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyApplicationProcessTime') ,(select id FROM eg_feature WHERE name = 'Modify ApplicationProces Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create ApplicationProces Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create ApplicationProces Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify ApplicationProces Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify ApplicationProces Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View ApplicationProces Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View ApplicationProces Master'));

---

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create DocumentName Master','Create DocumentName Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View DocumentName Master','View DocumentName Master',(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify DocumentName Master','Modify DocumentName Master',(select id from eg_module  where name = 'Water Tax Management')); 


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DocumentNamesMaster') ,(select id FROM eg_feature WHERE name = 'Create DocumentName Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DocumentNamesList') ,(select id FROM eg_feature WHERE name = 'View DocumentName Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyDocumentNamesMaster') ,(select id FROM eg_feature WHERE name = 'Modify DocumentName Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create DocumentName Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create DocumentName Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Modify DocumentName Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify DocumentName Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'View DocumentName Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View DocumentName Master'));

---


INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create ChairPerson Master','Create ChairPerson Master',(select id from eg_module  where name = 'Water Tax Management')); 



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'getchairpersontableajax') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'activechairpersonexistsasoncurrentdate-ajax') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'addchairpersonname-ajax') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ChairPersonDetailsScreen') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Property Administrator') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create ChairPerson Master'));
