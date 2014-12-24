#UP

----------- MAIN--------------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'EGF',  TO_Date( '09/24/2008 11:51:32 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL, '/EGF'
, NULL, 'EGF', NULL); 

----------- EGF--------------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Transactions',  TO_Date( '09/24/2008 11:51:32 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, NULL, NULL, (select id_module from eg_module where module_name='EGF'), 'Transactions', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Reports',  TO_Date( '09/24/2008 11:51:32 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='EGF'), 'Reports', 2); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Masters',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='EGF'), 'Masters', 3); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Processing',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, NULL, NULL, (select id_module from eg_module where module_name='EGF'), 'Processing', 4); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Set-up',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='EGF'), 'Set-up', 5); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Deductions',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, NULL, NULL, (select id_module from eg_module where module_name='EGF'), 'Deductions', 7); 

----  TRANSACTIONS--------------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Receipts',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='Transactions'), 'Receipts', 1); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Bills Accounting',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 'Bills Accounting', 2); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Bills Accounting ',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 'Bills Accounting ', 3); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Bill Payments',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='Transactions'), 'Bill Payments', 4); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Payments',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='Transactions'), 'Payments', 5); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Journal Proper',  TO_Date( '09/24/2008 11:51:33 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 'Journal Proper', 6); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Contra Entries',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 'Contra Entries', 7); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'BRS',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 'BRS', 8); 


-------- Bills Accounting----------------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Bill Registers',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Bills Accounting '), 'Bill Registers', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, ' Bills Accounting ',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Bills Accounting '), ' Bills Accounting ', 2); 


-----Bills Accoutning under Bill register----


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Salary Bills ',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name=' Bills Accounting '), 'Salary Bills', 1); 

-----Reports---------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Financial Statements',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Reports'), 'Financial Statements', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Accounting Records',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Reports'), 'Accounting Records', 2); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'MIS Reports',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, NULL, NULL, (select id_module from eg_module where module_name='Reports'), 'MIS Reports', 3); 

----------- Masters----------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Chart of Accounts',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Masters'), 'Chart of Accounts', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Procurement Orders',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Masters'), 'Procurement Orders', 2); 

  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Supplier/Contractors',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),3,'Supplier/Contractors');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Schemes',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),4,'Schemes');


Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_modulemaster.nextVal, 'Report Schedule Mapping',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),1,'Report Schedule Mapping');
  
  Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
   Values
    (seq_modulemaster.nextVal, 'Salary Codes',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),2,'Salary Codes');


----------- DEDUCTIONS----------

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Master',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select id_module from eg_module where module_name='Deductions'), 'Master', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Party Types',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, NULL, NULL, (select id_module from eg_module where module_name='Master'), 'Party Types', 1); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Contract Types',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Master'), 'Contract Types', 2); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Recovery Masters',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Master'), 'Recovery Masters', 3); 

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
seq_modulemaster.nextVal, 'Remittance Recovery',  TO_Date( '09/24/2008 11:51:34 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, NULL, (select id_module from eg_module where module_name='Deductions'), 'Remitance Recovery', 2); 


commit;


INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
seq_eg_action.nextVal, 'Cancellation of  Vouchers-Search', NULL, NULL,  TO_Date( '09/22/2008 07:10:18 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/HTML/CancelVoucher.htm', NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 9, 'Cancel Vouchers', 1, '/HelpAssistance/AP/CancellationofVouchers-Search_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
seq_eg_action.nextVal, 'Approve Vouchers', NULL, NULL,  TO_Date( '09/22/2008 07:10:18 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/HTML/ConfirmVoucher.htm?', NULL, NULL, (select id_module from eg_module where module_name='Transactions'), 10, 'Approve Vouchers', 1, '/HelpAssistance/AP/ApproveVouchers_AP.htm'); 
COMMIT;

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'LOGIN', TO_DATE('06/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), '/eGov.jsp', 'LOGIN', 0);


delete from eg_roleaction_map where actionid in (select id from eg_action where module_id=(select id_module from eg_module where module_name='Transactions'));
--delete from eg_action where module_id=(select id_module from eg_module where module_name='Transactions');
commit;


--- RECEIPTS

delete from eg_roleaction_map where actionid in (select id from eg_action where module_id=(select id_module from eg_module where module_name='Receipts'));
delete from eg_action where module_id=(select id_module from eg_module where module_name='Receipts');
commit;


Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
  
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Property Tax Collection-create', NULL,NULL, sysdate, 
    '/HTML/PT_Field.htm', 'showMode=new', NULL, (select id_module from eg_module where 
    module_name='Receipts'), seq_order_number.nextval, 
    'Property Tax Collection-create', 1, 'HelpAssistance/Property Tax Collection-create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Miscellaneous Receipt-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where 
    module_name='Receipts'), seq_order_number.nextval, 
    'Miscellaneous Receipt-Create', 1, 'HelpAssistance/Miscellaneous Receipts-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'View Receipts-Search', 1, 'HelpAssistance/View Receipts.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'Modify Receipts-Search', 1, 'HelpAssistance/Modify Receipts-Search.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'Reverse Receipts-Search', 1, 'HelpAssistance/Reverse Receipts-Search.htm');
	
commit;	


--------------------Journal voucher

delete from eg_roleaction_map where actionid in (select id from eg_action where module_id=(select id_module from eg_module where module_name='Journal Proper'));
delete from eg_action where module_id=(select id_module from eg_module where module_name='Journal Proper');
commit;


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_General_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Journal Proper'), 1, 
    'Journal Voucher-Create', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General&showMode=view', NULL, (select id_module from eg_module where module_name='Journal Proper'), 2, 
    'View Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General&showMode=edit', NULL, (select id_module from eg_module where module_name='Journal Proper'), 3, 
    'Modify Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=General&showMode=modify', NULL, (select id_module from eg_module where module_name='Journal Proper'), 4, 
    'Reverse Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');

commit;	

-------- contra entries

delete from eg_roleaction_map where actionid in (select id from eg_action where module_id=(select id_module from eg_module where module_name='Contra Entries'));
delete from eg_action where module_id=(select id_module from eg_module where module_name='Contra Entries');
commit;


drop sequence seq_order_number;
Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 1, 
    'Cash Deposit-Create', 1, 'HelpAssistance/Cash Deposit.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Create', 1, 'HelpAssistance/Cash Withdrawal.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Deposit-Create', NULL,NULL, sysdate, 
    '/HTML/PayInSlip.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Cheque Deposit-Create', 1, 'HelpAssistance/Pay in.htm');
    
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Create', 1, 'HelpAssistance/Bank to Bank Transfer.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 5, 
    'View Contra Entries-Search', 1, 'HelpAssistance/View Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 6, 
    'Modify Contra Entries-Search', 1, 'HelpAssistance/Modify Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 7, 
    'Reverse Contra Entries-Search', 1, 'HelpAssistance/Reverse Contra Entries-Search.htm');


commit;	


