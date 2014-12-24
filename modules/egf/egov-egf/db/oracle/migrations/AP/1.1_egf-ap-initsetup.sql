#UP

Insert into EG_MODULE
(ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
Values
(seq_modulemaster.nextVal, 'EGF',sysdate,'1','/EGF',NULL,NULL,'EGF');


--------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
 (seq_modulemaster.nextVal, 'Transactions',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),1,'Transactions');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
 (seq_modulemaster.nextVal, 'Reports',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),2,'Reports');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Masters',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),3,'Masters');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Period End Activities',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),4,'Period End Activities');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Set-up',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),5,'Set-up');
  
--Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
-- Values
 -- (seq_modulemaster.nextVal, 'Administration',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),6,'Administration');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Deductions',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),7,'Deductions');



--Transactions----------------------------------------------------------------------------------------------------------------



Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Receipts',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),1,'Receipts');


Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Expenditures',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),2,'Expenditures');


Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Journal Vouchers',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),3,'Journal Vouchers');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Contra Entries',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),4,'Contra Entries');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'BRS',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),5,'BRS');

--Reports---------------------------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Financial Statements',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),1,'Financial Statements');
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Accounting Records',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),2,'Accounting Records');
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'MIS Reports',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),3,'MIS Reports');
  
  
----Masters-----------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Chart of Accounts',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),1,'Chart of Accounts');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Supplier/Contractors',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),9,'Supplier/Contractors');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Schemes',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),10,'Schemes');


------Set-up---------------------------------------------------------------------------------------------------------------

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Salary Codes',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),5,'Salary Codes');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Report Schedule Mapping',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),4,'Report Schedule Mapping');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Cheque Printing Format',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),5,'Cheque Printing Format');




--Expenditures---------------------------------------------------------

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Procurement Orders',sysdate,'1','',(select id_module from eg_module where module_name='Expenditures'),1,'Procurement Orders');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Bill Registers',sysdate,'1','',(select id_module from eg_module where module_name='Expenditures'),2,'Bill Registers');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Bills Accounting',sysdate,'1','',(select id_module from eg_module where module_name='Expenditures'),3,'Bills Accounting');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Payments',sysdate,'1','',(select id_module from eg_module where module_name='Expenditures'),4,'Payments');
  
------Bill Registers--------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Salary Bills',sysdate,'1','',(select id_module from eg_module where module_name='Bill Registers'),6,'Salary Bills');
  
--Deductions---------------------------------------------------------------------------------------------------------------
--Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 --Values
  --(seq_modulemaster.nextVal, 'Master',sysdate,'1','',(select id_module from eg_module where module_name='Deductions'),1,'Master');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Remittance Recovery',sysdate,'1','',(select id_module from eg_module where module_name='Deductions'),2,'Remitance Recovery');

--Deductions-Master-------------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Party Types',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),6,'Party Types');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Contract Types',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),7,'Contract Types');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Recovery Masters',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),8,'Recovery Masters');



--end of eg_module
--------------------------------------------------------------------------------------------------
--egaction details------------------------------------------------
--
--SQL Statement which produced this data:
--  select m.id,m.name,m.parentid,m.actionid,(select name from menutree m2 where m2.id=m.parentid) from menutree m,eg_action e  where actionid is not null and e.id=m.actionid order by parentid,id
--

--Transactions--------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cancellation of  Vouchers-Search', NULL,null, sysdate, 
    '/HTML/VMC/CancelVoucher_VMC.jsp', '', NULL, (select id_module from eg_module where module_name='Transactions'), 6, 
    'Cancel Vouchers', 1, 'HELP/Cancel Vouchers.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Approve Vouchers', NULL,NULL, sysdate, 
    '/HTML/VMC/ConfirmVoucher_VMC.jsp?', '', NULL, (select id_module from eg_module where module_name='Transactions'), 7, 
    'Approve Vouchers', 1, 'HELP/Approve Vouchers.htm');


--Masters---------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Code-Screen Mappings', NULL,NULL, sysdate, 
    '/HTML/ftServicesmap.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 2, 
    'Code-Screen Mappings', 1, 'HELP/Code-Screen Mapping');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Creditors Recoveries', NULL,NULL, sysdate, 
    '/HTML/TdsEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 3, 
    'Creditors Recoveries', 1, 'HELP/Creditors Recoveries.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined Codes', NULL,NULL, sysdate, 
    '/HTML/SubCodesEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 4, 
    'User-defined Codes', 1, 'HELP/User-defined Codes.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Source of Financing', NULL,NULL, sysdate, 
    '/HTML/FundSource.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 5, 
    'Source of Financing', 1, 'HELP/Source of Financing.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Collection/Payment point', NULL,NULL, sysdate, 
    '/HTML/BillCollector.htm', '', NULL, (select id_module from eg_module where module_name='Masters'),6, 
    'Collection/Payment point', 1, 'HELP/Collection/Payment point.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Accounting Entity', NULL,NULL, sysdate, 
    '/HTML/AccountingEntity.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 7, 
    'Accounting Entity', 1, 'HELP/Accounting Entity.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Setup Cheque in Hand/Cash in Hand', NULL,NULL, sysdate, 
    '/HTML/setupCashCheckInHand.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 8, 
    'Setup Cheque in Hand/Cash in Hand', 1, 'HELP/Setup Cheque in Hand/Cash in Hand.htm');

--Period End Activities-----------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year-SetUp', NULL,NULL, sysdate, 
    '/HTML/FinancialYearEnq.htm', '', NULL, (select id_module from eg_module where module_name='Period End Activities'), 1, 
    'Financial Year', 1, 'HELP/Financial Year.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Opening Balance-SetUp', NULL,NULL, sysdate, 
   '/HTML/OpeningBalance.htm', '', NULL, (select id_module from eg_module where module_name='Period End Activities'), 2, 
    'Opening Balance', 1, 'HELP/Opening Balance.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fiscal periods-SetUp', NULL,NULL, sysdate, 
    '/HTML/SetUp.htm', '', NULL, (select id_module from eg_module where module_name='Period End Activities'), 3, 
    'Close Period', 1, 'HELP/Close Period.htm');
--Set-up--------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Funds-Search', NULL,NULL, sysdate, 
    '/HTML/Fund.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 1, 
    'Funds', 1, 'HELP/Funds.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Functions-Search', NULL,NULL, sysdate, 
    '/HTML/Function.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 2, 
    'Functions', 1, 'HELP/Functions.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'ULB Details', NULL,NULL, sysdate, 
    '/HTML/CompanyDetail.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 3, 
    'ULB Details', 1, 'HELP/ULB Details.htm');


--Report Schedule Mapping-----------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Report Schedule Mapping-Create', NULL,NULL, sysdate, 
    '/Reports/ScheduleMaster.jsp', '', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 1, 
    'Create Report Schedule', 1, 'HELP/Report Schedule Mapping-Create.htm');
  
  Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Schedules-Search ', NULL,NULL, sysdate, 
        '/Reports/ScheduleSearch.htm', '', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 1, 
        'View/Modify Report Schedule ', 1, 'HELP/View-Modify Report Schedule.htm');







