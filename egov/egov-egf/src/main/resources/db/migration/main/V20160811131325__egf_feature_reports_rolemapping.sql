
------------------ADDING FEATURE--------------------------

--REPORTS
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Income-Expenditures Statements','Income-Expenditures Statements',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Balance Sheet','Balance Sheet',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Trial Balance','Trial Balance',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Book','Bank Book',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sub Ledger Report','Sub Ledger Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Journal Book Report','Journal Book Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'General Ledger Report','General Ledger Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Opening Balance Report','Opening Balance Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Sub Ledger Schedule Report','Sub Ledger Schedule Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Day Book Report','Day Book Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cheque Issue Register','Cheque Issue Register',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Complete Bill Register Report','Complete Bill Register Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Voucher Status Report','Voucher Status Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Advice For RTGS Payment','Bank Advice For RTGS Payment',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'RTGS Register Report','RTGS Register Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Deduction Detailed Report','Deduction Detailed Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Deductions Remittance Summary','Deductions Remittance Summary',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Departmentwise Budget','Departmentwise Budget',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Functionwise Budget','Functionwise Budget',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Budget Appropriation Register','Budget Appropriation Register',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Budget Additional Appropriation','Budget Additional Appropriation',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Budget Variance Report','Budget Variance Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Budget Upload Report','Budget Upload Report',(select id from eg_module  where name = 'EGF'));
------------------ADDING FEATURE ACTIONS-------------------

--FINANCIAL STATEMENTS
-- Income-Expenditures Statements
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Income-Expenditures Statements'), id from eg_action where name  in('Income - Expenditures Statement','Report-ajaxPrintIncomeExpenditureReport','Report-generateIncomeExpenditurePdf','Report-generateIncomeExpenditureXls','exil-report-genIESubR','GeneralLedgerReport-searchDrilldown','Report-generateIncomeExpenditureSchedulePDF','Report-generateIncomeExpenditureScheduleXls','Voucher View','Report-View Schedule Income expenditure','Report-generateIncomeExpenditureSubReportPdf','Report-generateIncomeExpenditureSubReportXls','report-ie-detailedschedule','report-ie-detailedschedulepdf','report-ie-detailedschedulexls','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Balance sheet
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Balance Sheet'), id from eg_action where name  in('Balance Sheet','BalanceSheetReport-print','exil-BSreport-BSXls','exil-BSreport-BSpdf','report-balancesheetsubreport-showdropdown','exil-BSreport-BSSXls','exil-BSreport-BSSpdf','GeneralLedgerReport-searchDrilldown','report-balancesheet-showdropdown','exil-BSreport-genSchepdf','exil-BSreport-genScheXls','report-bs-detailedschedule','report-bs-detailedschedulepdf','report-bs-detailedschedulexls','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--ACCOUNTING RECORDS
--Trial balance
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Trial Balance'), id from eg_action where name  in('TrialBalance','TrialBalanceSearch','GeneralLedgerReport-searchDrilldown','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Bank book
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Bank Book'), id from eg_action where name  in('Bank Book','Load all bank across fund','ajax-common-loadaccnumber','Report-ajax search BankBook','Report-bankBookReportPdf','Report-bankBookReportXls','View Vouchers','BankBookReport-showChequeDetails','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Sub ledger report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Sub Ledger Report'), id from eg_action where name  in('SubLedger-Search','ajaxLoadSLreportCodes','ajaxLoadSubLedgerTypesByGlCode','ajax-common-entityby20','SubLedgerName-View','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Journal book report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Journal Book Report'), id from eg_action where name  in('JournalBook-Search','JournalBookReport-ajaxSearch','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--General ledger report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'General Ledger Report'), id from eg_action where name  in('GeneralLedger-Search','ajaxLoadGLreportCodes','ajaxLoadFunctionCodes','GeneralLedger-ajaxSearch','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Opening Balance Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Opening Balance Report'), id from eg_action where name  in('OpeningBalance-Search','OpeningBalanceReport-ajaxSearch');

--Sub Ledger Schedule Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Sub Ledger Schedule Report'), id from eg_action where name  in('SubLedgerSchedule-View','SubLedgerSchedule-Search','ajaxLoadSLreportCodes','ajaxLoadSubLedgerTypesByGlCode','SubLedgerScheduleReport-ajaxSearch','SubLedgerName-View');

