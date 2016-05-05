Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'DataEntryConnectionReport','/report/dataEntryConnectionReport/search',null,(select id from eg_module
 where name='WaterTaxReports'),6,'Data Entry Records Details Report','true','wtms',0,1,to_timestamp('2015-08-15 11:02:43.968604',
 'null'),1,to_timestamp('2015-08-15 11:02:43.968604','null'),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='DataEntryConnectionReport'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='DataEntryConnectionReport'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'DataEntryConnectionReport-ajax','/report/dataEntryConnectionReport/search/result/',null,(select id from eg_module
 where name='Water Tax Management'),null,'generationofDataEntryConnectionReport','false','wtms',0,1,to_timestamp('2015-08-15 11:02:43.968604',
 'null'),1,to_timestamp('2015-08-15 11:02:43.968604','null'),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='DataEntryConnectionReport-ajax'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Report Viewer'),
(select id from eg_action where name='DataEntryConnectionReport-ajax'));