--Receipts------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Receipts'), 1, 
    'Create Receipt', 1, 'HelpAssistance/AP/Miscellaneous Receipts_AP.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/ReceiptSearch_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Receipts'), 2, 
    'View Receipts', 1, 'HelpAssistance/AP/View Receipts-Search_AP.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/ReceiptSearch_VMC.jsp', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Receipts'), 3, 
    'Modify Receipts', 1, 'HelpAssistance/AP/Modify Receipts-Search_AP.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/ReceiptSearch_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Receipts'), 4, 
    'Reverse Receipts', 1, 'HelpAssistance/AP/Reverse Receipts-Search_AP.htm');

--Bills Accounting--------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bills', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBill_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 1, 
    'Salary Bills', 1, 'HELP/Salary Bill.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View  Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 2, 
    'View  Bills', 1, 'HELP/View  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify  Bills-Search', NULL,NULL, sysdate, 
        '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 3, 
    'Modify  Bills', 1, 'HELP/Modify  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Reverse Bills-Search', NULL,NULL, sysdate, 
        '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 4, 
    'Reverse Bills', 1, 'HELP/Reverse Bills-Search.htm');


--Payments---------------------------------------------------------------------------------------------------------------------------------------
  
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments', NULL,NULL, sysdate, 
    '/payment/payment.do', 'submitType=beforeSearchAllBills', NULL, (select id_module from eg_module where module_name='Payments'), 2, 
    'Bill Payments', 1, 'HELP/Bill Payments.htm');
    
    
   Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments ', NULL,NULL, sysdate, 
    '/payment/payment.do', 'submitType=searchAllBills', NULL, (select id_module from eg_module where module_name='Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
    
    
     Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Bill Payments', NULL,NULL, sysdate, 
        '/payment/payment.do', 'submitType=beforeCreatePayment', NULL, (select id_module from eg_module where module_name='Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
     
    
    Insert into eg_action
            (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
          Values
            (seq_eg_action.nextVal, ' Bill Payments ', NULL,NULL, sysdate, 
             '/payment/payment.do', 'submitType=createPayment', NULL, (select id_module from eg_module where module_name='Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
  
   

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID,ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Direct Bank Payments', NULL,NULL, sysdate, 
    '/HTML/VMC/DirectBankPayment_VMC.jsp', 'showMode=paymentBank', NULL, (select id_module from eg_module where module_name='Payments'), 1, 
    'Direct Bank Payments', 1, 'HelpAssistance/AP/Cash Payment_AP.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payment', NULL,NULL, sysdate, 
    '/HTML/VMC/AdvanceJournal_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Payments'), 3, 
    'Advance Payment', 1, 'HELP/Advance Payments.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Payments-Search', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=view', NULL, (select id_module from eg_module where module_name='Payments'), 4, 
    'View Payments', 1, 'HELP/View Payments-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'View Payments- Search', NULL,NULL, sysdate, 
        '/payment/PaymentVhSearch.jsp', 'submitType=searchPaymentVouchers\&mode=view', NULL, (select id_module from eg_module where module_name='Payments'), 4, 
        'View Payments', 0, 'HELP/View Payments-Search.htm');
   
    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Payments-Search', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=modify', NULL, (select id_module from eg_module where module_name='Payments'), 5, 
    'Modify Payments', 1, 'HELP/Modify Payments-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify Payments- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do?', 'submitType=searchPaymentVouchers\&mode=modify', NULL, (select id_module from eg_module where module_name='Payments'), 5, 
        'Modify Payments', 0, 'HELP/Modify Payments-Search.htm');
  
    
    
    
   

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Payments-Search', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=reverse', NULL, (select id_module from eg_module where module_name='Payments'), 6, 
    'Reverse Payments', 1, 'HELP/Reverse Payments-Search.htm');
    
   
   Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Reverse Payments- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do', 'submitType=searchPaymentVouchers\&mode=reverse', NULL, (select id_module from eg_module where module_name='Payments'), 6, 
    'Reverse Payments', 0, 'HELP/Reverse Payments-Search.htm');

--Insert into eg_action
--   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
-- Values
--   (seq_eg_action.nextVal, 'Cancel Payment', NULL,NULL, sysdate, 
--    'payment/PaymentVhSearch.jsp', 'mode=cancel', NULL, (select id_module from eg_module where module_name='Payments'), 7, 
--    'Cancel Payment', 1, 'HELP/Cancel Payment.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Generate Payment Advice-Search', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=paymentAdvice', NULL, (select id_module from eg_module where module_name='Payments'), 8, 
    'Generate Payment Advice', 1, 'HELP/Generate Payment Advice-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Generate Payment Advice- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do', 'submitType=searchPaymentVouchers\&mode=paymentAdvice', NULL, (select id_module from eg_module where module_name='Payments'), 8, 
        'Generate Payment Advice', 0, 'HELP/Generate Payment Advice-Search.htm');
    
    
    
   

----Journal Voucher----------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_General_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Journal Vouchers'), 1, 
    'Create Journal Voucher', 1, 'HELP/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General\&showMode=view', NULL, (select id_module from eg_module where module_name='Journal Vouchers'), 2, 
    'View Journal Vouchers', 1, 'HELP/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General\&showMode=edit', NULL, (select id_module from eg_module where module_name='Journal Vouchers'), 3, 
    'Modify Journal Vouchers', 1, 'HELP/Journal Voucher.htm');
--not executed
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General\&showMode=modify', NULL, (select id_module from eg_module where module_name='Journal Vouchers'), 4, 
    'Reverse Journal Vouchers', 1, 'HELP/Journal Voucher.htm');
-----------------------------------------------------------------------------------------------------------------------------------------


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 1, 
    'Cash Deposit', 1, 'HELP/Cash Deposit.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal', 1, 'HELP/Cash Withdrawal.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Bank to Bank Transfer', 1, 'HELP/Bank to Bank Transfer.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay in', NULL,NULL, sysdate, 
    '/HTML/VMC/PayInSlip_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Pay in', 1, 'HELP/Pay in.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Contra\&showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 5, 
    'View Contra Entries', 1, 'HELP/View Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Contra\&showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 6, 
    'Modify Contra Entries', 1, 'HELP/Modify Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Contra\&showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 7, 
    'Reverse Contra Entries', 1, 'HELP/Reverse Contra Entries-Search.htm');










--Financial Statements---------------------------------------------------


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Income - Expenditures Statement', NULL,NULL, sysdate, 
    '/Reports/IncomeExpenseReports.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 1, 
    'Income - Expenditures Statement', 1, 'HELP/Income - Expenditures Statement.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt/Payment Report', NULL,NULL, sysdate, 
    '/Reports/RPReport.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 2, 
    'Receipt/Payment Report', 1, 'HELP/Receipt/Payment Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Balance Sheet', NULL,NULL, sysdate, 
    '/Reports/BSReport.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 3, 
    'Balance Sheet', 1, 'HELP/Balance Sheet.htm');

