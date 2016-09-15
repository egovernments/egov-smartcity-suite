

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxapplicationprocesstime') ,(select id FROM eg_feature WHERE name = 'Create ApplicationProces Master'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'modifyapplicationProcesswithId','/masters/applicationProcessTime/edit/',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'modifyapplicationProcesswithId','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'modifyapplicationProcesswithId') ,(select id FROM eg_feature WHERE name = 'Modify ApplicationProces Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxWaterRatesamster')
 ,(select id FROM eg_feature WHERE name = 'Create WaterRatedNon Master'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxWaterRatesamster')
 ,(select id FROM eg_feature WHERE name = 'Modify WaterRatedNon Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateAdditionalConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));


delete from eg_feature_action where action in(select id from eg_action where  contextroot='wtms' and name='WaterTaxCreateNewConnectionNewForm'
) and feature in(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection');

delete from eg_feature_action where action in(select id from eg_action where  contextroot='wtms' and name='WaterTaxCreateNewConnectionNewForm'
) and feature in(select id FROM eg_feature WHERE name = 'Create WaterTax ChangeOfUseConnection');

update eg_feature set name='Create WaterTax ReConnection' where name='Create WaterTax ReConnConnection'  and module in((select id from eg_module  where name = 'Water Tax Management'));




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createReConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterReConnectionApplication') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'UpdateWaterConnectionApplication') ,(select 
id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create WaterTax ReConnection'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'dataeditentrymessage','/application/newConnection-existingMessage/',null,(select 
id from eg_module where name='WaterTaxTransactions'),1,'dataeditentrymessage','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'dataeditentrymessage') ,
(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax'));



delete from  eg_feature_action where ACTION in(select id FROM eg_action  WHERE name = 'modifydataentryconnectiondetails') and 
feature in(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax');

delete from  eg_feature_role where feature in(select id FROM eg_feature WHERE name = 'DataEntry For WaterTax');

--

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),
'Modify DataEntry For WaterTax','Modify DataEntry For WaterTax',
(select id from eg_module  where name = 'Water Tax Management')); 

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES (
(select id FROM eg_action  WHERE name = 'modifydataentryconnectiondetails') ,
(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'dataeditentrymessage') ,
(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editdatentryForm') ,
(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxForExistingConsumerCodeFordataEntry') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'WaterTaxAjaxCheckPrimaryConnection') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));




INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'editdataEntryDemand') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewWaterConnection') ,(select id FROM eg_feature WHERE name = 'Modify DataEntry For WaterTax'));



Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'donationmastermodifyAction','/masters/donationMaster/edit/',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'donationmastermodifyAction','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'donationmastermodifyAction') ,
(select id FROM eg_feature WHERE name = 'Modify Donation Master'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'maximumpipesizeajax') ,
(select id FROM eg_feature WHERE name = 'Modify Donation Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'minimumpipesizeajax') ,
(select id FROM eg_feature WHERE name = 'Modify Donation Master'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 
'donationheadercombinationajax') ,(select id FROM eg_feature WHERE name = 'Modify Donation Master'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'usageTypeMasterEditAction','/masters/usageTypeMaster/edit/',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'usageTypeMasterEditAction','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'usageTypeMasterEditAction') ,
(select id FROM eg_feature WHERE name = 'Modify UsageTypeMaster'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'pipesizeMasterEditAction','/masters/pipesizeMaster/edit/',null,(select 
id from eg_module where name='WaterTaxMasters'),1,'pipesizeMasterEditAction','false','wtms',
0,1,to_timestamp('2015-08-31 13:33:48.231263','null'),1,to_timestamp('2015-08-31 13:33:48.231263','null'),
(select id from eg_module where name='Water Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'pipesizeMasterEditAction') ,
(select id FROM eg_feature WHERE name = 'Modify PipeSize Master'));



INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateBillForHSCNO') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'generateBill-download') ,(select id FROM eg_feature WHERE name = 'WaterTax Reports Feature'));