----
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
    
   commit;
   
   ----Bills accounting without BR
  
   Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, 'Contractor Bill-Create', NULL,NULL, sysdate, 
       '/HTML/ContractorJournal.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 1, 
       'Contractor Bill-Create', 1, 'HelpAssistance/Contractor Bill-Create.htm');
   Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, 'Supplier Bill-Create', NULL,NULL, sysdate, 
       '/HTML/SupplierJournal.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 2, 
       'Supplier Bill-Create', 1, 'HelpAssistance/Supplier Bill-Create.htm');
       
   Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, 'Salary Bill-create', NULL,NULL, sysdate, 
       '/HTML/JV_Salary.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 3, 
       'Salary Bill-Create', 1, 'HelpAssistance/Salary Bill-create.htm');
   
   Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, 'View  Bills-Search', NULL,NULL, sysdate, 
       '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 4, 
       'View  Bills-Search', 1, 'HelpAssistance/View  Bills-Search.htm');
       
       Insert into eg_action
          (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
        Values
          (seq_eg_action.nextVal, 'Modify  Bills-Search', NULL,NULL, sysdate, 
           '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 5, 
       'Modify  Bills-Search', 1, 'HelpAssistance/Modify  Bills-Search.htm');
       
       Insert into eg_action
          (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
        Values
          (seq_eg_action.nextVal, 'Reverse Bills-Search', NULL,NULL, sysdate, 
           '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 6, 
       'Reverse Bills-Search', 1, 'HelpAssistance/Reverse Bills-Search.htm');
   
   commit;	
 
---------------------------------
-------- Bills Accounting-- bill registers-- salary bills
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill-Create', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegister_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Salary Bills '), 1, 
    'Create Salary Bill', 1, 'HELP/Create Salary Bill.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegisterSearch_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Salary Bills '), 2, 
    'View Salary Bills', 1, 'HELP/View Salary Bills-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBillRegisterSearch_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Salary Bills '), 3, 
    'Modify  Salary Bills', 1, 'HELP/Create Salary Bill.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Approve Salary Bills-Search', NULL,NULL, sysdate, 
    '/HTML/VMC/ApproveSalaryBillRegister_VMC.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Salary Bills '), 4, 
    'Approve Salary Bills', 1, 'HELP/Approve Salary Bill.htm');
    
-------- Bills Accounting-- bill registers
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
    
-------- Bills Accounting-- salary bills
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bills ', NULL,NULL, sysdate, 
    '/HTML/VMC/SalaryBill_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name=' Bills Accounting '), 1, 
    'Salary Bills', 1, 'HELP/Salary Bill.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View  Bills-Search ', NULL,NULL, sysdate, 
    '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=view', NULL, (select id_module from eg_module where module_name=' Bills Accounting '), 2, 
    'View  Bills', 1, 'HELP/View  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify  Bills-Search ', NULL,NULL, sysdate, 
        '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=edit', NULL, (select id_module from eg_module where module_name=' Bills Accounting '), 3, 
    'Modify  Bills', 1, 'HELP/Modify  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Reverse Bills-Search ', NULL,NULL, sysdate, 
        '/HTML/VMC/JournalVoucherSearch_VMC.jsp', 'journalType=Bill\&showMode=modify', NULL, (select id_module from eg_module where module_name=' Bills Accounting '), 4, 
    'Reverse Bills', 1, 'HELP/Reverse Bills-Search.htm');


-- Bill Payments

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments', NULL,NULL, sysdate, 
    '/payment/payment.do', 'submitType=beforeSearchAllBills', NULL, (select id_module from eg_module where module_name='Bill Payments'), 2, 
    'Bill Payments', 1, 'HELP/Bill Payments.htm');
    
    
   Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bill Payments ', NULL,NULL, sysdate, 
    '/payment/payment.do', 'submitType=searchAllBills', NULL, (select id_module from eg_module where module_name='Bill Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
    
    
     Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Bill Payments', NULL,NULL, sysdate, 
        '/payment/payment.do', 'submitType=beforeCreatePayment', NULL, (select id_module from eg_module where module_name='Bill Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
     
    
    Insert into eg_action
            (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
          Values
            (seq_eg_action.nextVal, ' Bill Payments ', NULL,NULL, sysdate, 
             '/payment/payment.do', 'submitType=createPayment', NULL, (select id_module from eg_module where module_name='Bill Payments'), 2, 
    'Bill Payments', 0, 'HELP/Bill Payments.htm');
  
   

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID,ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Direct Bank Payments', NULL,NULL, sysdate, 
    '/HTML/VMC/DirectBankPayment_VMC.jsp', 'showMode=paymentBank', NULL, (select id_module from eg_module where module_name='Bill Payments'), 1, 
    'Direct Bank Payments', 1, 'HelpAssistance/AP/Cash Payment_AP.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payment', NULL,NULL, sysdate, 
    '/HTML/VMC/AdvanceJournal_VMC.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bill Payments'), 3, 
    'Advance Payment', 1, 'HELP/Advance Payments.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Payments-Search ', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=view', NULL, (select id_module from eg_module where module_name='Bill Payments'), 4, 
    'View Payments', 1, 'HELP/View Payments-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' View Payments- Search', NULL,NULL, sysdate, 
        '/payment/PaymentVhSearch.jsp', 'submitType=searchPaymentVouchers\&mode=view', NULL, (select id_module from eg_module where module_name='Bill Payments'), 4, 
        'View Payments', 0, 'HELP/View Payments-Search.htm');
 
 
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Payments-Search ', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=modify', NULL, (select id_module from eg_module where module_name='Bill Payments'), 5, 
    'Modify Payments', 1, 'HELP/Modify Payments-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Modify Payments- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do?', 'submitType=searchPaymentVouchers\&mode=modify', NULL, (select id_module from eg_module where module_name='Bill Payments'), 5, 
        'Modify Payments', 0, 'HELP/Modify Payments-Search.htm');
  
    
    
    
   

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Payments-Search ', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=reverse', NULL, (select id_module from eg_module where module_name='Bill Payments'), 6, 
    'Reverse Payments', 1, 'HELP/Reverse Payments-Search.htm');
    
   
   Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, ' Reverse Payments- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do', 'submitType=searchPaymentVouchers\&mode=reverse', NULL, (select id_module from eg_module where module_name='Bill Payments'), 6, 
    'Reverse Payments', 0, 'HELP/Reverse Payments-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Generate Payment Advice-Search', NULL,NULL, sysdate, 
    '/payment/PaymentVhSearch.jsp', 'mode=paymentAdvice', NULL, (select id_module from eg_module where module_name='Bill Payments'), 7, 
    'Generate Payment Advice', 1, 'HELP/Generate Payment Advice-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Generate Payment Advice- Search', NULL,NULL, sysdate, 
        '/payment/paymentAdvice.do', 'submitType=searchPaymentVouchers\&mode=paymentAdvice', NULL, (select id_module from eg_module where module_name='Bill Payments'), 7, 
        'Generate Payment Advice', 0, 'HELP/Generate Payment Advice-Search.htm');
--single payment

drop sequence seq_order_number;
Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Payment-Create', NULL, NULL, sysdate, 
    '/HTML/DirectBankPayment.htm', 'showMode=paymentBank', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Bank Payment-Create', 1, 'HelpAssistance/Bank Payment-Create.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Payments-Create', NULL, NULL, sysdate, 
    '/HTML/VMC/DirectCashPayment_VMC.jsp', 'showMode=paymentCash', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Cash Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payments-Create','' , NULL, sysdate, 
    '/HTML/AdvanceJournal.htm', '', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Advance Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay Supplier/Contractor-Create', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPayment.jsp', 'showMode=searchPay', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Pay Supplier/Contractor-Create', 1, 'HelpAssistance/Pay Supplier/Contractor-Create');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Payments-Create', NULL, NULL, sysdate, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Salary Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Payments-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'View Payments-Search', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Payment-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Modify Payment-Search', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Payments-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Reverse Payments-Search', 1, 'HelpAssistance/');

	commit;
	
