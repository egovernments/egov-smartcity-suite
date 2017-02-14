Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'waterchargearrearReportResult','/reports/arrear/arrearReport',null,(select id from eg_module
 where name='WaterTaxReports'),6,'Arrear Register Report','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='waterchargearrearReportResult'));