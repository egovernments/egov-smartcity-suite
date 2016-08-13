
------------------ADDING FEATURE--------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Expense Bill','Create Expense Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Expense Bill','View Expense Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Expense Bill','Modify Expense Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Expense Bill','Approve Expense Bill',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Bills','Cancel Bills',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Bill Registers','View Bill Registers',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Journal Voucher','Create Journal Voucher',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Journal Voucher','Modify Journal Voucher',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Journal Voucher','View Journal Voucher',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Journal Voucher','Approve Journal Voucher',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Voucher From Bill','Create Voucher From Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Voucher From Bill','Modify Voucher From Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Voucher From Bill','View Voucher From Bill',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Voucher From Bill','Approve Voucher From Bill',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Direct Bank Payment','Create Direct Bank Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Direct Bank Payment','Modify Direct Bank Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Direct Bank Payment','View Direct Bank Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Direct Bank Payment','Approve Direct Bank Payment',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Bill Payment','Create Bill Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Bill Payment','Modify Bill Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Bill Payment','View Bill Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Bill Payment','Approve Bill Payment',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cheque Assignment','Cheque Assignment',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Surrender/Reassign Cheque','Surrender or Reassign Cheque',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'RTGS Assignment','RTGS Assignment',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Surrender RTGS','Surrender RTGS',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cancel Vouchers','Cancel Vouchers',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Vouchers','View Vouchers',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank To Bank Transfer','Bank To Bank Transfer',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Reconciliation Summary Report','Reconciliation Summary Report',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Statement Entries-Not In Bank Book','Bank Statement Entries-Not In Bank Book',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Reconcile With Bank-Manual','Reconcile With Bank-Manual',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Auto Reconcile-Statement Upload','Auto Reconcile-Statement Upload',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Reconcile Uploaded Statement','Reconcile Uploaded Statement',(select id from eg_module  where name = 'EGF'));

------------------ADDING FEATURE ACTIONS-------------------

-- expense bill
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Expense Bill'), id from eg_action where name  in('ajax-common-entityby20','AjaxApproverDropdown','ajax-common-checklist','Expense Bill-Create','cbill-ajaxprint','ExpenseBillXls','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','ajax-process-getAllCoaCodesExceptCashBank','AjaxMiscReceiptFundSource','AjaxDesignationDropdown', 'ajax-process-function','cbill-print-crud','searchEntries-accountdetail','ExpenseBillPdf','Contingent Bill-View','ContingentBillUpdate','ExpenseBillCreate','ajax-process-coacodes','ajax-common-codesofdetailtype','cbill-print'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Expense Bill'), id from eg_action where name  in('ajax-common-entityby20','AjaxApproverDropdown','ajax-common-checklist','Expense Bill-Create','cbill-ajaxprint','ExpenseBillXls','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','ajax-process-getAllCoaCodesExceptCashBank','AjaxMiscReceiptFundSource','AjaxDesignationDropdown', 'ajax-process-function','cbill-print-crud','searchEntries-accountdetail','ExpenseBillPdf','Contingent Bill-View','ContingentBillUpdate','ExpenseBillCreate','ajax-process-coacodes','ajax-common-codesofdetailtype','cbill-print');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Expense Bill'), id from eg_action where name  in('Contingent Bill-View','cbill-print','cbill-ajaxprint','ExpenseBillPdf','ExpenseBillXls','cbill-print-crud','Bill View-View');    
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Expense Bill'), id from eg_action where name  in('ContingentBillUpdate','Contingent Bill-View','ExpenseBillPdf','ExpenseBillXls','cbill-print','AjaxDesignationDropdown','AjaxApproverDropdown','cbill-ajaxprint','cbill-print-crud');  

--cancel bill
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Cancel Bills'), id from eg_action where name  in('Cancel Bills','CancelBill-cancelBill','Cancel Bill action','CancelBill-search'); 

--view bill registers
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Bill Registers'), id from eg_action where name  in('View Bill Registers-Search','BillRegisterSearch','Bill View-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-print-crud'); 

-- journal voucher
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Journal Voucher'), id from eg_action where name  in('Journal Voucher-Create','ajax-process-coacodes','ajax-process-function','ajax-common-detailcode','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','ajax-common-entityby20','searchEntries-accountdetail','AjaxDesignationDropdown','AjaxApproverDropdown','JournalVoucherCreate','Voucher View','jv-beforemodify','ModifyJVoucher','Print Journal Voucher','pjv-printajax','pjv-printpdf','pjv-printxls','pjv-view','PreApprovedVoucherUpdate','ajax-common-detailtype');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Journal Voucher'), id from eg_action where name  in('Journal Voucher-Create','ajax-process-coacodes','ajax-process-function','ajax-common-detailcode','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','ajax-common-entityby20','searchEntries-accountdetail','AjaxDesignationDropdown','AjaxApproverDropdown','JournalVoucherCreate','Voucher View','jv-beforemodify','ModifyJVoucher','Print Journal Voucher','pjv-printajax','pjv-printpdf','pjv-printxls','pjv-view','PreApprovedVoucherUpdate','ajax-common-detailtype');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Journal Voucher'), id from eg_action where name  in('Contingent Bill-View','cbill-print','cbill-ajaxprint','ExpenseBillPdf','ExpenseBillXls','cbill-print-crud','Voucher View','jv-beforemodify');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Journal Voucher'), id from eg_action where name  in('pjv-view','jv-beforemodify','PreApprovedVoucherUpdate','Print Journal Voucher','pjv-printajax','pjv-printpdf','pjv-printxls','AjaxDesignationDropdown','AjaxApproverDropdown');