--Chart Of Accounts----------------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Chart Of Accounts', NULL,NULL, sysdate, 
    '/commonyui/egov/ChartOfaccountsMenuTree.jsp?eGovAppName=ChartOfAccounts', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 1, 
    'Chart Of Accounts', 1, 'HELP/Chart Of Accounts.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Add Bank', NULL,NULL, sysdate, 
    '/HTML/BankAdd.htm', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 2, 
    'Add Bank', 1, 'HELP/Add Bank.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Add/Modify Branch-Modify Bank', NULL,NULL, sysdate, 
    '/HTML/BankEnquiry.htm', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 3, 
    'Add/Modify Branch-Modify Bank', 1, 'HELP/ADD/Modify BRANCH AND Modify BANK.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Detailed Code-Create/Modify/View', NULL,NULL, sysdate, 
    '/HTML/DetailCodeEnquiry.htm?showMode=view\&actionId=109', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 4, 
    'Create/Modify/View Detailed Code', 1, 'HELP/Create/Modify/View Detailed Code.htm');
    
    
    ---Procurement Orders-------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/WorksDetailAdd_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Procurement Orders'), 1, 
    'Create Procurement Order', 1, 'HELP/Create Procurement Order.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Procurement Orders-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/POSearch_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Procurement Orders'), 2, 
    'View Procurement Orders', 1, 'HELP/View Procurement Orders.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Procurement Orders-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/POSearch_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Procurement Orders'), 3, 
    'Modify Procurement Orders', 1, 'HELP/Modify Procurement Orders.htm');



----Supplier/Contractor-----------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier/Contractor-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/RelationMod_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 1, 
    'Create Supplier/Contractor', 1, 'HELP/Create Supplier/Contractor.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Supplier/Contractor-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/Relation_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 2, 
    'View Supplier/Contractor', 1, 'HELP/View Supplier/Contractor-Search.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Supplier/Contractor-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/Relation_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Supplier/Contractor'), 3, 
    'Modify Supplier/Contractor', 1, 'HELP/Modify Supplier/Contractor-Search.htm');

----Accounting Records--------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Trial Balance', NULL,NULL, sysdate, 
    '/Reports/TrialBalance.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 1, 
    'Trial Balance', 1, 'HELP/Trial Balance.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Book', NULL,NULL, sysdate, 
    '/Reports/CashBookRcptPmt.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 2, 
    'Cash Book', 1, 'HELP/Cash Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Book', NULL,NULL, sysdate, 
    '/Reports/BankBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 3, 
    'Bank Book', 1, 'HELP/Bank Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Book', NULL,NULL, sysdate, 
    '/Reports/JournalBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 4, 
    'Journal Book', 1, 'HELP/Journal Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'General Ledger', NULL,NULL, sysdate, 
    '/Reports/GeneralLedger.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 5, 
    'General Ledger', 1, 'HELP/General Ledger.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Sub-Ledger', NULL,NULL, sysdate, 
    '/Reports/SubLedgerReport.jsp', 'reportType=sl', NULL, (select id_module from eg_module where module_name='Accounting Records'), 7, 
    'Sub-Ledger', 1, 'HELP/Sub-Ledger.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Day Book', NULL,NULL, sysdate, 
    '/Reports/DayBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 6, 
    'Day Book', 1, 'HELP/Day Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'SubLedger Schedule', NULL,NULL, sysdate, 
    '/Reports/SubLedgerSchedule.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 8, 
    'SubLedger Schedule', 1, 'HELP/SubLedger Schedule.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Opening Balance Report', NULL,NULL, sysdate, 
    '/Reports/OpeningBalance.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 9, 
    'Opening Balance Report', 1, 'HELP/Opening Balance Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque-in-hand Report', NULL,NULL, sysdate, 
    '/Reports/chequeInHand.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 11, 
    'Cheque-in-hand Report', 1, 'HELP/Cheque-in-hand Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheques Received', NULL,NULL, sysdate, 
    '/Reports/ChequesReceived.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Accounting Records'), 10, 
    'Cheques Received', 1, 'HELP/Cheques Received.htm');
    
