-----Transactions Delete for roleaction mapping 
delete from EG_ROLEACTION where roleid =(select id from eg_role where name='Legal Case Administrator') and actionid in (select id from eg_action where name in ('createlegalcase', 'elasticsearchlegalcase', 'editlegalcase', 'viewlegalcase',
'New-Judgment', 'Edit-Judgment', 'New-Hearing', 'Edit-Hearing', 'view-Hearing', 'legalajaxforemployeeposition',
'New-LcInterimOrder', 'View LcInterimOrder', 'List LcInterimOrder', 'Edit LcInterimOrder','New Vacate Stay',
'getAllEmployeeNames', 'AddStandingCouncil', 'legalcaseAdvocateSearch', 'AddCounterAffidavit', 'legalajaxforDepartment',
'legalajaxforposition', 'New-JudgmentImpl', 'New-LCDisposal', 'Edit-LCDisposal', 'legalajaxposition',
'legalcasepopulatePetitionList', 'legalcasepopulateCourtNameList', 'searchlegalcase', 'searchlegalcaseresult') and contextroot = 'lcms' );


-----Reports Delete for roleaction mapping 
delete from EG_ROLEACTION where roleid = (select id from eg_role where name='Legal Case Administrator') 
and actionid in (select id from eg_action where name in ('TimeSeriesReports', 'TimeSeriesReportResult', 'GenericSubReports', 
'GenericSubReportResult', 'DailyBoardReports', 'DailyBoardReportResult', 'LCMSDUEReports', 'CaDueReportform', 
'CaDueReportResult', 'Report_PWR_Due', 'PwrDueReportResult', 'CaDueReportResult', 'judgementImplDueReportResult', 
'judgementImplDueReport', 'employeeHearingDueReport', 'employeehearingDueReportResult', 'DrillDownReport-TimeSeriesReport', 
'DrillDownReport-GenericSubReport') and contextroot = 'lcms' );

------Delete Script for role 
delete from eg_role where name = 'Legal Case Administrator';


---------------Create new Role for LCMS--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) 
VALUES (nextval('seq_eg_role'), 'Legal Case Administrator', 'user has access to all transactional and reports screens', now(), 1, 1, now(), 0);

--Transactions view role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='Legal Case Administrator') 
as roleid, id from eg_action where name in ('createlegalcase', 'elasticsearchlegalcase', 'editlegalcase', 'viewlegalcase', 
'New-Judgment', 'Edit-Judgment', 'New-Hearing', 'Edit-Hearing', 'view-Hearing', 'legalajaxforemployeeposition', 
'New-LcInterimOrder', 'View LcInterimOrder', 'List LcInterimOrder', 'Edit LcInterimOrder','New Vacate Stay', 
'getAllEmployeeNames', 'AddStandingCouncil', 'legalcaseAdvocateSearch', 'AddCounterAffidavit', 'legalajaxforDepartment', 
'legalajaxforposition', 'New-JudgmentImpl', 'New-LCDisposal', 'Edit-LCDisposal', 'legalajaxposition', 
'legalcasepopulatePetitionList', 'legalcasepopulateCourtNameList', 'searchlegalcase', 'searchlegalcaseresult') and contextroot = 'lcms' );


--Reports role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='Legal Case Administrator') 
as roleid, id from eg_action where name in ('TimeSeriesReports', 'TimeSeriesReportResult', 'GenericSubReports', 
'GenericSubReportResult', 'DailyBoardReports', 'DailyBoardReportResult', 'LCMSDUEReports', 'CaDueReportform', 
'CaDueReportResult', 'Report_PWR_Due', 'PwrDueReportResult', 'CaDueReportResult', 'judgementImplDueReportResult', 
'judgementImplDueReport', 'employeeHearingDueReport', 'employeehearingDueReportResult', 'DrillDownReport-TimeSeriesReport', 
'DrillDownReport-GenericSubReport') and contextroot = 'lcms' );