-- reports - Financial Statements

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
  
INSERT INTO eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
VALUES
(seq_eg_action.NEXTVAL, 'Audit Trail',SYSDATE, '/Reports/voucherList.jsp', '', (SELECT id_module FROM eg_module WHERE module_name='MIS Reports'), 13, 'Audit Trail', 1, 'HelpAssistance/AP/AuditTrail_AP.htm');
   
   
   
--Chart Of Accounts----------------------------------------------------------------------------------------------------------------------
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
seq_eg_action.NEXTVAL, 'Chart Of Accounts', NULL, NULL, NULL, '/eGov_COA.jsp', 'window=left', NULL, (SELECT id_module FROM eg_module WHERE module_name='Chart of Accounts')
, 1, 'Chart Of Accounts', 1, '/HelpAssistance/AP/Chart Of Accounts_AP.htm'); 

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


commit;

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
    '/HTML/VMC/Relation_VMC.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 3, 
    'Modify Supplier/Contractor', 1, 'HELP/Modify Supplier/Contractor-Search.htm');
	
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

	commit;

--Masters---------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Code-Screen Mappings', NULL,NULL, sysdate, 
    '/HTML/ftServicesmap.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 5, 
    'Code-Screen Mappings', 1, 'HELP/Code-Screen Mapping');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Creditors Recoveries', NULL,NULL, sysdate, 
    '/HTML/TdsEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 6, 
    'Creditors Recoveries', 1, 'HELP/Creditors Recoveries.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined Codes', NULL,NULL, sysdate, 
    '/HTML/SubCodesEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 7, 
    'User-defined Codes', 1, 'HELP/User-defined Codes.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Source of Financing', NULL,NULL, sysdate, 
    '/HTML/FundSource.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 8, 
    'Source of Financing', 1, 'HELP/Source of Financing.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Collection/Payment point', NULL,NULL, sysdate, 
    '/HTML/BillCollector.htm', '', NULL, (select id_module from eg_module where module_name='Masters'),9, 
    'Collection/Payment point', 1, 'HELP/Collection/Payment point.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Accounting Entity', NULL,NULL, sysdate, 
    '/HTML/AccountingEntity.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 10, 
    'Accounting Entity', 1, 'HELP/Accounting Entity.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Setup Cheque in Hand/Cash in Hand', NULL,NULL, sysdate, 
    '/HTML/setupCashCheckInHand.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 11, 
    'Setup Cheque in Hand/Cash in Hand', 1, 'HELP/Setup Cheque in Hand/Cash in Hand.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Tax Setup-Enquiry', NULL,NULL, sysdate, 
    '/HTML/TaxCodeMapEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 12, 
    'Tax Setup-Enquiry', 1, 'HelpAssistance/Tax Setup-Enquiry.htm');

commit;	
    
--Processing-----------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year-SetUp', NULL,NULL, sysdate, 
    '/HTML/FinancialYearEnq.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 1, 
    'Financial Year', 1, 'HELP/Financial Year.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Opening Balance-SetUp', NULL,NULL, sysdate, 
   '/HTML/OpeningBalance.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 2, 
    'Opening Balance', 1, 'HELP/Opening Balance.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fiscal periods-SetUp', NULL,NULL, sysdate, 
    '/HTML/SetUp.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 3, 
    'Close Period', 1, 'HELP/Close Period.htm');
commit;

--Report Schedule Mapping-----------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Report Schedule Mapping-Create', NULL,NULL, sysdate, 
    '/Reports/ScheduleMaster.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 1, 
    'Report Schedule Mapping-Create', 1, 'HelpAssistance/Report Schedule Mapping-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Report Schedule Mapping-Modify', NULL,NULL, sysdate, 
    '/Reports/ScheduleMaster.jsp', 'showMode=edit', NULL,'' , 0, 
    'Report Schedule Mapping-Modify', 0, 'HelpAssistance/Report Schedule Mapping-Modify.htm');
    
  Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Schedules-Search ', NULL,NULL, sysdate, 
        '/Reports/ScheduleSearch.htm', '', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 2, 
        'View/Modify Report Schedule ',1, 'HelpAssistance/View-Modify Report Schedule.htm');
commit;	

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Funds-Search', NULL,NULL, sysdate, 
    '/HTML/Fund.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 3, 
    'Funds', 1, 'HELP/Funds.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Functions-Search', NULL,NULL, sysdate, 
    '/HTML/Function.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 4, 
    'Functions', 1, 'HELP/Functions.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'ULB Details', NULL,NULL, sysdate, 
    '/HTML/CompanyDetail.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 5, 
    'ULB Details', 1, 'HELP/ULB Details.htm');

	commit;


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
  

commit;  


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

commit;	
    
    
-------------------------------    



