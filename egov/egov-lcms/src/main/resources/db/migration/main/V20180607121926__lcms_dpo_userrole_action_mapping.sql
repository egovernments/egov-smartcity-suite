--Search and  view role-actions
delete from EG_ROLEACTION where roleid =(select id from eg_role where name='LCMS VIEW ACCESS') and actionid in (select id from eg_action where name in ('elasticsearchlegalcase','viewlegalcase','searchlegalcase','searchlegalcaseresult') and contextroot = 'lcms');

-----Reports Delete for roleaction mapping 
delete from EG_ROLEACTION where roleid = (select id from eg_role where name='LCMS VIEW ACCESS') and actionid in (select id from eg_action where name in ('TimeSeriesReports', 'TimeSeriesReportResult', 'GenericSubReports', 
'GenericSubReportResult', 'DailyBoardReports', 'DailyBoardReportResult', 'LCMSDUEReports', 'CaDueReportform', 
'CaDueReportResult', 'Report_PWR_Due', 'PwrDueReportResult', 'CaDueReportResult', 'judgementImplDueReportResult', 
'judgementImplDueReport', 'employeeHearingDueReport', 'employeehearingDueReportResult', 'DrillDownReport-TimeSeriesReport', 
'DrillDownReport-GenericSubReport') and contextroot = 'lcms' );

------Delete Script for role 
delete from eg_role where name = 'LCMS VIEW ACCESS';

---Inserting role
INSERT INTO eg_role(id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,internal) values(nextval('seq_eg_role'),'LCMS VIEW ACCESS','This role has view access to view legalcase and all reports',now(),(select id from eg_user where lower(username) ='system' and type='SYSTEM'),(select id from eg_user where lower(username) ='system' and type='SYSTEM'),now(),0,false); 


--Transactions view role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='LCMS VIEW ACCESS') 
as roleid, id from eg_action where name in ('elasticsearchlegalcase', 'viewlegalcase', 'searchlegalcase', 'searchlegalcaseresult') and contextroot = 'lcms' );


--Reports role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='LCMS VIEW ACCESS') 
as roleid, id from eg_action where name in ('TimeSeriesReports', 'TimeSeriesReportResult', 'GenericSubReports', 
'GenericSubReportResult', 'DailyBoardReports', 'DailyBoardReportResult', 'LCMSDUEReports', 'CaDueReportform', 
'CaDueReportResult', 'Report_PWR_Due', 'PwrDueReportResult', 'CaDueReportResult', 'judgementImplDueReportResult', 
'judgementImplDueReport', 'employeeHearingDueReport', 'employeehearingDueReportResult', 'DrillDownReport-TimeSeriesReport', 
'DrillDownReport-GenericSubReport') and contextroot = 'lcms' );