-----BRS-----------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reconcile with Bank', NULL,NULL, sysdate, 
    '/brs/BankReconciliation.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='BRS'), 1, 
    'Reconcile with Bank', 1, 'HELP/Reconcile with Bank.htm');
    
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Reconcile with Bank ', NULL,NULL, sysdate, 
        '/brs/BankReconciliation.do', 'submitType=showBrsDetails', NULL, (select id_module from eg_module where module_name='BRS'), 1, 
        'Reconcile with Bank', 0, 'HELP/Reconcile with Bank.htm');

Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Reconcile with Bank', NULL,NULL, sysdate, 
        '/brs/BankReconciliation.do', 'submitType=reconcile', NULL, (select id_module from eg_module where module_name='BRS'), 1, 
        'Reconcile with Bank', 0, 'HELP/Reconcile with Bank.htm');
       
       
       Insert into eg_action
              (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
            Values
              (seq_eg_action.nextVal, ' Reconcile with Bank ', NULL,NULL, sysdate, 
               '/brs/BankReconciliation.do', 'submitType=getAccountNumbers', NULL, (select id_module from eg_module where module_name='BRS'), 1, 
               'Reconcile with Bank', 0, 'HELP/Reconcile with Bank.htm');

       




Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Statement Entries-Not in Bank Book', NULL,NULL, sysdate, 
    '/brs/BankStatementEntries.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='BRS'), 2, 
    'Bank Statement Entries-Not in Bank Book', 1, 'HELP/Bank Statement Entries-Not in Bank Book.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Statement Entries- Not in Bank Book', NULL,NULL, sysdate, 
    '/brs/BankStatementEntries.do', 'submitType=getAccountNumbers', NULL, (select id_module from eg_module where module_name='BRS'), 2, 
    'Bank Statement Entries-Not in Bank Book', 0, 'HELP/Bank Statement Entries-Not in Bank Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Statement Entries -Not in Bank Book', NULL,NULL, sysdate, 
    '/brs/BankStatementEntries.do', 'submitType=getDetails', NULL, (select id_module from eg_module where module_name='BRS'), 2, 
    'Bank Statement Entries-Not in Bank Book', 0, 'HELP/Bank Statement Entries-Not in Bank Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Statement Entries - Not in Bank Book', NULL,NULL, sysdate, 
    '/brs/BankStatementEntries.do', 'submitType=saveDetails', NULL, (select id_module from eg_module where module_name='BRS'), 2, 
    'Bank Statement Entries-Not in Bank Book', 0, 'HELP/Bank Statement Entries-Not in Bank Book.htm');





Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reconciliation Summary', NULL,NULL, sysdate, 
    '/brs/ReconciliationSummary.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='BRS'), 3, 
    'Reconciliation Summary', 1, 'HELP/Reconciliation Summary.htm');
    
  Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reconciliation Summary ', NULL,NULL, sysdate, 
    '/brs/ReconciliationSummary.do', 'submitType=getAccountNumbersForRS', NULL, (select id_module from eg_module where module_name='BRS'), 3, 
    'Reconciliation Summary', 0, 'HELP/Reconciliation Summary.htm');
    
       
      Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Reconciliation Summary', NULL,NULL, sysdate, 
        '/brs/ReconciliationSummary.do', 'submitType=brsSummary', NULL, (select id_module from eg_module where module_name='BRS'), 3, 
    'Reconciliation Summary', 0, 'HELP/Reconciliation Summary.htm');
  
   
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Reconciliation Summary ', NULL,NULL, sysdate, 
        '/brs/ReconciliationSummary.jsp', '', NULL, (select id_module from eg_module where module_name='BRS'), 3, 
    'Reconciliation Summary', 0, 'HELP/Reconciliation Summary.htm'); 
   

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Dishonored Cheques', NULL,NULL, sysdate, 
    '/brs/DishonoredChequeEntries.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='BRS'), 4, 
    'Dishonored Cheques', 1, 'HELP/Dishonored Cheques.htm');
   
   Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Dishonored Cheques ', NULL,NULL, sysdate, 
        '/brs/DishonoredChequeEntries.do', 'submitType=getAccountNumbers', NULL, (select id_module from eg_module where module_name='BRS'), 4, 
        'Dishonored Cheques', 0, 'HELP/Dishonored Cheques.htm');
   
   Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Dishonored Cheques', NULL,NULL, sysdate, 
        '/brs/DishonoredChequeEntries.do', 'submitType=getDetails', NULL, (select id_module from eg_module where module_name='BRS'), 4, 
        'Dishonored Cheques', 0, 'HELP/Dishonored Cheques.htm'); 
    
     Insert into eg_action
           (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
         Values
           (seq_eg_action.nextVal, ' Dishonored Cheques ', NULL,NULL, sysdate, 
            '/brs/DishonoredChequeEntries.do', 'submitType=saveDetails', NULL, (select id_module from eg_module where module_name='BRS'), 4, 
        'Dishonored Cheques', 0, 'HELP/Dishonored Cheques.htm'); 
        
        Insert into eg_action
	           (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
	         Values
	           (seq_eg_action.nextVal, ' Dishonored  Cheques ', NULL,NULL, sysdate, 
	            '/brs/DishonoredChequeEntries.do', 'submitType=beforePrintDishonoredCheque', NULL, (select id_module from eg_module where module_name='BRS'), 4, 
        'Dishonored Cheques', 0, 'HELP/Dishonored Cheques.htm'); 
    
   
  
-----Administration---------------------------------------------------------------------------------------------------------------------------------

--Insert into eg_action
  -- (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 --Values
 --  (seq_eg_action.nextVal, 'Boundary Settings', NULL,NULL, sysdate, 
 --   '/administration/adminFrames.jsp', '', NULL, (select id_module from eg_module where module_name='Administration'), 1, 
 --   'Boundary Settings', 1, 'HELP/Boundary Settings.htm');



--Insert into eg_action
  -- (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
-- Values
 --  (seq_eg_action.nextVal, 'Role-Based Access Control', NULL,NULL, sysdate, 
--    '/rbac/index.jsp', '', NULL, (select id_module from eg_module where module_name='Administration'), 2, 
--    'Role-Based Access Control', 1, 'HELP/Role-Based Access Control.htm');

-----MIS Reports---------------------------------------------------------------------------------------------------------------------------------



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Supplier Report', NULL,NULL, sysdate, 
    '/Reports/rptContractorSupplier.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 1, 
    'Contractor Supplier Report', 1, 'HELP/Contractor Supplier Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Issue Register', NULL,NULL, sysdate, 
    '/Reports/rptChequeRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 2, 
    'Cheque Issue Register', 1, 'HELP/Cheque Issue Register.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Register of Bills', NULL,NULL, sysdate, 
    '/Reports/RptBillRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 3, 
    'Register of Bills', 1, 'HELP/Register of Bills.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt Register', NULL,NULL, sysdate, 
    '/Reports/receiptRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 4, 
    'Receipt Register', 1, 'HELP/Receipt Register.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Transaction', NULL,NULL, sysdate, 
    '/Reports/BankTransaction.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='MIS Reports'), 5, 
    'Bank Transaction', 1, 'HELP/Bank Transaction.htm');
   
    Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Statement of Outstanding Liability ', NULL,NULL, sysdate, 
    '/Reports/OsStmtForLiabilityExpenses.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 6, 
    'Statement of Outstanding Liability ', 1, 'HELP/OsStmtForLiabilityExpenses.htm');

     Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Function-wise IE Subsidary Register', NULL,NULL, sysdate, 
    '/Reports/FunctionwiseIESubsidaryRegRep.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 7, 
    'Function-wise IE Subsidary Register', 1, 'HELP/Function-wise IE Subsidary Register.htm');
   
     Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Deposit Register', NULL,NULL, sysdate, 
    '/Reports/DepositeRegisterReport.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 8, 
    'Deposit Register', 1, 'HELP/Deposit Register.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Register of Advance', TO_DATE('07/29/2008 18:53:39', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/AdvanceRegister.jsp', '', (select id_module from eg_module where module_name='MIS Reports'), 9, 'Register of Advance', 1, 'HELP/AdvanceRegister.htm');


  Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Scheme Utilization', TO_DATE('07/21/2008 12:43:11', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/SchemeUtilizationReport.jsp', '', (select id_module from eg_module where module_name='MIS Reports'), 10, 'Scheme Utilization', 1, 'HELP/SchemeUtilization.htm');

 Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Dishonored Cheques Report', TO_DATE('07/21/2008 12:43:11', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/DishonoredChequeReport.jsp', '', (select id_module from eg_module where module_name='MIS Reports'), 11, 'Dishonored Cheques Report', 1, 'HELP/Dishonored Cheques Report.htm');
   
 INSERT INTO eg_action
      (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Summary Statement Report',SYSDATE, '/Reports/SummaryStatement.jsp', '', (SELECT id_module FROM eg_module WHERE module_name='MIS Reports'), 12, 'Summary Statement Report', 1, 'HelpAssistance/AP/SummaryStatement_AP.htm');
   
   INSERT INTO eg_roleaction_map SELECT (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME IN ('SuperUser','Super User','SUPER USER')),id FROM eg_action WHERE name='Summary Statement Report';
   

INSERT INTO eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
VALUES
(seq_eg_action.NEXTVAL, 'Audit Trail',SYSDATE, '/Reports/voucherList.jsp', '', (SELECT id_module FROM eg_module WHERE module_name='MIS Reports'), 13, 'Audit Trail', 1, 'HelpAssistance/AP/AuditTrail_AP.htm');
   
INSERT INTO eg_roleaction_map SELECT (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME IN ('SuperUser','Super User','SUPER USER')),id FROM eg_action WHERE name='Audit Trail';
   


---------------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Works Bill-Create', NULL,NULL, sysdate, 
    '/billsaccounting/worksBill.do', 'submitType=beforeCreate', NULL, (select id_module from eg_module where module_name='Bill Registers'), 1, 
    'Create Works Bill', 1, 'HELP/Works Bill-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Works Bill - Create', NULL,NULL, sysdate, 
    '/billsaccounting/worksBill.do', 'submitType=getTdsAndotherdtls', NULL, (select id_module from eg_module where module_name='Bill Registers'), 1, 
    'Create Works Bill', 0, 'HELP/Works Bill-Create.htm');
    
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Works Bill - Create', NULL,NULL, sysdate, 
        '/billsaccounting/worksBill.do', 'submitType=create', NULL, (select id_module from eg_module where module_name='Bill Registers'), 1, 
        'Create Works Bill', 0, 'HELP/Works Bill-Create.htm');
    
    
   
    
    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingent Bill-Create', NULL,NULL, sysdate, 
    '/billsaccounting/cbill.do', 'submitType=beforeCreateCBill', NULL, (select id_module from eg_module where module_name='Bill Registers'), 2, 
    'Create Contingent Bill', 1, 'HELP/Contingent Bill-Create.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Contingent Bill- Create', NULL,NULL, sysdate, 
        '/billsaccounting/cbill.do', 'submitType=create', NULL, (select id_module from eg_module where module_name='Bill Registers'), 2, 
        'Create Contingent Bill', 0, 'HELP/Contingent Bill-Create.htm');
        
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Bill Registers-Search', NULL,NULL, sysdate, 
    '/billsaccounting/BillRegisterSearch.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Bill Registers'), 3, 
    'View Bill Registers', 1, 'HELP/View Bill Registers-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Bill Registers-Search', NULL,NULL, sysdate, 
    '/billsaccounting/BillRegisterSearch.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Bill Registers'), 4, 
    'Modify Bill Registers', 1, 'HELP/Modify Bill Registers-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Approve Bill Registers-Search', NULL,NULL, sysdate, 
    '/inbox/Inbox.jsp', '', NULL, (select id_module from eg_module where module_name='Bill Registers'), 5, 
    'Approve Bill Register', 1, 'HELP/Approve Bill Register.htm');


    Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cancellation of  Bills', NULL,NULL, sysdate, 
    '/HTML/VMC/CancelBills_VMC.jsp', '', NULL, (select id_module from eg_module where module_name='Bill Registers'), 6, 
    'Cancel Bills', 1, 'HelpAssistance/AP/CancelBills.htm');


