---reports in menu tree
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
VALUES (nextval('SEQ_EG_MODULE'), 'LCMS Reports', true, NULL, (select id from eg_module where name='LCMS'), 'Reports', 6);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'DailyBoardReports','/reports/dailyBoardReport',(select id from eg_module 
 where name='LCMS Reports'),1,'Daily Board Reports',true,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='DailyBoardReports'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'DailyBoardReportResult','/reports/dailyBoardReportresults',(select id from eg_module 
 where name='LCMS Reports'),1,'DailyBoardReportResult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='DailyBoardReportResult'));