-- voucher from bill
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Voucher From Bill'), id from eg_action where name  in('Generate PJV','BillVouchersList','view Voucher','AjaxDesignationDropdown','AjaxApproverDropdown','Contingent Bill-View','cbill-print','cbill-ajaxprint','ExpenseBillPdf','ExpenseBillXls','PreApprovedVoucherSave','pjv-view','PreApprovedVoucherUpdate','Print Journal Voucher','pjv-printpdf','pjv-printxls','pjv-printajax','cbill-print-crud');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Voucher From Bill'), id from eg_action where name  in('Contingent Bill-View','cbill-print','cbill-ajaxprint','ExpenseBillPdf','ExpenseBillXls','cbill-print-crud','Voucher View','jv-beforemodify');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Voucher From Bill'), id from eg_action where name  in('pjv-view','Contingent Bill-View','Print Journal Voucher','pjv-printpdf','pjv-printxls','pjv-printajax','AjaxDesignationDropdown','AjaxApproverDropdown','PreApprovedVoucherUpdate','cbill-print','cbill-ajaxprint','ExpenseBillPdf','ExpenseBillXls','cbill-print-crud');

--Direct bank payment
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Direct Bank Payment'), id from eg_action where name  in('Direct Bank Payments','ajax-process-getAllCoaCodesExceptCashBank','ajax-process-function','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','ajax-common-bankbyfandt','ajax-common-loadbaccount','ajax-common-loadaccnum','ajax-common-accountbalance','ajax-common-detailcode','ajax-common-entityby20','searchEntries-accountdetail','AjaxDesignationDropdown','AjaxApproverDropdown','Direct Bank Payments-Create','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','payment-dbp-viewInboxItem','DirectBankPaymentsendForApproval');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Direct Bank Payment'), id from eg_action where name  in('Direct Bank Payments','ajax-process-getAllCoaCodesExceptCashBank','ajax-process-function','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','ajax-common-bankbyfandt','ajax-common-loadbaccount','ajax-common-loadaccnum','ajax-common-accountbalance','ajax-common-detailcode','ajax-common-entityby20','searchEntries-accountdetail','AjaxDesignationDropdown','AjaxApproverDropdown','Direct Bank Payments-Create','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','payment-dbp-viewInboxItem','DirectBankPaymentsendForApproval');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Direct Bank Payment'), id from eg_action where name  in('payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Direct Bank Payments-View');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Direct Bank Payment'), id from eg_action where name  in('payment-dbp-viewInboxItem','AjaxDesignationDropdown','AjaxApproverDropdown','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','DirectBankPaymentsendForApproval' );

--bill payment
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Bill Payment'), id from eg_action where name  in('Contractor/Supplier Payments','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','Payment Search','Payment Save','ajax-common-loadbaccount','ajax-common-accountbalance','AjaxDesignationDropdown','AjaxApproverDropdown','Payment Create','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Payment sendForApproval','payment-view');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Bill Payment'), id from eg_action where name  in('Contractor/Supplier Payments','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource','Payment Search','Payment Save','ajax-common-loadbaccount','ajax-common-accountbalance','AjaxDesignationDropdown','AjaxApproverDropdown','Payment Create','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Payment sendForApproval','payment-view');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Bill Payment'), id from eg_action where name  in('payment-view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Bill Payment'), id from eg_action where name  in('payment-view','AjaxDesignationDropdown','AjaxApproverDropdown','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Payment sendForApproval');

--Cheque assignment
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Cheque Assignment'), id from eg_action where name  in('Cheque Assignment','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','AjaxMiscReceiptFundSource','Ajax-LoadBanksWithApprovedPayments','Ajax-LoadBankAccountsWithApprovedPayments','ChequeAssignmentSearch','ChequeAssignment-yearCode','ChequeAssignmentCreate','chequeFormatPrint','ajax-common-validatecheque');

--surrender/reassign cheque
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Surrender/Reassign Cheque'), id from eg_action where name  in('Surrender/Reassign Cheque','ajax-common-bankcheqassigned','SearchChequesForSurrender','ChequeAssignment-yearCode','ChequeAssignmentSave','Voucher View','Direct Bank Payments-View','payment-view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint');

