Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,
version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'ajaxForExistingConsumerCodeFordataEntry','/ajax-consumerCodeExistFordataEntry',
null,(select id from eg_module where name='WaterTaxTransactions'),4,'ajaxForExistingConsumerCodeFordataEntry','false','wtms',0,1,to_timestamp('2015-09-15 10:44:14.929994','null'),1,to_timestamp('2015-09-15 10:44:14.929994','null'),(select id from eg_module where name='Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),
(select id from eg_action where name='ajaxForExistingConsumerCodeFordataEntry'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='ajaxForExistingConsumerCodeFordataEntry'));