--Insert into eg_action 
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'TimeSeriesReports','/timeseriesreports/timeSeriesReport',(select id from eg_module 
where name='LCMS Reports'),1,'Time Series Report',true,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='TimeSeriesReports'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'TimeSeriesReportResult','/timeseriesreports/timeSeriesReportresults',(select id from eg_module 
where name='LCMS Reports'),1,'TimeSeriesReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='TimeSeriesReportResult'));