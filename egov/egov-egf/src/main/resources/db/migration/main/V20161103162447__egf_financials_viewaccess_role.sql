insert into eg_role values(nextval('seq_eg_role'),'FINANCIALS VIEW ACCESS','User has view access to Financials Masters and transactional data',current_date,1,1,current_date,0);

--COA
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('ChartOfAccountsView','common-tree-coa','ViewChartOfAccounts');

--Detailed Code
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('ChartOfAccounts-viewDetailedCode','ChartOfAccountsSearchView');

--Bank Account Cheque
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('accountCheque-view','Ajax-load Banks','ajax-common-loadbaccount','accountCheque-viewCheques');

--Bank
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('BankMasterView','modifyBank','BankBranchMaster','BankAccountMaster');

--User Defined Codes
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-AccountEntity','Search and View Result-AccountEntity','View-AccountEntity');

--Recovery Codes
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-Recovery','Search and View Result-Recovery','View-Recovery');

--Fund
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-Fund','Search and View Result-Fund','View-Fund');

--Supplier
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-Relation','Search and View Result-Relation','View-Relation');

--Account Entity
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-Accountdetailtype','Search and View Result-Accountdetailtype','View-Accountdetailtype');

--Fund Source
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View Fundsource','Search and View Result-Fundsource','View-Fundsource');

--Function
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-Function','Search and View Result-Function','View-Function');

--Scheme and Sub Scheme
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View Schemes-Search','searchScheme','View Scheme Code','View Sub-schemes - Search','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','searchSubScheme','exil-SubScheme View');

--Budget Group
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View-BudgetGroup','Search and View-BudgetGroup','Search and View Result-BudgetGroup');

--Budget Definition
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View-Budget','Search and View-Budget','Search and View Result-Budget');

--Search Budget
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search Budget','ajax-budget-loadBudget','BudgetSearch-groupedBudgets','budgetSearch!groupedBudgetDetailList');

--Financial Year
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('Search and View-CFinancialYear','Search and View Result-CFinancialYear','View-CFinancialYear');

--View Bill Register
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View Bill Registers-Search','BillRegisterSearch','Bill View-View','cbill-print','ExpenseBillPdf','ExpenseBillXls','cbill-ajaxprint','Contingent Bill-View','cbill-print-crud');

--View Voucher 
Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='FINANCIALS VIEW ACCESS'),id  from eg_action  where name in ('View Vouchers','JournalVoucherSearch','Voucher View','payment-view','Direct Bank Payments-View','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','jv-beforemodify','Remittance recovery view','payment-voucherprint','payment-voucherAjaxprint','payment-voucherPDFprint','payment-voucherXLSprint','Bank to Bank Transfer-View');



