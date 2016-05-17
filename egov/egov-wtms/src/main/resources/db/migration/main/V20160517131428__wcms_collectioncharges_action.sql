delete from eg_roleaction where actionid = (select id from eg_action where name ='WaterTaxConnectionDataEntry-edit' and contextroot='wtms');
delete from eg_action where name ='WaterTaxConnectionDataEntry-edit' and contextroot='wtms';

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'Collect Water Charges','/search/waterSearch/','applicationType=COLLECTTAX',(select id from eg_module where name='Billbased Services'),null,'Collect Water Charges',true,'wtms',
null,1,to_timestamp('2015-08-19 10:45:51.291494','null'),1,to_timestamp('2015-08-19 10:45:51.291494','null'),(select id from eg_module where name='Collection'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Collect Water Charges' and contextroot='wtms'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='Collect Water Charges' and contextroot='wtms'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='Collect Water Charges' and contextroot='wtms'));