

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BudgetAppropriationRegister'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BudgetAppropriationRegisterResult '));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BudgetAppropriationRegisterXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BudgetAppropriationRegisterPdf'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Budget Variance Report'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajax Load Variance Report'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BudgetVarianceReportPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BudgetVarianceReportXls'));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-ajax search BankBook'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-bankBookReportPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-bankBookReportXls'));
