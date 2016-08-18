------------------ADDING FEATURE--------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Chart Of Accounts','Chart Of Accounts',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add/Modify Bank Account Cheque','Add/Modify Bank Account Cheque',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create/Modify/View Detailed Code','Create/Modify/View Detailed Code',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Bank','Modify Bank',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Bank','Create Bank',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Add User Defined Code','Add User Defined Code',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View User Defined Code','View User Defined Code',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit User Defined Code','Edit User Defined Code',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Function','Create Function',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Function','View Function',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Function','Edit Function',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Recovery','Create Recovery',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Recovery','View Recovery',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Recovery','Edit Recovery',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Fund','Create Fund',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Fund','View Fund',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Fund','Edit Fund',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Supplier','Create Supplier',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Supplier','View Supplier',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Supplier','Edit Supplier',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Account Entity','Create Account Entity',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Account Entity','View Account Entity',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Fund Source','Create Fund Source',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Fund Source','View Fund Source',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Fund Source','Edit Fund Source',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Scheme','Create Scheme',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Scheme','View Scheme',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Scheme','Edit Scheme',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Sub Scheme','Create Sub Scheme',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Sub Scheme','View Sub Scheme',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Sub Scheme','Edit Sub Scheme',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Financial Year','Create Financial Year',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Financial Year','View Financial Year',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Edit Financial Year','Edit Financial Year',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Close Period','Close Period',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Transfer Closing Balance','Transfer Closing Balance',(select id from eg_module  where name = 'EGF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Opening Balance Entry','Opening Balance Entry',(select id from eg_module  where name = 'EGF'));
------------------ADDING FEATURE ACTIONS-------------------

-- COA
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Chart Of Accounts'), id from eg_action where name  in('common-tree-coa','Chart Of Accounts','exil-ChartOfaccountsView','ChartOfAccountsAddNewCoa','ChartOfAccountsSave','ChartOfAccountsModify','ChartOfAccountsUpdate');

--add/modify bank account cheque
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Add/Modify Bank Account Cheque'), id from eg_action where name  in('Add/Modify Bank Account Cheque','Ajax-load Banks','ajax-common-loadbaccount','accountCheque-manipulateCheques','AccountChequeSave');  

--Create/Modify/View Detailed Code
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create/Modify/View Detailed Code'), id from eg_action where name  in('Detailed Code-Create/Modify/View','ChartOfAccountsSearchModify','ChartOfAccountsUpdate','ChartOfAccountsSearchView','ChartOfAccountsModify','exil-chartofaccount add new','Get Next GLcode','ChartOfAccountsCreate');  

--Modify Bank
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Bank'), id from eg_action where name  in('BankMasterModify','modifyBank','saveBank','BankBranchMaster','BankAccountMaster' );

--Create Bank
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Bank'), id from eg_action where name  in('BankMasterModify','modifyBank','saveBank','BankBranchMaster','BankAccountMaster' );

--User Defined Code
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Add User Defined Code'), id from eg_action where name  in('New-AccountEntity','Create-AccountEntity','Result-AccountEntity'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View User Defined Code'), id from eg_action where name  in('Search and View-AccountEntity','Search and View Result-AccountEntity','View-AccountEntity'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit User Defined Code'), id from eg_action where name  in('Search and Edit-AccountEntity','Search and Edit Result-AccountEntity','Edit-AccountEntity','Update-AccountEntity','Result-AccountEntity'); 

--Function
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Function'), id from eg_action where name  in('New-Function','Create-Function','Result-Function'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Function'), id from eg_action where name  in('Search and View-Function','Search and View Result-Function','View-Function'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Function'), id from eg_action where name  in('Search and Edit-Function','Search and Edit Result-Function','Edit-Function','Update-Function','Result-Function'); 

--Recovery
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Recovery'), id from eg_action where name  in('New-Recovery','AjaxGetAccountCodes','Create-Recovery','Result-Recovery'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Recovery'), id from eg_action where name  in('Search and View-Recovery','Search and View Result-Recovery','View-Recovery'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Recovery'), id from eg_action where name  in('Search and Edit-Recovery','Search and Edit Result-Recovery','Edit-Recovery','Update-Recovery','Result-Recovery'); 

--Fund
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Fund'), id from eg_action where name  in('New-Fund','Create-Fund','Result-Fund'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Fund'), id from eg_action where name  in('Search and View-Fund','Search and View Result-Fund','View-Fund'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Fund'), id from eg_action where name  in('Search and Edit-Fund','Search and Edit Result-Fund','Edit-Fund','Update-Fund','Result-Fund'); 

--Supplier
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Supplier'), id from eg_action where name  in('New-Relation','Create-Relation','Result-Relation'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Supplier'), id from eg_action where name  in('Search and View-Relation','Search and View Result-Relation','View-Relation'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Supplier'), id from eg_action where name  in('Search and Edit-Relation','Search and Edit Result-Relation','Edit-Relation','Update-Relation','Result-Relation'); 

--Account entity
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Account Entity'), id from eg_action where name  in('New-Accountdetailtype','Create-Accountdetailtype','Result-Accountdetailtype'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Account Entity'), id from eg_action where name  in('Search and View-Accountdetailtype','Search and View Result-Accountdetailtype','View-Accountdetailtype');

