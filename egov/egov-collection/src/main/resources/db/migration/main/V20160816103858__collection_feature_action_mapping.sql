------------------------------------ADDING FEATURE STARTS------------------------
--Masters
--Service Category
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Service Category','Create a Service Category',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Service Category','Modify a Service Category',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Service Category','View a Service Category',(select id from eg_module where name='Collection'));
--Service Details
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Service Details','Create a Service Details',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Service Details','Modify a Service Details',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Service Details','View a Service Details',(select id from eg_module where name='Collection'));
--Service To Bank Mapping
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Service To Bank Mapping','Create a Service To Bank Mapping',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Service To Bank Mapping','Modify a Service To Bank Mapping',(select id from eg_module where name='Collection'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Service To Bank Mapping','View a Service To Bank Mapping',(select id from eg_module where name='Collection'));

--Transactions
--Miscelleneous Receipts
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Miscelleneous Receipts','Create Miscelleneous Receipts',(select id from eg_module where name='Collection'));
--Challan Receipt
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Challan Receipt','Challan Receipt',(select id from eg_module where name='Collection'));
--Search Receipts
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Receipts','Search Receipts',(select id from eg_module where name='Collection'));
--Create Challan
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Challan','Create Challan',(select id from eg_module where name='Collection'));
--Search Challans
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Challan','Search Challan',(select id from eg_module where name='Collection'));
--Search Online Receipts 
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Online Receipts','Search Online Receipts',(select id from eg_module where name='Collection'));
--Dishonored Cheque
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Dishonored Cheque','Create Dishonored Cheque',(select id from eg_module where name='Collection'));
--Bank Remittance
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Remittance','Perform Bank Remittance',(select id from eg_module where name='Collection'));
--Property Tax
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Property Tax Collection','Property Tax Collection',(select id from eg_module where name='Collection'));
--Property Mutation
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Mutation Fee Payment','Mutation Fee Payment',(select id from eg_module where name='Collection'));
--Collect Water Charges
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collect Water Charges','Collect Water Charges',(select id from eg_module where name='Collection'));

--Reports
--Cash Collection
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cash Collection','Cash Collection Report',(select id from eg_module where name='Collection'));
--Cheque Collection
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cheque Collection','Cheque Collection Report',(select id from eg_module where name='Collection'));
--Collection Summary 
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collection Summary','Collection Summary Report',(select id from eg_module where name='Collection'));
--Collection Summary-HeadWise 
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Collection Summary-HeadWise Report','Collection Summary-HeadWise Report',(select id from eg_module where name='Collection'));
--Receipt Register
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Receipt Register Report','Receipt Register Report',(select id from eg_module where name='Collection'));
--Bank Remittance Statement
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Remittance Statement','Bank Remittance Statement Report',(select id from eg_module where name='Collection'));
--Remittance voucher
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Remittance voucher Report','Remittance voucher Report',(select id from eg_module where name='Collection'));
--Online Transaction Report
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Online Transaction Report','Online Transaction Report',(select id from eg_module where name='Collection'));
--Dishonored Cheque Report
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Dishonored Cheque Report','Dishonored Cheque Report',(select id from eg_module where name='Collection'));
------------------------------------ADDING FEATURE ENDS------------------------
------------------------------------ADDING FEATURE ACTION STARTS------------------------
--Masters
--Service Category
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategory') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryList') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryEdit') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategorySave') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryCreate') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategory') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryList') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryEdit') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategorySave') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategory') ,(select id FROM eg_feature WHERE name = 'View Service Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceCategoryList') ,(select id FROM eg_feature WHERE name = 'View Service Category'));

--Service Details
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetails') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsCreate') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsList') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsView') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeCreate') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsView') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsModify') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeModify') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsSchemeList') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceLoadScheme') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceLoadSubScheme') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceCodeUniqueCheck') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxMiscReceiptFundSource') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-function') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-coacodes') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetails') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsList') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsView') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeCreate') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsView') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsModify') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeModify') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsSchemeList') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceLoadScheme') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceLoadSubScheme') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceCodeUniqueCheck') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxMiscReceiptFundSource') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-function') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-coacodes') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetails') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsList') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceDetailsView') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-coacodes') ,(select id FROM eg_feature WHERE name = 'View Service Details'));