---------- admin

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Administration', TO_DATE('08/04/2008 10:17:40', 'MM/DD/YYYY HH24:MI:SS'), '/egi/eGov.jsp', (select id_module from eg_module where module_name='EGF'), 1, 'Administration', 0, '/HelpAssistance/Administartion.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Boundary', TO_DATE('08/04/2008 10:17:40', 'MM/DD/YYYY HH24:MI:SS'), '/BndryAdmin/boundary.jsp', (select id_module from eg_module where module_name='Boundary Settings'), 1, 'Create Boundary', 1, '/HelpAssistance/CreateBoundary.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Boundary Type', TO_DATE('08/04/2008 10:17:40', 'MM/DD/YYYY HH24:MI:SS'), '/BeforeCreateBndryType.do', (select id_module from eg_module where module_name='Boundary Settings'), 2, 'Create Boundary Type', 1, '/HelpAssistance/CreateBoundaryTYpe.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Serach Boundary Type', TO_DATE('08/04/2008 10:17:40', 'MM/DD/YYYY HH24:MI:SS'), '/BndryAdmin/viewBndryType.jsp', (select id_module from eg_module where module_name='Boundary Settings'), 3, 'Search Boundary Type', 1, '/helpassistance/serachBoundrytype.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Search User', TO_DATE('08/06/2008 06:17:04', 'MM/DD/YYYY HH24:MI:SS'), '/administration/rjbac/user/searchUser.jsp',( select id_module from eg_module where module_name='UserManagement'), 3, 'Search User', 1, '/HelpAssistance/admin/SearchUser.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Create User', TO_DATE('08/04/2008 11:14:59', 'MM/DD/YYYY HH24:MI:SS'), '/SetupUser.do', 'bool=CREATE',( select id_module from eg_module where module_name='UserManagement'), 2, 'Create User', 1, '/HelpAssistance/admin/createUser.htm');
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Create Role', TO_DATE('08/04/2008 11:14:59', 'MM/DD/YYYY HH24:MI:SS'), '/SetupRole.do', 'bool=VIEW',( select id_module from eg_module where module_name='UserManagement'), 1, 'Create Role', 1, '/HelpAssistance/admin/createRole.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Create RoleAction Map', TO_DATE('08/04/2008 11:14:59', 'MM/DD/YYYY HH24:MI:SS'), 'admin/BeforeViewRoleModule.do', '',( select id_module from eg_module where module_name='UserManagement'), 1, 'Create RoleAction Map', 1, '/HelpAssistance/admin/createRole.htm');



Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Create Employee', TO_DATE('08/06/2008 06:17:04', 'MM/DD/YYYY HH24:MI:SS'), '/BeforePIMSMasterAction.do', 'submitType=beforeCreate\&master=EmployeeLight', (select id_module from eg_module where module_name='Employee'), 4, 'Create Employee', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'View Employee', TO_DATE('08/06/2008 06:17:04', 'MM/DD/YYYY HH24:MI:SS'), 'BeforeSearchAction.do', 'module=Employee\&masters=Employee\&mode=View', (select id_module from eg_module where module_name='Employee'), 5, 'View Employee', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Modify Employee', TO_DATE('08/06/2008 06:17:04', 'MM/DD/YYYY HH24:MI:SS'), 'BeforeSearchAction.do', 'module=Employee\&masters=Employee\&mode=Modify', (select id_module from eg_module where module_name='Employee'), 6, 'Modify Employee', 1);

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Department', TO_DATE('08/06/2008 06:55:05', 'MM/DD/YYYY HH24:MI:SS'), 'SetupDepartment.do', 'bool=VIEW', (select id_module from eg_module where module_name='Department'), 1, 'Department', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Create Designation', TO_DATE('08/06/2008 07:10:26', 'MM/DD/YYYY HH24:MI:SS'), 'BeforeDesignationMasterAction.do', 'submitType=beforCreate', (select id_module from eg_module where module_name='Designation'), 1, 'Create Designation', 1);

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Modify Designation', TO_DATE('08/06/2008 07:10:26', 'MM/DD/YYYY HH24:MI:SS'), 'BeforeDesignationMasterAction.do', 'submitType=beforeModify', (select id_module from eg_module where module_name='Designation'), 2, 'Modify Designation', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Position Hierarchy', TO_DATE('08/06/2008 07:18:43', 'MM/DD/YYYY HH24:MI:SS'), 'BeforePositionHeirarchyMasterAction.do', 'submitType=beforCreate', (select id_module from eg_module where module_name='Hierarchy'), 2, 'Position Hierarchy', 1);

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'Create Hierarchy', TO_DATE('08/06/2008 07:18:43', 'MM/DD/YYYY HH24:MI:SS'), '/SetupHierarchyType.do', 'bool=VIEW', (select id_module from eg_module where module_name='Hierarchy'), 1, 'Hierarchy', 1);

commit;



---  for disabling the following folders

UPDATE EG_MODULE SET isenabled=0 WHERE module_name='BRS';
UPDATE EG_MODULE SET isenabled=0 WHERE module_name='Bill Payments';
UPDATE EG_MODULE SET isenabled=0 WHERE module_name='Bills Accounting ';
UPDATE EG_MODULE SET isenabled=0 WHERE module_name='Schemes';
UPDATE EG_MODULE SET isenabled=0 WHERE module_name='Salary Codes';
UPDATE  EG_ACTION SET IS_ENABLED=0 WHERE NAME='Creditors Recoveries';
COMMIT;


UPDATE EG_ACTION SET is_enabled=0 WHERE name='Deposit Register';
UPDATE EG_ACTION SET is_enabled=0 WHERE name='Scheme Utilization';
UPDATE EG_ACTION SET is_enabled=0 WHERE name='Dishonored Cheques Report';
UPDATE EG_ACTION SET is_enabled=0 WHERE name='Summary Statement Report';
COMMIT;


-------- drill down

INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Receipt Accounting-View', NULL,NULL, SYSDATE, 
    '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Miscellaneous Receipt-Create', 0, 'HelpAssistance/Miscellaneous Receipts-Create.htm');

INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Receipt Accounting-Modify', NULL,NULL, SYSDATE, 
    '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Miscellaneous Receipt-Create', 0, 'HelpAssistance/Miscellaneous Receipts-Create.htm');
    
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Receipt Accounting-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/VMC/miscReceipt_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Miscellaneous Receipt-Create', 0, 'HelpAssistance/Miscellaneous Receipts-Create.htm');
    
    
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Journal Voucher-View', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_General_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Journal Proper'), 1, 
    'Journal Voucher-Create', 0, 'HelpAssistance/Journal Voucher.htm');

	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Journal Voucher-Modify', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_General_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Journal Proper'), 1, 
    'Journal Voucher-Create', 0, 'HelpAssistance/Journal Voucher.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Journal Voucher-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_General_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Journal Proper'), 1, 
    'Journal Voucher-Create', 0, 'HelpAssistance/Journal Voucher.htm');
	
	
COMMIT;
	 
-- contra 	 

INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Deposit-View', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 1, 
    'Cash Deposit-Create', 0, 'HelpAssistance/Cash Deposit.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Deposit-modify', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 1, 
    'Cash Deposit-Create', 0, 'HelpAssistance/Cash Deposit.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Deposit-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_CToB_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 1, 
    'Cash Deposit-Create', 0, 'HelpAssistance/Cash Deposit.htm');

	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Withdrawal-View', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Create', 0, 'HelpAssistance/Cash Withdrawal.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Withdrawal-Modify', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Create', 0, 'HelpAssistance/Cash Withdrawal.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Withdrawal-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToC_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Create', 0, 'HelpAssistance/Cash Withdrawal.htm');
	
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cheque Deposit-View', NULL,NULL, SYSDATE, 
    '/HTML/PayInSlip.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 3, 
    'Cheque Deposit-Create', 0, 'HelpAssistance/Pay in.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cheque Deposit-Modify', NULL,NULL, SYSDATE, 
    '/HTML/PayInSlip.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 3, 
    'Cheque Deposit-Create', 0, 'HelpAssistance/Pay in.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cheque Deposit-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/PayInSlip.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 3, 
    'Cheque Deposit-Create', 0, 'HelpAssistance/Pay in.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank to Bank Transfer-View', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Create', 0, 'HelpAssistance/Bank to Bank Transfer.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank to Bank Transfer-Modify', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Create', 0, 'HelpAssistance/Bank to Bank Transfer.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank to Bank Transfer-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/VMC/JV_Contra_BToB_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Create', 0, 'HelpAssistance/Bank to Bank Transfer.htm');
	
	COMMIT;

