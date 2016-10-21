Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'LCMSDUEReports','/reports/dueReport',(select id from eg_module 
 where name='LCMS Reports'),1,'Reports Between Due Dates',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='LCMSDUEReports'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'Report_PWR_Due','/reports/pwrDueReport',(select id from eg_module 
 where name='LCMS Reports'),1,'Report on PWR Due',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Report_PWR_Due'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'PwrDueReportResult','/reports/pwrDueReportResult',(select id from eg_module 
 where name='LCMS Reports'),1,'PwrDueReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='PwrDueReportResult'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'CaDueReportform','/reports/caDueReport',(select id from eg_module 
 where name='LCMS Reports'),1,'CaDueReportform',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='CaDueReportform'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'CaDueReportResult','/reports/caDueReportResult',(select id from eg_module 
 where name='LCMS Reports'),1,'CaDueReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='CaDueReportResult'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'judgementImplDueReportResult','/reports/judgementImplDueReportResult',(select id from eg_module 
 where name='LCMS Reports'),1,'judgementImplDueReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='judgementImplDueReportResult'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'employeehearingDueReportResult','/reports/employeehearingDueReportResult',(select id from eg_module 
 where name='LCMS Reports'),1,'employeehearingDueReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='employeehearingDueReportResult'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'judgementImplDueReport','/reports/judgementImplDueReport',(select id from eg_module 
 where name='LCMS Reports'),1,'judgementImplDueReport',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='judgementImplDueReport'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'employeeHearingDueReport','/reports/employeeHearingDueReport',(select id from eg_module 
 where name='LCMS Reports'),1,'employeeHearingDueReport',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='employeeHearingDueReport'));