--RTGS assignment
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'RTGS Assignment'), id from eg_action where name  in('chequeAssignmentForRTGS','Load all bank across fund','Ajax-LoadBankAccountsWithApprovedPayments','ChequeAssignmentSearchRTGS','Voucher View','payment-view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Direct Bank Payments-View','ChequeAssignmentUpdate','bankAdviceReportExportPDF');

--surrender RTGS
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Surrender RTGS'), id from eg_action where name  in('SurrenderRTGS','ajax-common-bankcheqassigned','SurrenderRTGSSearch','Voucher View','payment-view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Direct Bank Payments-View','ChequeAssignmentSave','ChequeAssignment-yearCode');

--cancel vouchers
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Cancel Vouchers'), id from eg_action where name  in('cancelVoucherSearch','Cancellation of  Vouchers-Search','Voucher View','Cancel Selected Vouchers','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','payment-view','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls', 'cbill-ajaxprint');

--view vouchers
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Vouchers'), id from eg_action where name  in('View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Contingent Bill-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View');

--bank to bank transfer
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Bank To Bank Transfer'), id from eg_action where name  in('Bank to Bank Transfer','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','Ajax-load Banks','AjaxMiscReceiptFundSource','ajax-common-loadaccnumber','ajax-common-loadaccnum','ajax-common-accountbalance','ContraBTBCreate');

--reconciliation summary report
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Reconciliation Summary Report'), id from eg_action where name  in('Reconciliation Summary-New','common-bankbranches','common-bankaccounts','Reconciliation Summary-summary');

--bank statement entries-not in bank book
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Bank Statement Entries-Not In Bank Book'), id from eg_action where name  in('BankEntriesNotInBankBookNewForm','AjaxMiscReceiptScheme','AjaxMiscReceiptSubScheme','AjaxMiscReceiptFundSource',
'ajaxLoadAllBanksByFund','ajaxLoadBankBranchFromBank','common-bankaccounts','BankEntriesNotInBankBookList','ajax-common-detailcode','BankEntriesNotInBankBookSave');

--Reconcile With Bank Manual
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Reconcile With Bank-Manual'), id from eg_action where name  in('Reconcile with Bank','common-bankbranches','common-bankaccounts','Reconcile with Bank ',' Reconcile with Bank','brs pending balance');

--Auto Reconcile-Statement Upload
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Auto Reconcile-Statement Upload'), id from eg_action where name  in('AutoReconcile-Upload','common-bankbranches','common-bankaccounts','autoReconciliation-crud');

--Reconcile Uploaded Statement
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Reconcile Uploaded Statement'), id from eg_action where name  in('AutoReconcile','common-bankbranches','common-bankaccounts','autoReconciliation-schedule','autoReconciliation-generateReport','autoReconciliation-generateXLS','autoReconciliation-generatePDF');


------------------ADDING FEATURE ROLES--------------------

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Expense Bill'),id from eg_role where name in('Super User','Bill Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Expense Bill'),id from eg_role where name in('Super User','Bill Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Expense Bill'),id from eg_role where name in('Super User','Bill Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Expense Bill'),id from eg_role where name in('Super User','Bill Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Cancel Bills'),id from eg_role where name in('Super User','Bill Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Bill Registers'),id from eg_role where name in('Super User','Bill Creator','Bill Approver','Voucher Creator','Voucher Approver','Payment Creator','Payment Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Journal Voucher'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Journal Voucher'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Journal Voucher'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Journal Voucher'),id from eg_role where name in('Super User','Voucher Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Voucher From Bill'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Voucher From Bill'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Voucher From Bill'),id from eg_role where name in('Super User','Voucher Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Voucher From Bill'),id from eg_role where name in('Super User','Voucher Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Direct Bank Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Direct Bank Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Direct Bank Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Direct Bank Payment'),id from eg_role where name in('Super User','Payment Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Bill Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Bill Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Bill Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Bill Payment'),id from eg_role where name in('Super User','Payment Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Cheque Assignment'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Surrender/Reassign Cheque'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'RTGS Assignment'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Surrender RTGS'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Cancel Vouchers'),id from eg_role where name in('Super User','Voucher Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Vouchers'),id from eg_role where name in('Super User','Voucher Creator','Voucher Approver','Payment Creator','Payment Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Bank To Bank Transfer'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Reconciliation Summary Report'),id from eg_role where name in('Super User','Bank Reconciler');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Bank Statement Entries-Not In Bank Book'),id from eg_role where name in('Super User');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Reconcile With Bank-Manual'),id from eg_role where name in('Super User','Bank Reconciler');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Auto Reconcile-Statement Upload'),id from eg_role where name in('Super User','Bank Reconciler');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Reconcile Uploaded Statement'),id from eg_role where name in('Super User','Bank Reconciler');