----Service To Bank Mapping
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceToBankMapping') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingCreate') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'serviceListNotMappedToAccount') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankBranchsByBankForReceiptPayments') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankAccountByBankBranch') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceToBankMapping') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingList') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingSearch') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingEdit') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingSave') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'serviceListNotMappedToAccount') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankBranchsByBankForReceiptPayments') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankAccountByBankBranch') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Modify  Service To Bank Mapping'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingList') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceTypeToBankAccountMappingSearch') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankBranchsByBankForReceiptPayments') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'bankAccountByBankBranch') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));

--Reports
--Cash Collection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CashCollectionSubmissionReport') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CashCollectionReportResult') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CashCollectionReport') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
--Cheque Collection
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ChequeCollectionSubmissionReport') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ChequeCollectionReportResult') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ChequeCollectionReport') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
--Bank Remittance Statement Report 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittanceStatementReport') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittanceStatementReportResult') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittancePrintBankChallanReport') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCollectionsBankAccount') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
--Online Transaction Report
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReport') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReportResult') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
-- Remittance voucher report
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittanceVoucherReport') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittanceVoucherReportResult') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxAccountListOfService') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ServiceListOfAccount') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
--Receipt Register Report
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ReceiptRegisterReport') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ReceiptRegisterReportResult') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
--Dishonored Cheque Report
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeReport') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeCreateReport') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
--Online Transaction Report 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReport') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'OnlineTransactionReportResult') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
--Collection Summary-HeadWise 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionSummaryHeadWiseReport') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionSummaryHeadWiseReportResult') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
--Transaction
--Search Challan
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallanReset') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewChallan') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallanSearch') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallan') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PrintChallan') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxLoadServiceByCategoryForChallan') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
--Search Receipt
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchReceipts') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchReceiptReset') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchReceiptSearch') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewReceipts') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CancelReceipt') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveOnCancelReceipt') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PrintReceipts') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
--Search Online Receipt
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOnlineReceipts') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOnlineReceiptReset') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchOnlineReceiptsSearch') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
--Miscelleneous Receipts
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateMiscReceipts') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptAccountDetailTypeService') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinMiscByService') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveReceipt') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxLoadServiceByCategoryForMisc') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
--Create Challan
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CreateChallan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxChallanApproverDesignation') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxChallanApproverPosition') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveChallan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'PrintChallan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ViewChallan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Challan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinAccDtlsByService') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxFinSubledgerByService') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax-process-coacodes') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxLoadServiceByCategoryForChallan') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
--Challan Receipt 
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ChallanReceipt') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SaveOrUpdateReceipt') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallan') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallanSearch') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxServiceByCategory') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchChallanReset') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
--DisHonored Cheque
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredCheque') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeAccountNo') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeList') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DishonoredChequeSubmit') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
--Bank Remittance
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'BankRemittance') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxAccountListOfService') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'BankRemittanceListData') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'BankRemittanceCreate') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RemittancePrintBankChallanReport') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
--Bill Services
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Property Tax Collection') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionBillingStub') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Mutation Fee Payment') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionBillingStub') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Collect Water Charges') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CollectionBillingStub') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));


------------------------------------ADDING FEATURE ACTION ENDS------------------------ 
------------------------------------ADDING FEATURE ROLE STARTS------------------------
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));



INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Remitter') ,(select id FROM eg_feature WHERE name = 'Bank Remittance'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Miscelleneous Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Challan Receipt'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Property Tax Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Mutation Fee Payment'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Collection Operator') ,(select id FROM eg_feature WHERE name = 'Collect Water Charges'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Challan Creator') ,(select id FROM eg_feature WHERE name = 'Create Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Challan Creator') ,(select id FROM eg_feature WHERE name = 'Search Challan'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Create Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Modify Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'View Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Create Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Modify Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Create Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'Modify Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Master Access') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_Cancel Access') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'View Service Category'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'View Service Details'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'View Service To Bank Mapping'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Search Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Search Challan'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Search Online Receipts'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Cash Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Cheque Collection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Collection Summary'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Collection Summary-HeadWise Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Receipt Register Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Bank Remittance Statement'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Remittance voucher Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Online Transaction Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Coll_View Access') ,(select id FROM eg_feature WHERE name = 'Dishonored Cheque Report'));