---------------------------------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegister_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Salary Bills'), 1, 
    'Create Salary Bill', 1, 'HELP/Create Salary Bill.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegisterSearch_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Salary Bills'), 2, 
    'View Salary Bills', 1, 'HELP/View Salary Bills-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegisterSearch_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Salary Bills'), 3, 
    'Modify  Salary Bills', 1, 'HELP/Create Salary Bill.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Approve Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/ApproveSalaryBillRegister_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Salary Bills'), 4, 
    'Approve Salary Bills', 1, 'HELP/Approve Salary Bill.htm');

--Salary Codes----------------------------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Codes-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/MasterSalaryCodes_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Salary Codes'), 1, 
    'Create Salary Codes', 1, 'HELP/Salary Codes-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Codes-view', NULL,NULL, sysdate, 
    '/HTML/VMC/MasterSalaryCodes_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Salary Codes'), 2, 
    'View Salary Codes ', 1, 'HELP/Salary Codes-View.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Codes-Modify', NULL,NULL, sysdate, 
    '/HTML/VMC/MasterSalaryCodes_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Salary Codes'), 3, 
    'modify Salary Codes', 1, 'HELP/Salary Codes-Modify.htm');



--Schemes--------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Scheme-Create', NULL,NULL, sysdate, 
    '/HTML/AddScheme.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Schemes'), 1, 
    'Create Scheme', 1, 'HELP/Scheme-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Schemes-Search', NULL,NULL, sysdate, 
    '/HTML/schemeSearch.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Schemes'), 2, 
    'View Schemes', 1, 'HELP/View Schemes-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Schemes-Search', NULL,NULL, sysdate, 
    '/HTML/schemeSearch.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Schemes'), 3, 
    'Modify Schemes', 1, 'HELP/Modify Scheme.htm');

--Remittance Recovery------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Remittance Recovery-Create', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeCreateRemitRecovery', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 1, 
    'Create Remittance Recovery', 1, 'HELP/Create Remittance Recovery.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Remittance Recovery-Search', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeRemittanceList\&mode=view', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 2, 
    'View Remittance Recovery', 1, 'HELP/View Remittance Recovery-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'View Remittance Recovery', NULL,NULL, sysdate, 
        '/deduction/remitRecovery.do', 'submitType=beforeViewAndModifyRemitRecovery\&mode=view', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 2, 
        'View Remittance Recovery', 0, 'HELP/View Remittance Recovery.htm');
        
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Remittance Recovery-Search', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeRemittanceList\&mode=modify', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 3, 
    'Modify Remittance Recovery', 1, 'HELP/Modify Remittance Recovery-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify Remittance Recovery', NULL,NULL, sysdate, 
        '/deduction/remitRecovery.do', 'submitType=beforeViewAndModifyRemitRecovery\&mode=modify', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 3, 
        'Modify Remittance Recovery', 0, 'HELP/Modify Remittance Recovery.htm');
        
       Insert into eg_action
           (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
         Values
           (seq_eg_action.nextVal, 'Modify Remittance Recovery ', NULL,NULL, sysdate, 
            '/deduction/remitRecovery.do', 'submitType=modifyRemitRecovery', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 3, 
            'Modify Remittance Recovery', 0, 'HELP/Modify Remittance Recovery.htm');

----Party Types--------------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Party Type-Create', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforeCreate', NULL, (select id_module from eg_module where module_name='Party Types'), 1, 
    'Create Party Types', 1, 'HELP/Create Party Type.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Party Type- Create', NULL,NULL, sysdate, 
        '/deduction/PartyTypeMaster.do', 'submitType=createPartyType', NULL, (select id_module from eg_module where module_name='Party Types'), 1, 
        'Create Party Types', 0, 'HELP/Create Party Type.htm');
 
    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Party Types-Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforePartyTypeSearch\&mode=view', NULL, (select id_module from eg_module where module_name='Party Types'), 2, 
    'View Party Types', 1, 'HELP/View Party Types-Search.htm');
    
   Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Party Types- Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=getPartyTypeSearchList\&mode=view', NULL, (select id_module from eg_module where module_name='Party Types'), 2, 
    'View Party Types', 0, 'HELP/View Party Types-Search.htm');
  
    
     
    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Party Type-Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforePartyTypeSearch\&mode=modify', NULL, (select id_module from eg_module where module_name='Party Types'), 3, 
    'Modify Party Types', 1, 'HELP/Modify Party Type-Search.htm');
    
  
   Insert into eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       Values
         (seq_eg_action.nextVal, 'Modify Party Types -Search', NULL,NULL, sysdate, 
          '/deduction/PartyTypeMaster.do', 'submitType=getPartyTypeSearchList\&mode=modify', NULL, (select id_module from eg_module where module_name='Party Types'), 2, 
        'View Party Types', 0, 'HELP/View Party Types-Search.htm');
        
  
  Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Party Type- Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=modifyPartyType', NULL, (select id_module from eg_module where module_name='Party Types'), 3, 
    'Modify Party Types', 0, 'HELP/Modify Party Type-Search.htm');  
   
    
  
    
    
    