-- bills accouting

 INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Contractor Bill-View', NULL,NULL, SYSDATE, 
       '/HTML/ContractorJournal.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 1, 
       'Contractor Bill-Create', 0, 'HelpAssistance/Contractor Bill-Create.htm');
 INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Contractor Bill-Modify', NULL,NULL, SYSDATE, 
       '/HTML/ContractorJournal.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 1, 
       'Contractor Bill-Create', 0, 'HelpAssistance/Contractor Bill-Create.htm');	   
 INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Contractor Bill-Reverse', NULL,NULL, SYSDATE, 
       '/HTML/ContractorJournal.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 1, 
       'Contractor Bill-Create', 0, 'HelpAssistance/Contractor Bill-Create.htm');
	   
   INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Supplier Bill-View', NULL,NULL, SYSDATE, 
       '/HTML/SupplierJournal.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 2, 
       'Supplier Bill-Create', 0, 'HelpAssistance/Supplier Bill-Create.htm');
  INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Supplier Bill-Modify', NULL,NULL, SYSDATE, 
       '/HTML/SupplierJournal.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 2, 
       'Supplier Bill-Create', 0, 'HelpAssistance/Supplier Bill-Create.htm');

 INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Supplier Bill-Reverse', NULL,NULL, SYSDATE, 
       '/HTML/SupplierJournal.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 2, 
       'Supplier Bill-Create', 0, 'HelpAssistance/Supplier Bill-Create.htm');
   INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Salary Bill-View', NULL,NULL, SYSDATE, 
       '/HTML/JV_Salary.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 3, 
       'Salary Bill-Create', 0, 'HelpAssistance/Salary Bill-create.htm');
	INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Salary Bill-Modify', NULL,NULL, SYSDATE, 
       '/HTML/JV_Salary.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 3, 
       'Salary Bill-Create', 0, 'HelpAssistance/Salary Bill-create.htm');
INSERT INTO eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    VALUES
      (seq_eg_action.NEXTVAL, 'Salary Bill-Reverse', NULL,NULL, SYSDATE, 
       '/HTML/JV_Salary.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Bills Accounting'), 3, 
       'Salary Bill-Create', 0, 'HelpAssistance/Salary Bill-create.htm');
	   
	   COMMIT;

-- payment

INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank Payment-View', NULL, NULL, SYSDATE, 
    '/HTML/DirectBankPayment.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Bank Payment-Create', 0, 'HelpAssistance/Bank Payment-Create.htm');
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank Payment-Modify', NULL, NULL, SYSDATE, 
    '/HTML/DirectBankPayment.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Bank Payment-Create', 0, 'HelpAssistance/Bank Payment-Create.htm');
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Bank Payment-Reverse', NULL, NULL, SYSDATE, 
    '/HTML/DirectBankPayment.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Bank Payment-Create', 0, 'HelpAssistance/Bank Payment-Create.htm');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Payments-View', NULL, NULL, SYSDATE, 
    '/HTML/VMC/DirectCashPayment_VMC.jsp', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Cash Payments-Create', 0, 'HelpAssistance/');
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Payments-Modify', NULL, NULL, SYSDATE, 
    '/HTML/VMC/DirectCashPayment_VMC.jsp', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Cash Payments-Create', 0, 'HelpAssistance/');
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Cash Payments-Reverse', NULL, NULL, SYSDATE, 
    '/HTML/VMC/DirectCashPayment_VMC.jsp', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Cash Payments-Create', 0, 'HelpAssistance/');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Advance Payments-View','' , NULL, SYSDATE, 
    '/HTML/AdvanceJournal.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Advance Payments-Create', 0, 'HelpAssistance/');
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Advance Payments-Modify','' , NULL, SYSDATE, 
    '/HTML/AdvanceJournal.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Advance Payments-Create', 0, 'HelpAssistance/');
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Advance Payments-Reverse','' , NULL, SYSDATE, 
    '/HTML/AdvanceJournal.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Advance Payments-Create', 0, 'HelpAssistance/');
	
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Pay Supplier/Contractor-View', NULL, NULL, SYSDATE, 
    '/HTML/SubLedgerPayment.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Pay Supplier/Contractor-Create', 0, 'HelpAssistance/Pay Supplier/Contractor-Create');
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Pay Supplier/Contractor-Modify', NULL, NULL, SYSDATE, 
    '/HTML/SubLedgerPayment.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Pay Supplier/Contractor-Create', 0, 'HelpAssistance/Pay Supplier/Contractor-Create');
INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Pay Supplier/Contractor-Reverse', NULL, NULL, SYSDATE, 
    '/HTML/SubLedgerPayment.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Pay Supplier/Contractor-Create', 0, 'HelpAssistance/Pay Supplier/Contractor-Create');
	INSERT INTO eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Salary Payments-View', NULL, NULL, SYSDATE, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=view', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Salary Payments-Create', 0, 'HelpAssistance/');
    INSERT INTO EG_ACTION
(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Salary Payments-Modify', NULL, NULL, SYSDATE, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Salary Payments-Create', 0, 'HelpAssistance/');
INSERT INTO EG_ACTION
(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Salary Payments-Reverse', NULL, NULL, SYSDATE, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Salary Payments-Create', 0, 'HelpAssistance/');
	COMMIT;

INSERT INTO EG_ACTION
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Property Tax Collection-View', NULL,NULL, SYSDATE, 
    '/HTML/PT_Field.htm', 'showMode=view', NULL, (SELECT id_module FROM EG_MODULE WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Property Tax Collection-create', 0, 'HelpAssistance/Property Tax Collection-create.htm');
	
	INSERT INTO EG_ACTION
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Property Tax Collection-Modify', NULL,NULL, SYSDATE, 
    '/HTML/PT_Field.htm', 'showMode=edit', NULL, (SELECT id_module FROM EG_MODULE WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Property Tax Collection-create', 0, 'HelpAssistance/Property Tax Collection-create.htm');
	
	INSERT INTO EG_ACTION
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Property Tax Collection-Reverse', NULL,NULL, SYSDATE, 
    '/HTML/PT_Field.htm', 'showMode=modify', NULL, (SELECT id_module FROM EG_MODULE WHERE 
    module_name='Receipts'), seq_order_number.NEXTVAL, 
    'Property Tax Collection-create', 0, 'HelpAssistance/Property Tax Collection-create.htm');
commit;

INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Procurement Order-View', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/WorksDetailEnq_VMC.jsp', 'showMode=view', (SELECT id_module FROM eg_module WHERE module_name='Procurement Orders'), 1, 'Procurement Order-View', 0, 'HELP/Create Works Bill.htm');
INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Procurement Order-Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/VMC/WorksDetailAdd_VMC.jsp', 'showMode=modify', (SELECT id_module FROM eg_module WHERE module_name='Procurement Orders'), 1, 'Procurement Order-Modify', 0, 'HELP/Create Works Bill.htm');

commit;


INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Function-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FunctionAdd.htm', 'showMode=new', (SELECT id_module FROM eg_module WHERE module_name='Set-up'), 0, 'Function-Create/Modify', 0,
   'HelpAssistance/AP/Function-Create/Modify.htm');
   
   INSERT INTO eg_action
        (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
      VALUES
        (seq_eg_action.NEXTVAL, 'Function-Create/Modify ', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
        '/HTML/FunctionAdd.htm', 'showMode=edit', (SELECT id_module FROM eg_module WHERE module_name='Set-up'), 0, 'Function-Create/Modify', 0,
      'HelpAssistance/AP/Function-Create/Modify.htm');


INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Fund Source-Create/Modify', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FundSourceAdd.htm', 'showMode=new', (SELECT id_module FROM eg_module WHERE module_name='Masters'), 1, 'Fund Source-Create/Modify', 0,
   'HelpAssistance/AP/Fund Source-Create/Modify.htm');