--Fund source
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Fund Source'), id from eg_action where name  in('Create Fundsource','Create-Fundsource','Result-Fundsource'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Fund Source'), id from eg_action where name  in('View Fundsource','Search and View Result-Fundsource','View-Fundsource'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Fund Source'), id from eg_action where name  in('Edit Fundsource','Search and Edit Result-Fundsource','Edit-Fundsource','Update-Fundsource','Result-Fundsource');

--Scheme
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Scheme'), id from eg_action where name  in('Scheme-Create','Scheme Code unique check','NameUniqueChecks','createScheme'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Scheme'), id from eg_action where name  in('Modify Schemes-Search','searchScheme','View Scheme Code'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Scheme'), id from eg_action where name  in('Modify Schemes-Search','searchScheme','Edit Scheme Code','Scheme Code unique check','NameUniqueChecks','editScheme');  

--Sub Scheme
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Sub Scheme'), id from eg_action where name  in('Create Sub Scheme','NameUniqueChecks','CodeUniqueChecks','createSubScheme'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Sub Scheme'), id from eg_action where name  in('View Sub-schemes - Search','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','searchSubScheme','exil-SubScheme View'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Sub Scheme'), id from eg_action where name  in('View Sub-schemes - Search','AjaxMiscReceiptSubScheme','AjaxMiscReceiptScheme','searchSubScheme','EditSubScheme','NameUniqueChecks',
'CodeUniqueChecks','ModifySubScheme'); 

-----Period end activities

--Financial year
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Financial Year'), id from eg_action where name  in('New-CFinancialYear','Create-CFinancialYear','Result-CFinancialYear'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Financial Year'), id from eg_action where name  in('Search and View-CFinancialYear','Search and View Result-CFinancialYear','View-CFinancialYear'); 
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Edit Financial Year'), id from eg_action where name  in('Search and Edit-CFinancialYear','Search and Edit Result-CFinancialYear','Edit-CFinancialYear','Update-CFinancialYear','Result-CFinancialYear');

--Close period
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Close Period'), id from eg_action where name  in('Search and Edit-ClosedPeriod','Search and Edit Result-ClosedPeriod','Edit-ClosedPeriod','Update-ClosedPeriod','Result-ClosedPeriod'); 

--Transfer closing balance
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Transfer Closing Balance'), id from eg_action where name  in('TransferClosingBalance','TransferClosingBalanceTransfer');

--Opening balance entry
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Opening Balance Entry'), id from eg_action where name  in('Opening Balance Entry','ajax-process-function','Minor Head Ajax','Major Head Ajax','loadAllLiablityCodes', 'loadAllAssetCodes','SearchTransactionSummariesForNonSubledger','ajax-common-detailtype','ajax-common-detailcode','Ajax Transaction Submit','ajax-common-entityby20','ajax-LoadEntites_AccountDetailType','searchEntries-accountdetail','SearchTransactionSummariesForSubledger'); 

------------------ADDING FEATURE ROLES--------------------

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Chart Of Accounts'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Add/Modify Bank Account Cheque'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create/Modify/View Detailed Code'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Bank'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Bank'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Add User Defined Code'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View User Defined Code'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit User Defined Code'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Function'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Function'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Function'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Recovery'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Recovery'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Recovery'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Fund'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Fund'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Fund'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Supplier'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Supplier'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Supplier'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create  Account Entity'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View  Account Entity'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Fund Source'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Fund Source'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Fund Source'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Scheme'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Scheme'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Scheme'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Sub Scheme'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Sub Scheme'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Sub Scheme'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Financial Year'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Financial Year'),id from eg_role where name in('Super User','Financial Administrator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Edit Financial Year'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Close Period'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Transfer Closing Balance'),id from eg_role where name in('Super User','Financial Administrator');

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Opening Balance Entry'),id from eg_role where name in('Super User','Financial Administrator');