-------Contract Types---------------------------------------------------------------------------------------------------------------------



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contract Type-Create', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeCreate', NULL, (select id_module from eg_module where module_name='Contract Types'), 1, 
    'Create Contract Type', 1, 'HELP/Create Contract Type.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contract Type- Create', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=createDocumentType', NULL, (select id_module from eg_module where module_name='Contract Types'), 1, 
    'Create Contract Type', 0, 'HELP/Create Contract Type.htm');
        

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Contract Types-Search', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeDocumentTypeSearch\&mode=view', NULL, (select id_module from eg_module where module_name='Contract Types'), 2, 
    'View Contract Types', 1, 'HELP/View Contract Types-Search.htm');
    
   
   Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'View Contract Types- Search', NULL,NULL, sysdate, 
        '/deduction/DocumentTypeMaster.do', 'submitType=getDocumentTypeSearchList\&mode=view', NULL, (select id_module from eg_module where module_name='Contract Types'), 2, 
        'View Contract Types', 0, 'HELP/View Contract Types-Search.htm');
        

    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Contract Types-Search', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeDocumentTypeSearch\&mode=modify', NULL, (select id_module from eg_module where module_name='Contract Types'), 3, 
    'Modify Contract Types', 1, 'HELP/Modify Contract Types-Search.htm');
    
    
    
         Insert into eg_action
               (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
             Values
               (seq_eg_action.nextVal, 'Modify Contract Types -Search', NULL,NULL, sysdate, 
                '/deduction/DocumentTypeMaster.do', 'submitType=getDocumentTypeSearchList\&mode=modify', NULL, (select id_module from eg_module where module_name='Contract Types'), 2, 
            'View Contract Types', 0, 'HELP/View Contract Types-Search.htm');
    
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify Contract Types- Search', NULL,NULL, sysdate, 
        '/deduction/DocumentTypeMaster.do', 'submitType=modifyDocumentType', NULL, (select id_module from eg_module where module_name='Contract Types'), 3, 
        'Modify Contract Types', 0, 'HELP/Modify Contract Types-Search.htm');
 
    
     
    -----Recovery Masters-------------------------------------------------------------------------------------


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery-Create', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 1, 
    'Create Recovery', 1, 'HELP/Create Recovery.htm');
    
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Recovery- Create', NULL,NULL, sysdate, 
        '/deduction/recoverySetupMaster.do', 'submitType=createRecoveryMaster', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 1, 
        'Create Recovery', 0, 'HELP/Create Recovery.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Recoveries-Search', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toView', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 2, 
    'View Recoveries', 1, 'HELP/View Recovery.htm');
    
    
    
  


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Recoveries-Search', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toModify', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 3, 
    'Modify Recoveries', 1, 'HELP/Modify Recovery.htm');
    
   Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Recoveries- Search', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=modifyRecoveryMaster', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 3, 
    'Modify Recoveries', 0, 'HELP/Modify Recovery.htm');
  
    
    
   -- modifyRecoveryMaster

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'LOGIN', TO_DATE('06/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/eGov_AP.htm', 'LOGIN', 0);


    
--------------------------------------------------------------------------------------------------------------------------------------------------



--SQL Statement which produced this data:
--  SELECT * from eg_ACTION WHERE QUERYPARAMS='showMode=new'
--
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
  (seq_eg_action.nextVal, 'Receipt Accounting-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Receipts'), 1, 'Receipt Accounting', 0, '../../HelpAssistance/Miscellaneous%20Receipts.htm');
--Insert into eg_action
--   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
-- Values
--   (seq_eg_action.nextVal, 'Salary Journal-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/SalaryBill_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Salary Bills'), 1, 'Salary Bill', 0, 'HELP/Salary Bill.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payments-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/AdvanceJournal_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Payments'), 3, 'Advance Payments-View', 0, 'HELP/Advance Payments.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_General_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Journal Vouchers'), 1, 'Journal Voucher-View', 0, 'HELP/Journal Voucher.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Contra Entries'), 1, 'Cash Deposit-View', 0, 'HELP/Cash Deposit.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Contra Entries'), 2, 'Cash Withdrawal-View', 0, 'HELP/Cash Withdrawal.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Contra Entries'), 3, 'Bank to Bank Transfer-View', 0, 'HELP/Bank to Bank Transfer.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay in Slip-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/PayInSlip_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Contra Entries'), 4, 'Pay in Slip-View', 0, 'HELP/Pay in.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Create Supplier/Contractor-View', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/RelationMod_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Supplier/Contractors'), 1, 'Create Supplier/Contractor-View', 0, 'HELP/Create Supplier/Contractor.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Transaction-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/BankTransaction.jsp', 'showMode=view', (select id_module from eg_module where module_name='MIS Reports'), 5, 'Bank Transaction-View', 0, 'HELP/Bank Transaction.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/SalaryBillRegister_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Salary Bills'), 1, 'Salary Bill-View', 0, 'HELP/Create Salary Bill.htm');
--Insert into eg_action
 --  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 --Values
  -- (seq_eg_action.nextVal, 'Salary Codes-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/MasterSalaryCodes_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Salary Codes'), 1, 'Salary Codes-View', 0, 'HELP/Create Salary Codes.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Scheme-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/AddScheme.jsp', 'showMode=view', (select id_module from eg_module where module_name='Schemes'), 1, 'Scheme-View', 0, 'HELP/Create Scheme.htm');



---------------
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt Accounting-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Receipts'), 1, 'Receipt Accounting-Modify', 0, '../../HelpAssistance/Miscellaneous%20Receipts.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payments-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/AdvanceJournal_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Payments'), 3, 'Advance Payments-Modify', 0, 'HELP/Advance Payments.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_General_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Journal Vouchers'), 1, 'Journal Voucher-Modify', 0, 'HELP/Journal Voucher.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Contra Entries'), 1, 'Cash Deposit-Modify', 0, 'HELP/Cash Deposit.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Contra Entries'), 2, 'Cash Withdrawal-Modify', 0, 'HELP/Cash Withdrawal.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Contra Entries'), 3, 'Bank to Bank Transfer-Modify', 0, 'HELP/Bank to Bank Transfer.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay in Slip-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/PayInSlip_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Contra Entries'), 4, 'Pay in Slip-Modify', 0, 'HELP/Pay in.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier/Contractor-Modify', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/RelationMod_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Supplier/Contractors'), 1, 'Supplier/Contractor-Modify', 0, 'HELP/Create Supplier/Contractor.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Transaction-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/BankTransaction.jsp', 'showMode=edit', (select id_module from eg_module where module_name='MIS Reports'), 5, 'Bank Transaction-Modify', 0, 'HELP/Bank Transaction.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/SalaryBillRegister_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Salary Bills'), 1, 'Salary Bill-Modify', 0, 'HELP/Create Salary Bill.htm');
--Insert into eg_action
 --  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 --Values
   --(seq_eg_action.nextVal, 'Salary Codes-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/MasterSalaryCodes_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Salary Codes'), 1, 'Create Salary Codes', 0, 'HELP/Create Salary Codes.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Scheme-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/AddScheme.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Schemes'), 1, 'Scheme-Modify', 1, 'HELP/Create Scheme.htm');
