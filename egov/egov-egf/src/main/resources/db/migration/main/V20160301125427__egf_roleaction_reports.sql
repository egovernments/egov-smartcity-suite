

INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Load chequeIssueRegisterReport'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-chequeIssueRegisterPDF'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-chequeIssueRegisterXLS'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajax-common-accnum'));




INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-View Schedule Income expenditure'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-ajaxPrintIncomeExpenditureReport'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditureSubReportPdf'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditureSubReportXls'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditureSchedulePDF'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditureScheduleXls'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditurePdf'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-generateIncomeExpenditureXls'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-report-genIESubR'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='exil-report-genIEDet'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-ie-detailedschedule'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-ie-detailedschedulepdf'));
INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='report-ie-detailedschedulexls'));




