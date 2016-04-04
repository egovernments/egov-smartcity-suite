---------------Actions for Voucher Creator----------------------------------

Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='LOGIN'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='JournalVoucherCreate'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Print Journal Voucher'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Cancellation of  Vouchers-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Journal Voucher-Create'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='View Journal Vouchers-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank to Bank Transfer-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank Transaction-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank to Bank Transfer-Modify'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank Transaction-Modify'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank to Bank Transfer-Reverse'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Bank Transaction-Reverse'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='voucherSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-detailcode'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-detailtype'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-entityby20'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-bankcheqassigned'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-validatecheque'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-accnum'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='jv-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='jv-print-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='jv-beforemodify'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Cancel Selected Vouchers'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Cancel Voucher Search'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='cbill-pjv-print'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='pjv-view'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='pjv-printajax'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='cbill-pjv-ajaxprint'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='pjv-printpdf'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='pjv-printxls'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='searchEntries-accountdetail'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-loaduser'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='createVoucher'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-nextdesig'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-process-coacodes'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-process-function'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-common-loadName'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ShowFinancialWorkflowHistory'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='rev-voucher-link'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='ajax-process-getAllCoaCodesExceptCashBank'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='exil-submit data'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='exil-GetList_ShowMode'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='exil-GetList_serviceID'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Voucher View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='Contingent Bill-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='View Vouchers'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Creator'),(select id from eg_action where name='JournalVoucherSearch'));

-------------------------------------------------

-------------------Actions for Voucher Approver------------------------------
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='LOGIN'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Print Journal Voucher'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Direct Bank Payments-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Contingent Bill-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Voucher View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-detailcode'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-detailtype'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-entityby20'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-bankcheqassigned'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='cbill-print-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='searchEntries-accountdetail'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-loaduser'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-process-coacodes'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-process-function'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-loadName'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ShowFinancialWorkflowHistory'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Remittancerecovry-inbox'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='exp-JVprint'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Remittance recovery view'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Bill View Action'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='Bill View-View'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='transaction-billVoucher'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='DishonoredCheques-inbox'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='DishonorChequeWorkflow'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='cbill-pjv-print'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='pjv-view'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='pjv-printajax'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='cbill-pjv-ajaxprint'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='pjv-printpdf'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='pjv-printxls'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='View Vouchers'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='JournalVoucherSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='jv-beforemodify'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='ajax-common-nextdesig'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='AjaxDesignationDropdown'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='AjaxDesignationDropdown'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='createVoucher'));
Insert into eg_roleaction values((select id from eg_role where name='Voucher Approver'),(select id from eg_action where name='PreApprovedVoucherUpdate'));

-------------------------------------------------------------------------------------------

-----------------------Actions for Payment Creator----------------------

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Direct Bank Payments-View'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Direct Bank Payments-Modify'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Direct Bank Payments-Reverse'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Direct Bank Payments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='View Payments-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='View Payments- Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Reverse Payments-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Reverse Payments- Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='PaymentModify'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-viewInboxItems'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-dbp-viewInboxItem'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-print'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-view'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Ajax-Bank Account Based on Type'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Ajax-LoadBanksWithApprovedPayments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Ajax-LoadBankAccountsWithApprovedPayments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='Ajax-LoadDocumentNoAndDate'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-loadaccnumber'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-reversepayment'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-loadbaccount'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-voucherprint'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='payment-voucherAjaxprint'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-bankaccwithapprsalary'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-bankbyfandt'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-loadaccnum'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajax-common-accountbalance'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='View Vouchers'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='JournalVoucherSearch'));

---------------------------------------------------------------

--------------------------Actions for Payment Approver--------------------
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Direct Bank Payments-View'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='View Payments-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='View Payments- Search'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-viewInboxItems'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-dbp-viewInboxItem'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-print'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-view'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Ajax-Bank Account Based on Type'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Ajax-LoadBanksWithApprovedPayments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Ajax-LoadBankAccountsWithApprovedPayments'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='Ajax-LoadDocumentNoAndDate'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-loadaccnumber'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-loadbaccount'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-voucherprint'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='payment-voucherAjaxprint'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-bankaccwithapprsalary'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-bankbyfandt'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-loadaccnum'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ajax-common-accountbalance'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='View Vouchers'));
Insert into eg_roleaction values((select id from eg_role where name='Payment Approver'),(select id from eg_action where name='JournalVoucherSearch'));

-----------------------------------------------------

-----------------------Actions for Report Viewer----------------------
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Income - Expenditures Statement'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Receipt/Payment Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Balance Sheet'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Trial Balance'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Cash Book'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Bank Book'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Journal Book'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='General Ledger'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Sub-Ledger'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Day Book'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedger Schedule'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Opening Balance Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Cheque-in-hand Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Cheques Received'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Cheque Issue Register'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Dishonored Cheques Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Bill Register Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='voucherSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Voucher Status Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='GeneralLedger-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='GeneralLedger-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadGLreportCodes'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadFunctionCodes'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedgerSchedule-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedgerSchedule-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedgerName-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedger-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedger-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadSLreportCodes'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadSubLedgerTypesByGlCode'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='JournalBook-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='DayBook-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='DayBook-View'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='JournalBook-Search'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BankBookReport-showChequeDetails'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='GeneralLedger-ajaxSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='DayBookReport-ajaxSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='JournalBookReport-ajaxSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='SubLedgerScheduleReport-ajaxSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='OpeningBalanceReport-ajaxSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='VoucherStatusReportSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='VoucherStatusReportGeneratePdf'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='VoucherStatusReportGenerateXls'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='complete Bill Register Report'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BillRegisterReportBillSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='Report-BillregisterReprotSearch'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='TrialBalance'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='TrialBalance-crud'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='TrialBalanceSearch'));

-------------------------------------------------------------------