-----------
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt Accounting-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Receipts'), 1, 'Receipt Accounting-Reverse', 0, '../../HelpAssistance/Miscellaneous%20Receipts.htm');
--Insert into eg_action
--   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
-- Values
--   (seq_eg_action.nextVal, 'Salary Journal-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/SalaryBill_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Salary Journal-Reverse', 0, 'HELP/Salary Bill.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payments-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/AdvanceJournal_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Payments'), 3, 'Advance Payments-Reverse', 0, 'HELP/Advance Payments.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_General_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Journal Vouchers'), 1, 'Journal Voucher-Reverse', 0, 'HELP/Journal Voucher.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Contra Entries'), 1, 'Cash Deposit-Reverse', 0, 'HELP/Cash Deposit.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Contra Entries'), 2, 'Cash Withdrawal', 0, 'HELP/Cash Withdrawal.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Contra Entries'), 3, 'Bank to Bank Transfer-Reverse', 0, 'HELP/Bank to Bank Transfer.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay in Slip-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/PayInSlip_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Contra Entries'), 4, 'Pay in Slip-Reverse', 0, 'HELP/Pay in.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier/Contractor-Reverse', TO_DATE('05/01/2008 01:37:52', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/RelationMod_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Supplier/Contractors'), 1, 'Supplier/Contractor-Reverse', 0, 'HELP/Create Supplier/Contractor.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Transaction-Reverse', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/Reports/BankTransaction.jsp', 'showMode=modify', (select id_module from eg_module where module_name='MIS Reports'), 5, 'Bank Transaction-Reverse', 0, 'HELP/Bank Transaction.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill -Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/SalaryBillRegister_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Salary Bills'), 1, 'Salary Bill Register-Modify', 0, 'HELP/Create Salary Bill.htm');








Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Party Type-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/PartyTypeMaster.do', 'submitType=beforeViewAndModifyPartyType\&mode=view', (select id_module from eg_module where module_name='Party Types'), 1, 'Party Type-View', 0, 'HELP/Party Type-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Party Type-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/PartyTypeMaster.do', 'submitType=beforeViewAndModifyPartyType\&mode=view', (select id_module from eg_module where module_name='Party Types'), 1, 'Party Type-Modify', 0, 'HELP/Create Party Type-Modify.htm');



--------------------------------------------

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contract Type-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/DocumentTypeMaster.do', 'submitType=beforeViewAndModifyDocumentType\&mode=view', (select id_module from eg_module where module_name='Contract Types'), 1, 'Contract Type-View', 0, 'HELPAssistance/Contract Type-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contract Type-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/DocumentTypeMaster.do', 'submitType=beforeViewAndModifyDocumentType\&mode=modify', (select id_module from eg_module where module_name='Contract Types'), 1, 'Contract Type-Modify', 0, 'HELPAssistance/Contract Type-Modify.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/recoverySetupMaster.do', 'submitType=ViewRecoveryMaster', (select id_module from eg_module where module_name='Recovery Masters'), 1, 'Recovery-View', 0, 'HELP/Recovery-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/recoverySetupMaster.do', 'submitType=beforeModifyRecoveryMaster', (select id_module from eg_module where module_name='Recovery Masters'), 1, 'Recovery-Modify', 0, 'HELP/Recovery-Modify.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Remittance Recovery-View', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/remitRecovery.do', 'submitType=getRemittanceList\&mode=view', (select id_module from eg_module where module_name='Remittance Recovery'), 1, 'Remittance Recovery-View', 0, 'HELP/Remittance Recovery-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Remittance Recovery-Modify', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/remitRecovery.do', 'submitType=getRemittanceList\&mode=modify', (select id_module from eg_module where module_name='Remittance Recovery'), 1, 'Remittance Recovery-Modify', 0, 'HELP/Remittance Recovery-Modify.htm');


--------------------------------------------
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Works Bill-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/worksBill.do', 'submitType=beforeViewModify\&mode=view', (select id_module from eg_module where module_name='Bill Registers'), 1, 'Works Bill-View', 0, 'HELP/Works Bill-View.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Works Bill-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/worksBill.do', 'submitType=beforeViewModify\&mode=modify', (select id_module from eg_module where module_name='Bill Registers'), 1, 'Works Bill-Modify', 0, 'HELP/Works Bill-View.htm');
-------------------------------------------------------------------------------------------------------


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/payment/payment.do', 'submitType=beforeViewAndModifyPayment\&mode=view', (select id_module from eg_module where module_name='Payments'), 1, 'Bill Payments-View', 0, 'HelpAssistance\AP\Bill Payment_AP-View.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/payment/payment.do', 'submitType=beforeViewAndModifyPayment\&mode=modify', (select id_module from eg_module where module_name='Payments'), 1, 'Bill Payments-Modify', 0, 'HelpAssistance\AP\Bill Payment_AP-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments- Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/payment/payment.do', 'submitType=updatePayment', (select id_module from eg_module where module_name='Payments'), 1, 'Bill Payments-Modify', 0, 'HelpAssistance\AP\Bill Payment_AP-Modify.htm');


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/payment/payment.do', 'submitType=beforeViewAndModifyPayment\&mode=reverse', (select id_module from eg_module where module_name='Payments'), 1, 'Bill Payments-Reverse', 0, 'HelpAssistance\AP\Bill Payment_AP-revesre.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments- Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/payment/payment.do', 'submitType=reversePayment', (select id_module from eg_module where module_name='Payments'), 1, 'Bill Payments-Reverse', 0, 'HelpAssistance\AP\Bill Payment_AP-revesre.htm');



--Direct  Payments---------------------------------------------------------------------------------


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Direct Direct Bank Payments-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/DirectBankPayment_VMC.jsp', 'showMode=viewBank', (select id_module from eg_module where module_name='Payments'), 1, 'Direct Bank Payments-View', 0, 'HelpAssistance\AP\Direct Bank Payment_AP-View.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Direct Bank Payments-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/DirectBankPayment_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Payments'), 1, 'Direct Bank Payments-Modify', 0, 'HelpAssistance\AP\Direct Bank Payment_AP-Modify.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Direct Bank Payments-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/DirectBankPayment_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Payments'), 1, 'Direct Bank Payments-Reverse', 0, 'HelpAssistance\AP\Direct Bank Payment_AP-revesre.htm');





--/HTML/CompanyDetailAdd.htm



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined-Codes-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/SubCodesAdd.htm', '', (select id_module from eg_module where module_name='Masters'), 1, 'User-defined-Codes-Create/Modifyy', 0,
   'HelpAssistance/AP/User-defined Codes-Create.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fund Source-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FundSourceAdd.htm', '', (select id_module from eg_module where module_name='Masters'), 1, 'Fund Source-Create/Modify', 0,
   'HelpAssistance/AP/Fund Source-Create/Modify.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fund-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FundAdd.htm', '', (select id_module from eg_module where module_name='Set-up'), 0, 'Fund-Create/Modify', 0,
   'HelpAssistance/AP/Fund-CreateModify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Function-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FunctionAdd.htm', '', (select id_module from eg_module where module_name='Set-up'), 0, 'Function-Create/Modify', 0,
   'HelpAssistance/AP/Function-Create/Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'ULB-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/CompanyDetailAdd.htm', '', (select id_module from eg_module where module_name='Set-up'), 0, 'ULB-Create/Modify', 0,
   'HelpAssistance/AP/ULB-Create/Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Journal-Create', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/JV_Salary_VMC.jsp', 'showMode=new', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Salary Journal-Create', 0,
   'HelpAssistance/AP/Salary Journal-Create.htm');
  
  Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Journal-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
  '/HTML/VMC/JV_Salary_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Salary Journal-View', 0,
   'HelpAssistance/AP/Salary Journal-View.htm');


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Journal-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/JV_Salary_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Salary Journal-Modify', 0, 'HELP/Salary Bill.htm');


