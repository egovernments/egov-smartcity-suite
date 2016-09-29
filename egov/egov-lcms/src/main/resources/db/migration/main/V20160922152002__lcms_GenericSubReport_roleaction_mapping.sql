--Insert into eg_action 
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'GenericSubReports','/reports/genericSubReport',(select id from eg_module 
 where name='LCMS Reports'),1,'Generic Sub Reports',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='GenericSubReports'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'GenericSubReportResult','/reports/genericSubResult',(select id from eg_module 
 where name='LCMS Reports'),1,'GenericSubReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='GenericSubReportResult'));