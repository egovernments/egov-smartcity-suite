---Insert into eg_action for time series report
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'DrillDownReport-TimeSeriesReport','/timeseriesreports/drilldownreportresult',(select id from eg_module 
where name='LCMS Reports'),1,'DrillDownReport-TimeSeriesReport',false,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='DrillDownReport-TimeSeriesReport'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DrillDownReport-TimeSeriesReport') ,
(select id FROM eg_feature WHERE name = 'Time Series Report'));


----Generic sub report

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'DrillDownReport-GenericSubReport','/reports/genericdrilldownreportresults',(select id from eg_module 
where name='LCMS Reports'),1,'DrillDownReport-TimeSeriesReport',false,'lcms',(select id from eg_module where name='LCMS' 
and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='DrillDownReport-GenericSubReport'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DrillDownReport-GenericSubReport') ,
(select id FROM eg_feature WHERE name = 'Generic Sub Reports'));