INSERT INTO eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Fund Source-Create/Modify ', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
   '/HTML/FundSourceAdd.htm', 'showMode=edit', (SELECT id_module FROM eg_module WHERE module_name='Masters'), 1, 'Fund Source-Create/Modify', 0,
   'HelpAssistance/AP/Fund Source-Create/Modify.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Supplier/Contractor ', NULL,NULL, sysdate, 
    '/HTML/VMC/Relation_VMC.jsp', '', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 0, 
    'Modify Supplier/Contractor', 0, 'HELP/Modify Supplier/Contractor-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined Codes-New', NULL,NULL, sysdate, 
    '/HTML/SubCodesAdd.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Masters'), 0, 
    'User-defined Codes', 0, 'HELP/User-defined Codes.htm');

	Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined Codes-Modify', NULL,NULL, sysdate, 
    '/HTML/SubCodesAdd.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Masters'), 0, 
    'User-defined Codes', 0, 'HELP/User-defined Codes.htm');

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Collection/Payment point Create/Modify', TO_DATE('05/20/2008 01:19:57', 'MM/DD/YYYY HH24:MI:SS'), '/HTML/BillCollector.htm', '', (select id_module from eg_module where module_name='Masters'), 1, 'Collection/Payment point Create/Modify', 0, 'HELP/Financial Year Create-Modify.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Tax Setup-Create/Modify', NULL,NULL, sysdate, 
    '/HTML/TaxCodeMap.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 0, 
    'Tax Setup-Enquiry', 0, 'HelpAssistance/Tax Setup-Enquiry.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year-Create', NULL,NULL, sysdate, 
    '/HTML/FinancialYearAdd.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Processing'), 0, 
    'Financial Year', 0, 'HELP/Financial Year.htm');
	
	Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year-Modify', NULL,NULL, sysdate, 
    '/HTML/FinancialYearAdd.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Processing'), 0, 
    'Financial Year', 0, 'HELP/Financial Year.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fund-Add', NULL,NULL, sysdate, 
    '/HTML/FundAdd.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Set-up'), 0, 
    'Funds', 0, 'HELP/Funds.htm');
	
	Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fund-Modify', NULL,NULL, sysdate, 
    '/HTML/FundAdd.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Set-up'), 0, 
    'Funds', 0, 'HELP/Funds.htm');
	
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'ULB Details-Modify', NULL,NULL, sysdate, 
    '/HTML/CompanyDetailAdd.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Set-up'), 0, 
    'ULB Details', 0, 'HELP/ULB Details.htm');

