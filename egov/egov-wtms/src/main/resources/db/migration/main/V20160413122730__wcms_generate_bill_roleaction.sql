Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'GenerateBillReport','/report/generateBill/search',null,(select id from eg_module
 where name='WaterTaxReports'),6,'Generate Bill','true','wtms',0,1,to_timestamp('2015-08-15 11:02:43.968604',
 'null'),1,to_timestamp('2015-08-15 11:02:43.968604','null'),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='GenerateBillReport'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select ID from EG_ACTION where name='GenerateBillReport'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'GenerateBillReport-ajax','/report/generateBill/search/result',null,(select id from eg_module
 where name='Water Tax Management'),null,'generationofbill','false','wtms',0,1,to_timestamp('2015-08-15 11:02:43.968604',
 'null'),1,to_timestamp('2015-08-15 11:02:43.968604','null'),(select id from eg_module where name='Water Tax Management'));
 
 Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select ID from EG_ACTION where name='GenerateBillReport-ajax'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='GenerateBillReport-ajax'));