--Day Book Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Day Book Report'), id from eg_action where name  in('DayBook-Search','DayBook-View','DayBookReport-ajaxSearch','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--MIS REPORTS
--Cheque issue register
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Cheque Issue Register'), id from eg_action where name  in('Cheque Issue Register','ajax-common-accnum','Load chequeIssueRegisterReport','Voucher View','Report-chequeIssueRegisterPDF','Report-chequeIssueRegisterXLS','chequeFormatPrint','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Direct Bank Payments-View','payment-view','Remittance recovery view');

--Complete Bill Register Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Complete Bill Register Report'), id from eg_action where name  in('complete Bill Register Report','BillRegisterReportBillSearch');

--Voucher Status Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Voucher Status Report'), id from eg_action where name  in('Voucher Status Report','VoucherStatusReportSearch','VoucherStatusReportGeneratePdf','VoucherStatusReportGenerateXls','View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View','cbill-print-crud');

--Bank Advice For RTGS Payment
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Bank Advice For RTGS Payment'), id from eg_action where name  in('bankAdviceReportExportPDF','BankAdviceReportforRTGSBankPayment','BankAdviceReport-search','ajaxLoadBankBranchFromBank','ajaxLoadBankAccFromBranch','ajaxLoadRTGSChequeFromBankAcc');

--RTGS Register Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'RTGS Register Report'), id from eg_action where name  in('RTGSIssueRegisterReport','RtgsIssueRegisterReport-exportHtml','RtgsIssueRegisterReport-exportPdf','RtgsIssueRegisterReport-exportXls','ajaxLoadAllBanksByFund','ajaxLoadBankBranchFromBank','ajaxLoadBankAccFromBranch');

--REMITTANCE REPORTS
--Deduction Detailed Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Deduction Detailed Report'), id from eg_action where name  in('Pending TDS','ajax-report-tds-loadEntity','Report-pendingTDSReport','Report-pendingTDS_Pdf','Report-pendingTDS_Xls');

--Deductions Remittance Summary
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Deductions Remittance Summary'), id from eg_action where name  in('TDS Summary','ajax-report-tds-loadEntity','ajax pendingTDSReport','Report-TDSexportSummaryPdf','Report-TDSexportSummaryXls');

--BUDGET REPORTS
--Departmentwise Budget
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Departmentwise Budget'), id from eg_action where name  in('Departmentwise','Department Wise Print','Department Wise Html','Department Wise Pdf','Department Wise Xls');

--Functionwise Budget
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Functionwise Budget'), id from eg_action where name  in('Functionwise','Create Budget Report','Functionwise ',' Functionwise',' Functionwise ');

--Budget Appropriation Register
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Budget Appropriation Register'), id from eg_action where name  in('BudgetAppropriationRegister','Report-BudgetAppropriationRegisterResult ','Report-BudgetAppropriationRegisterPdf','Report-BudgetAppropriationRegisterXls');

--Budget Additional Appropriation
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Budget Additional Appropriation'), id from eg_action where name  in('Budget Additional Appropriation','Generate budget Approprition Report','Generate budget Approprition Pdf','Generate budget Approprition Xls');

--Budget Variance Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Budget Variance Report'), id from eg_action where name  in('Budget Variance Report','ajax Load Variance Report','Report-BudgetVarianceReportPdf','Report-BudgetVarianceReportXls');

--Budget Upload Report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Budget Upload Report'), id from eg_action where name  in('Search Result-BudgetUploadReport','Search BudgetUploadReport','BudgetUploadReport-getReferenceBudget');

------------------ADDING FEATURE ROLES--------------------

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Income-Expenditures Statements'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Balance Sheet'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Trial Balance'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Bank Book'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Sub Ledger Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Journal Book Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'General Ledger Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Opening Balance Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Sub Ledger Schedule Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Day Book Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Cheque Issue Register'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Complete Bill Register Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Voucher Status Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Bank Advice For RTGS Payment'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'RTGS Register Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Deduction Detailed Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Deductions Remittance Summary'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Departmentwise Budget'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Functionwise Budget'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Budget Appropriation Register'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Budget Additional Appropriation'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Budget Variance Report'),id from eg_role where name in('Super User','Financial Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Budget Upload Report'),id from eg_role where name in('Super User','Financial Report Viewer');