INSERT INTO EG_ACTION
(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Salary Payments-Modify', NULL, NULL, SYSDATE, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=edit', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Salary Payments-Create', 0, 'HelpAssistance/');
INSERT INTO EG_ACTION
(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 VALUES
   (seq_eg_action.NEXTVAL, 'Salary Payments-Reverse', NULL, NULL, SYSDATE, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=modify', NULL, (SELECT id_module FROM eg_module WHERE module_name='Payments') , seq_order_number.NEXTVAL, 
    'Salary Payments-Create', 0, 'HelpAssistance/');
	
	INSERT INTO eg_action
	   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
	 VALUES
	   (seq_eg_action.NEXTVAL, 'Fund Source-Create/Modify  ', TO_DATE('05/01/2008 01:37:51', 'MM/DD/YYYY HH24:MI:SS'),
	   '/HTML/FundSourceAdd.htm', '', (SELECT id_module FROM eg_module WHERE module_name='Masters'), 1, 'Fund Source-Create/Modify', 0,
   'HelpAssistance/AP/Fund Source-Create/Modify.htm');
   
     INSERT INTO eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       VALUES
         (seq_eg_action.NEXTVAL, 'Branch-Create/Modify', NULL,NULL, SYSDATE, 
          '/HTML/BankBranchAdd.htm', '', NULL, (SELECT id_module FROM eg_module WHERE module_name='Chart of Accounts'), 0, 
       'Branch-Add', 0, 'HELP/Add Bank.htm');
    
    INSERT INTO eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     VALUES
       (seq_eg_action.NEXTVAL, 'Branch-Create/Modify ', NULL,NULL, SYSDATE, 
        '/HTML/BankBranchAdd.htm', '', NULL, (SELECT id_module FROM eg_module WHERE module_name='Chart of Accounts'), 0, 
    'Branch-Add', 0, 'HELP/Add Bank.htm');
    
     INSERT INTO eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     VALUES
       (seq_eg_action.NEXTVAL, 'Bank Account-Create/Modify', NULL,NULL, SYSDATE, 
        '/HTML/BankBranchEnquiry.htm', '', NULL, (SELECT id_module FROM eg_module WHERE module_name='Chart of Accounts'), 0, 
        'Bank Account-Create/Modify', 0, 'HELP/Add Bank.htm');
	
COMMIT;

insert into eg_roleaction_map select 1,id from eg_action where id >=(select id from eg_action where name='Cancellation of  Vouchers-Search');
commit;



UPDATE eg_action SET url='/HTML/VMC/DirectBankPayment_VMC.jsp' WHERE name='Bank Payment-Create';
UPDATE eg_action SET url='/HTML/VMC/DirectBankPayment_VMC.jsp' WHERE name='Bank Payment-Modify';
UPDATE eg_action SET url='/HTML/VMC/DirectBankPayment_VMC.jsp' WHERE name='Bank Payment-Reverse';
UPDATE eg_action SET url='/HTML/VMC/DirectBankPayment_VMC.jsp' WHERE name='Bank Payment-View';
COMMIT;	


---- help files

UPDATE  eg_action SET action_help_url=REPLACE('/HelpAssistance/AP/'||name||'_AP.htm',' ','')  WHERE id=id; 
COMMIT;

-- Financial reports
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/Receipt-PaymentReport_AP.htm' WHERE name='Receipt/Payment Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/TrialBalance_AP.htm' WHERE name='Trial Balance';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashBook_AP.htm' WHERE name='Cash Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankBook_AP.htm' WHERE name='Bank Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/JournalBook_AP.htm' WHERE name='Journal Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/GeneralLedger_AP.htm' WHERE name='General Ledger';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/Sub-Ledger_AP.htm' WHERE name='Sub-Ledger';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/DayBook_AP.htm' WHERE name='Day Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/SubLedgerSchedule_AP.htm' WHERE name='SubLedger Schedule';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/OpeningBalanceReport_AP.htm' WHERE name='Opening Balance Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/Cheque-in-handReport_AP.htm' WHERE name='Cheque-in-hand Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/ChequesReceived_AP.htm' WHERE name='Cheques Received';


-- mis reports
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankTransaction_AP.htm' WHERE name='Bank Transaction';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/ChequeIssueRegister_AP.htm' WHERE name='Cheque Issue Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/ContractorSupplierReport_AP.htm' WHERE name='Contractor Supplier Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/DepositRegister_AP.htm' WHERE name='Deposit Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/FunctionwiseIESubsidaryRegister_AP.htm' WHERE name='Function-wise IE Subsidary Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/ReceiptRegister_AP.htm' WHERE name='Receipt Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/RegisterofBills_AP.htm' WHERE name='Register of Bills';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/StatementofOutstandingLiability_AP.htm' WHERE name='Statement of Outstanding Liability ';
COMMIT;


UPDATE eg_action SET action_help_url='/HelpAssistance/PropertyTaxCollection.htm' WHERE name='Property Tax Collection-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/PropertyTaxCollection.htm' WHERE name='Property Tax Collection-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/PropertyTaxCollection.htm' WHERE name='Property Tax Collection-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/PropertyTaxCollection.htm' WHERE name='Property Tax Collection-Create';
 
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractorBill.htm' WHERE name='Contractor Bill-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractorBill.htm' WHERE name='Contractor Bill-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractorBill.htm' WHERE name='Contractor Bill-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractorBill.htm' WHERE name='Contractor Bill-Create';
 
UPDATE eg_action SET action_help_url='/HelpAssistance/SupplierBill.htm' WHERE name='Supplier Bill-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/SupplierBill.htm' WHERE name='Supplier Bill-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/SupplierBill.htm' WHERE name='Supplier Bill-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/SupplierBill.htm' WHERE name='Supplier Bill-Create';
 
UPDATE eg_action SET action_help_url='/HelpAssistance/SalaryBill.htm' WHERE name='Salary Bill-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/SalaryBill.htm' WHERE name='Salary Bill-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/SalaryBill.htm' WHERE name='Salary Bill-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/SalaryBill.htm' WHERE name='Salary Bill-Create';
 
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CreateJournalProper_AP.htm' WHERE name='Journal Voucher-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CreateJournalProper_AP.htm' WHERE name='Journal Voucher-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CreateJournalProper_AP.htm' WHERE name='Journal Voucher-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CreateJournalProper_AP.htm' WHERE name='Journal Voucher-Create';

UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankPayment_AP.htm' WHERE name='Bank Payment-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankPayment_AP.htm' WHERE name='Bank Payment-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankPayment_AP.htm' WHERE name='Bank Payment-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankPayment_AP.htm' WHERE name='Bank Payment-Create';

 
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashPayment_AP.htm' WHERE name='Cash Payments-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashPayment_AP.htm' WHERE name='Cash Payments-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashPayment_AP.htm' WHERE name='Cash Payments-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashPayment_AP.htm' WHERE name='Cash Payments-Create';
 
 -- no help files for supllier/contractor payment and salary payment
 
 
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashDeposit_AP.htm' WHERE name='Cash Deposit-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashDeposit_AP.htm' WHERE name='Cash Deposit-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashDeposit_AP.htm' WHERE name='Cash Deposit-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashDeposit_AP.htm' WHERE name='Cash Deposit-Create';

UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashWithdrawal_AP.htm' WHERE name='Cash Withdrawal-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashWithdrawal_AP.htm' WHERE name='Cash Withdrawal-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashWithdrawal_AP.htm' WHERE name='Cash Withdrawal-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/CashWithdrawal_AP.htm' WHERE name='Cash Withdrawal-Create';

UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankToBankTransfer_AP.htm' WHERE name='Bank to Bank Transferl-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankToBankTransfer_AP.htm' WHERE name='Bank to Bank Transfer-Reverse';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankToBankTransfer_AP.htm' WHERE name='Bank to Bank Transfer-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/BankToBankTransfer_AP.htm' WHERE name='Bank to Bank Transfer-Create';
 
-- no help files for cheque deposit

-- masters

UPDATE eg_action SET action_help_url='/HelpAssistance/CodeScreenMapping.htm' WHERE name='Code-Screen Mappings';
UPDATE eg_action SET action_help_url='/HelpAssistance/Userdefinedsubcodesenquiry.htm' WHERE name='User-defined Codes';
UPDATE eg_action SET action_help_url='/HelpAssistance/Userdefinedsubcodes.htm' WHERE name='User-defined Codes-New';
UPDATE eg_action SET action_help_url='/HelpAssistance/Userdefinedsubcodesenquiry.htm' WHERE name='User-defined Codes-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/SourceofFinancing.html' WHERE name='Source of Financing';

UPDATE eg_action SET action_help_url='/HelpAssistance/Collection_Paymentpoint.htm' WHERE name='Collection/Payment point';
UPDATE eg_action SET action_help_url='/HelpAssistance/Collection_PaymentEnquiry.htm' WHERE name='Collection/Payment point Create/Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/AccountingEntity.htm' WHERE name='Accounting Entity';
UPDATE eg_action SET action_help_url='/HelpAssistance/SetupChequeinHand_CashinHand.htm' WHERE name='Setup Cheque in Hand/Cash in Hand';
UPDATE eg_action SET action_help_url='/HelpAssistance/TaxsetUp_Add.htm' WHERE name='Tax Setup-Enquiry';
UPDATE eg_action SET action_help_url='/HelpAssistance/TaxsetUp-Modify.htm' WHERE name='Tax Setup-Create/Modify';

UPDATE eg_action SET action_help_url='/HelpAssistance/Supplier-Contractor-create.htm' WHERE name='Supplier-Contractor-create';
UPDATE eg_action SET action_help_url='/HelpAssistance/Supplier-Contractor-create.htm' WHERE name='View Supplier/Contractor';
UPDATE eg_action SET action_help_url='/HelpAssistance/Supplier-Contractor-create.htm' WHERE name='Modify Supplier/Contractor ';
UPDATE eg_action SET action_help_url='/HelpAssistance/ProcurementOrder_create.htm' WHERE name='Procurement Order-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/ProcurementOrder_create.htm' WHERE name='Modify Procurement Orders-Search';

UPDATE eg_action SET action_help_url='/HelpAssistance/ProcurementOrder_create.htm' WHERE name='View Procurement Orders-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ChartofAccounts.htm' WHERE name='Chart Of Accounts';
UPDATE eg_action SET action_help_url='/HelpAssistance/BankBranchadd.htm' WHERE name='Add Bank';
UPDATE eg_action SET action_help_url='/HelpAssistance/BankEnquiry.htm' WHERE name='Add/Modify Branch-Modify Bank';
UPDATE eg_action SET action_help_url='/HelpAssistance/DetailCode-CreateModifyView.htm' WHERE name='Detailed Code-Create/Modify/View';
COMMIT;

UPDATE eg_action SET action_help_url='/HelpAssistance/FinancialYear_Setup.htm' WHERE name='Financial Year-SetUp';
UPDATE eg_action SET action_help_url='/HelpAssistance/FinancialYear_Modify.htm' WHERE name='Financial Year-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/FinancialYear_Setup.htm' WHERE name='Financial Year-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/OpeningBalance_Setup.htm' WHERE name='Opening Balance-SetUp';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReportScheduleMapping.htm' WHERE name='Report Schedule Mapping-Create';

UPDATE eg_action SET action_help_url='/HelpAssistance/ReportScheduleMapping_Modifyy.htm' WHERE name='Report Schedule Mapping-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/Fund_Add.htm' WHERE name='Fund-Add';
UPDATE eg_action SET action_help_url='/HelpAssistance/Fund_Modify.htm' WHERE name='Fund-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/FundSource_CreateModify.htm' WHERE name='Fund Source-Create/Modify';

UPDATE eg_action SET action_help_url='/HelpAssistance/FundSource_CreateModify.htm' WHERE name='Fund Source-Create/Modify ';
UPDATE eg_action SET action_help_url='/HelpAssistance/Function_Add.htm' WHERE name='Functions-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/Function_CreateModify.htm' WHERE name='Function-Create/Modify ';
UPDATE eg_action SET action_help_url='/HelpAssistance/Function_CreateModify.htm' WHERE name='Function-Create/Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/ULBDetail_Modify.htm' WHERE name='ULB Details-Modify';

UPDATE eg_action SET action_help_url='/HelpAssistance/ULBDetail_view.htm' WHERE name='ULB Details';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeModify.htm' WHERE name='Modify Party Type- Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeModify.htm' WHERE name='Modify Party Type-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeModify.htm' WHERE name='Modify Party Types -Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeView.htm' WHERE name='View Party Types- Search';

UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeView.htm' WHERE name='View Party Types-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeModify.htm' WHERE name='Modify Contract Types -Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeModify.htm' WHERE name='Modify Contract Types- Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeModify.htm' WHERE name='Modify Contract Types-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeview.htm' WHERE name='View Contract Types- Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeview.htm' WHERE name='View Contract Types-Search';

UPDATE eg_action SET action_help_url='/HelpAssistance/Recoveryview.htm' WHERE name='View Recoveries-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryModify.htm' WHERE name='Modify Recoveries- Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryModify.htm' WHERE name='Modify Recoveries-Search';

UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryModify.htm' WHERE name='Modify Remittance Recovery';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryModify.htm' WHERE name='Modify Remittance Recovery ';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryModify.htm' WHERE name='Modify Remittance Recovery-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryView.htm' WHERE name='View Remittance Recovery';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryView.htm' WHERE name='View Remittance Recovery-Search';

UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryCreate.htm' WHERE name='Remittance Recovery-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryCreate.htm' WHERE name='Recovery- Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryCreate.htm' WHERE name='Recovery-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeCreate.htm' WHERE name='Contract Type- Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeCreate.htm' WHERE name='Contract Type-Create';

UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeCreate.htm' WHERE name='Party Type- Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeCreate.htm' WHERE name='Party Type-Create';


COMMIT;


UPDATE eg_action SET action_help_url='/HelpAssistance/ConfirmVoucher_help.htm' WHERE name='Confirm Vouchers';
UPDATE eg_action SET action_help_url='/HelpAssistance/SourceofFinancing.htm' WHERE name='Source of Financing';
UPDATE eg_action SET action_help_url='/HelpAssistance/SetupChequeinHand_CashinHand.htm' WHERE name='Setup Cheque in Hand/Cash in Hand';
UPDATE eg_action SET action_help_url='/HelpAssistance/FinancialYear_Setup.htm' WHERE name='Financial Year-SetUp';
UPDATE eg_action SET action_help_url='/HelpAssistance/FundSource_CreateModify.htm' WHERE name='Funds';
UPDATE eg_action SET action_help_url='/HelpAssistance/SupplierBill.htm' WHERE name='Supplier Bill-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/IncomeExpReport.htm' WHERE name='Income - Expenditures Statement';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReceiptsPaymentReport.htm' WHERE name='Receipt/Payment Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/BalancesheetReport.htm' WHERE name='Balance Sheet';
UPDATE eg_action SET action_help_url='/HelpAssistance/DetailCode-CreateModifyView.htm' WHERE name='Detailed Code-Create/Modify/View';
UPDATE eg_action SET action_help_url='/HelpAssistance/TrialBalanceReport.htm' WHERE name='Trial Balance';
UPDATE eg_action SET action_help_url='/HelpAssistance/CashbookReport.htm' WHERE name='Cash Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/BankbookReport.htm' WHERE name='Bank Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/JournalbookReport.htm' WHERE name='Journal Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/GeneralLedgerReport.htm' WHERE name='General Ledger';
UPDATE eg_action SET action_help_url='/HelpAssistance/SubledgerReport.htm' WHERE name='Sub-Ledger';
UPDATE eg_action SET action_help_url='/HelpAssistance/DaybookReport.htm' WHERE name='Day Book';
UPDATE eg_action SET action_help_url='/HelpAssistance/SubledgerscheduleReport.htm' WHERE name='SubLedger Schedule';
UPDATE eg_action SET action_help_url='/HelpAssistance/OpeningBalanceReport.htm' WHERE name='Opening Balance Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/ChequeinHandReport.htm' WHERE name='Cheque-in-hand Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractorSupplierReport.htm' WHERE name='Contractor Supplier Report';
UPDATE eg_action SET action_help_url='/HelpAssistance/ChequeIssueRegisterReport.htm' WHERE name='Cheque Issue Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/BillRegisterReport.htm' WHERE name='Register of Bills';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReceiptRegisterReport.htm' WHERE name='Receipt Register';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewProcurementOrder-Search.htm' WHERE name='View Procurement Orders-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ModifyProcurementOrder-Search.htm' WHERE name='Modify Procurement Orders-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewSupplierContractor_Search.htm' WHERE name='View Supplier/Contractor-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ModifySupplierContractor_Search.htm' WHERE name='Modify Supplier/Contractor-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryCreate.htm' WHERE name='Remittance Recovery-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryView.htm' WHERE name='View Remittance Recovery-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RemittancerecoveryModify.htm' WHERE name='Modify Remittance Recovery-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeCreate.htm' WHERE name='Party Type-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeView.htm' WHERE name='View Party Types-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PartyTypeModify.htm' WHERE name='Modify Party Type-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeCreate.htm' WHERE name='Contract Type-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeview.htm' WHERE name='View Contract Types-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ContractTypeModify.htm' WHERE name='Modify Contract Types-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryCreate.htm' WHERE name='Recovery-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/Recoveryview.htm' WHERE name='View Recoveries-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/RecoveryModify.htm' WHERE name='Modify Recoveries-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewProcurementOrder-Search.htm' WHERE name='Procurement Order-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/FundSource_CreateModify.htm' WHERE name='Fund-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/Fund_Modify.htm' WHERE name='Fund-Modify';
UPDATE eg_action SET action_help_url='/HelpAssistance/BankBranchadd.htm' WHERE name='Bank Branch-View';
UPDATE eg_action SET action_help_url='/HelpAssistance/Billview.htm' WHERE name='View  Bills-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/BillModify.htm' WHERE name='Modify  Bills-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/BillReverse.htm' WHERE name='Reverse Bills-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/PaySupplierContractorsearch.htm' WHERE name='Pay Supplier/Contractor-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/SalaryPaymentCreate.htm' WHERE name='Salary Payments-Create';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewPaymentsSearch.htm' WHERE name='View Payments-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ModifyPaymentsSearch.htm' WHERE name='Modify Payment-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReversepaymentsSearch.htm' WHERE name='Reverse Payments-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewJournalvoucherSearch.htm' WHERE name='View Journal Vouchers-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ModifyJournalVoucherSearch.htm' WHERE name='Modify Journal Vouchers-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReverseJournalVoucherSearch.htm' WHERE name='Reverse Journal Vouchers-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ViewContraentriesSearch.htm' WHERE name='View Contra Entries-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ModifyContraentriesSearch.htm' WHERE name='Modify Contra Entries-Search';
UPDATE eg_action SET action_help_url='/HelpAssistance/ReverseContraentriesSearch.htm' WHERE name='Reverse Contra Entries-Search';


UPDATE eg_action SET url='/eGov.jsp', module_id=(SELECT id_module FROM eg_module WHERE module_name='EGF') WHERE name='LOGIN';
commit;


#DOWN