--/HTML/VMC/SupplierJournal_VMC.jsp?cgNumber=SJV20865&showMode=edit

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier Journal-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/SupplierJournal_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Supplier Journal-Modify', 0,
   'HelpAssistance/AP/Supplier Journal-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier Journal-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/SupplierJournal_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Supplier Journal-View', 0,
   'HelpAssistance/AP/Supplier Journal-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier Journal-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/SupplierJournal_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Supplier Journal-Reverse', 0,
   'HelpAssistance/AP/Supplier Journal-Reverse.htm');


--/EGF/HTML/VMC/ContractorJournal_VMC.jsp


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Journal-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContractorJournal_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contractor Journal-Modify', 0,
   'HelpAssistance/AP/Contractor Journal-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Journal-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContractorJournal_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contractor Journal-View', 0,
   'HelpAssistance/AP/Contractor Journal-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Journal-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContractorJournal_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contractor Journal-Reverse', 0,
   'HelpAssistance/AP/Contractor Journal-Reverse.htm');

--HTML/VMC/ContingencyJournal_VMC.jsp

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingency Journal-Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContingencyJournal_VMC.jsp', 'showMode=edit', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contingency Journal-Modify', 0,
   'HelpAssistance/AP/Contingency Journal-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingency Journal-View', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContingencyJournal_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contingency Journal-View', 0,
   'HelpAssistance/AP/Contingency Journal-View.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingency Journal-Reverse', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/VMC/ContingencyJournal_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Bill Registers'), 0, 'Contingency Journal-Reverse', 0,
   'HelpAssistance/AP/Contingency Journal-Reverse.htm');


--
--SQL Statement which produced this data:
--  select * from eg_action where url like '%.do' and queryparams like 'submitType=beforeCreate%'
--
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Works Bill', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/worksBill.do', 'submitType=beforeCreate', (select id_module from eg_module where module_name='Bill Registers'), 1, 'Create Works Bill', 0, 'HELP/Create Works Bill.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingent Bill', TO_DATE('05/01/2008 01:37:53', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/cbill.do', 'submitType=beforeCreateCBill', (select id_module from eg_module where module_name='Bill Registers'), 2, 'Create Contingent Bill', 0, 'HELP/Create Contingent Bill.htm');

  
--/HTML/VMC/WorksDetailEnq_VMC.jsp
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/WorksDetailAdd_VMC.jsp', 'showMode=modify', (select id_module from eg_module where module_name='Procurement Orders'), 1, 'Procurement Order-Modify', 0, 'HELP/Create Works Bill.htm');

  --showMode=view 



--  /deduction/recoverySetupMaster.do
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery Master-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/recoverySetupMaster.do', 'submitType=beforeModifyRecoveryMaster', (select id_module from eg_module where module_name='Recovery Masters'), 1, 'Recovery Master-View', 0, 'HELP/Create Works Bill.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery Master-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/deduction/recoverySetupMaster.do', 'submitType=viewRecoveryMaster', (select id_module from eg_module where module_name='Recovery Masters'), 1, 'Recovery Master-Modify', 0, 'HELP/Create Works Bill.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Approve Bill Registers', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/inbox/Inbox.jsp', '', (select id_module from eg_module where module_name='Bill Registers'), 1, 'Approve Bill Registers', 0, 'HELP/Create Works Bill.htm');


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/WorksDetailEnq_VMC.jsp', 'showMode=view', (select id_module from eg_module where module_name='Procurement Orders'), 1, 'Procurement Order-View', 0, 'HELP/Create Works Bill.htm');
--submitType=beforeModifyRecoveryMaster




--/billsaccounting/cbill.do?submitType=beforeViewModify&mode=modify&expType=Contingency




Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingent Bill-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/cbill.do', 'submitType=beforeViewModify\&mode=modify\&expType=Contingency', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Contingent Bill-Modify', 0, 'HELP/Create Works Bill.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contingent Bill-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/billsaccounting/cbill.do', 'submitType=beforeViewModify\&mode=view\&expType=Contingency', (select id_module from eg_module where module_name='Bills Accounting'), 1, 'Contingent Bill-View', 0, 'HELP/Create Works Bill.htm');


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year Create/Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/FinancialYearAdd.htm', '', (select id_module from eg_module where module_name='Period End Activities'), 1, 'Financial Year Create/Modify', 0, 'HELP/Financial Year Create-Modify.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Collection/Payment point Create/Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/BillCollector.htm', '', (select id_module from eg_module where module_name='Masters'), 1, 'Collection/Payment point Create/Modify', 0, 'HELP/Financial Year Create-Modify.htm');


--HTML/BankBranchEnquiry.htm

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'BankBranch-modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/BankBranchEnquiry.htm', '', (select id_module from eg_module where module_name='Period End Activities'), 1, 'BankBranch-Modify', 0, 'HELP/Financial Year Create-Modify.htm');



--insert into eg_roleaction_map(roleid,actionid) select 5,id from eg_action;


--update  eg_action set action_help_url='/HelpAssistance/AP/'||name||'_AP.htm' where id=id;

  update  eg_action set action_help_url=replace('/HelpAssistance/AP/'||name||'_AP.htm',' ','')  where id=id; 

--menu tree related changes end here-----------------------------------------------

update eg_user set isactive=1 where isactive is null;


--object creation for jbpm 

Insert into eg_object_type
  (ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE)
 Values
  (seq_object_type.nextVal, 'CBill', 'Contingent Bill', sysdate);
Insert into eg_object_type
  (ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE)
 Values
  (seq_object_type.nextVal, 'WorksBill', 'SupplierContractors Bill', sysdate);



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Administration', TO_DATE('08/04/2008 10:17:40', 'MM/DD/YYYY HH24:MI:SS'), '/egi/eGov.jsp', (select id_module from eg_module where module_name='EGF'), 1, 'Administration', 0, '/HelpAssistance/Administartion.htm');

update eg_action set url='/eGov.jsp', module_id=(select id_module from eg_module where module_name='EGF') where name='LOGIN';


--
--SQL Statement which produced this data:
--  select * from eg_action  where module_id in(select id_module from eg_module where parentid=39)order by id desc
--



---insert ll actions into role_map 



insert into eg_roleaction_map select (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME IN ('SuperUser','Super User','SUPER USER')),id from eg_action where id >=(select id from eg_action where name='Cancellation of  Vouchers-Search');
--insert into eg_roleaction_map select 1,id from eg_action ;

--------------------------end of admin------------------------------------

--For Summary Statement Report

UPDATE eg_billregister b SET b.STATUSID=( SELECT id FROM egw_status es WHERE UPPER(es.MODULETYPE)=UPPER('WORKSBILL') AND UPPER(es.DESCRIPTION)=UPPER('Passed')) 
WHERE UPPER(b.BILLSTATUS)=UPPER('Passed') AND UPPER(b.EXPENDITURETYPE)=UPPER('Works'); 


----------------------------------------------------------------------------

 UPDATE  EG_ACTION SET IS_ENABLED=1, module_id=(select id_module from eg_module where module_name='Supplier/Contractors')
 WHERE NAME='Modify Supplier/Contractor-Search' 
AND URL='/HTML/VMC/Relation_VMC.jsp' AND QUERYPARAMS='showMode=modify';


commit;


#DOWN

