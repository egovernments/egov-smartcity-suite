Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application)values
 (nextval('SEQ_EG_ACTION'),'DocumentNamesList','/wtms/masters/documentNamesMaster/list',null,
 (select id from eg_module where name='WaterTaxMasters'),1,'DocumentNamesList','false','wtms',0,1,now(),1,now(),
 (select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='DocumentNamesList' and contextroot='wtms'));