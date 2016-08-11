------------------ADDING FEATURE--------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Remittance Recovery','Create Remittance Recovery',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Remittance Recovery','Modify Remittance Recovery',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Remittance Recovery','View Remittance Recovery',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Remittance Recovery','Approve Remittance Recovery',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Cheque Assignment-Remittance Recovery','Cheque Assignment-Remittance Recovery',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Upload Budget','Upload Budget',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Approve Uploaded Budget','Approve Uploaded Budget',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Budget','Search Budget',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add/Release-Budget Addition Appropriation','Add/Release-Budget Addition Appropriation',(select id from eg_module  where name = 'EGF'));
------------------ADDING FEATURE ACTIONS-------------------

-- Remittance Recovery
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Remittance Recovery'), id from eg_action where name  in('Remittance Recovery-Create','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','AjaxMiscReceiptFundSource','RemitRecoverySearch','RemitRecoveryRemit','ajax-common-bankbyfandt','ajax-common-accountbalance','Ajax-Bank Account Based on Type','AjaxApproverDropdown','AjaxDesignationDropdown','RemitRecoveryCreate','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Remittancerecovry-inbox','RemitRecoverySendForApproval','payment-viewInboxItems');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Remittance Recovery'), id from eg_action where name  in('Remittance Recovery-Create','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','AjaxMiscReceiptFundSource','RemitRecoverySearch','RemitRecoveryRemit','ajax-common-bankbyfandt','ajax-common-accountbalance','Ajax-Bank Account Based on Type','AjaxApproverDropdown','AjaxDesignationDropdown','RemitRecoveryCreate','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Remittancerecovry-inbox','RemitRecoverySendForApproval','payment-viewInboxItems');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Remittance Recovery'), id from eg_action where name  in('Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint');
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Remittance Recovery'), id from eg_action where name  in('payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Remittancerecovry-inbox','RemitRecoverySendForApproval','AjaxApproverDropdown','AjaxDesignationDropdown','payment-viewInboxItems');

--Cheque Assignment-Remittance Recovery
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Cheque Assignment-Remittance Recovery'), id from eg_action where name  in('Cheque Assignment For Remittances','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','AjaxMiscReceiptFundSource','ajax_BanksApprovedRemit','ajax-common-bankaccbyapprovedpayments','SearchChequesOfRemittance','ajax-common-validatecheque','ChequeAssignmentCreate','chequeFormatPrint','ajax-common-bankbyapprovedpayments');

-----Budgeting
--Upload budget
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Upload Budget'), id from eg_action where name  in('BudgetLoadBeforeUpload','File Download','BudgetLoadUpload','BudgetLoadExport');

--Approve Uploaded Budget
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Approve Uploaded Budget'), id from eg_action where name  in('Search Approve Uploaded Budget','BudgetUploadReport-getReferenceBudget','Update Approve Uploaded Budget');

--Search budget
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Search Budget'), id from eg_action where name  in('Search Budget','ajax-budget-loadBudget','BudgetSearch-groupedBudgets','budgetSearch!groupedBudgetDetailList');

--Add/Release-Budget Addition Appropriation
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Add/Release-Budget Addition Appropriation'), id from eg_action where name  in('Add/Release Budget ReAppropriation','ajax-budgetReApp-loadBudget','BudgetReAppropriationLoadActuals','BudgetReAppropriationCreate');


------------------ADDING FEATURE ROLES--------------------

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Remittance Recovery'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Remittance Recovery'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Remittance Recovery'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Remittance Recovery'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Cheque Assignment-Remittance Recovery'),id from eg_role where name in('Super User','Payment Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Upload Budget'),id from eg_role where name in('Super User','Budget Creator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Approve Uploaded Budget'),id from eg_role where name in('Super User','Budget Approver');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Search Budget'),id from eg_role where name in('Super User','Budget Approver','Financial Report Viewer','Budget Creator','ERP Report Viewer');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Add/Release-Budget Addition Appropriation'),id from eg_role where name in('Super User','Budget Creator');
