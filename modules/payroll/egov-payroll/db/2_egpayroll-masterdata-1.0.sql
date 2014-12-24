#UP

INSERT INTO EG_OBJECT_TYPE ( ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE ) VALUES ( 
seq_object_type.nextVal, 'Exception', 'Exception',  SYSDATE); 
INSERT INTO EG_OBJECT_TYPE ( ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE ) VALUES ( 
seq_object_type.nextVal, 'payhead', 'payhead',  SYSDATE); 
INSERT INTO EG_OBJECT_TYPE ( ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE ) VALUES ( 
seq_object_type.nextVal, 'payscale', 'payscale',  SYSDATE); 
INSERT INTO EG_OBJECT_TYPE ( ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE ) VALUES ( 
seq_object_type.nextVal, 'advance', 'advance',  SYSDATE);  
INSERT INTO EG_OBJECT_TYPE ( ID, TYPE, DESCRIPTION, LASTMODIFIEDDATE ) VALUES ( 
seq_object_type.nextVal, 'payslip', 'payslip',  SYSDATE); 
--COMMIT;
 

 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
3, 'Deduction-Tax', 'Deduction Tax', 'D', 1,  SYSDATE); 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
1, 'Earning-Pay', 'Earning Pay', 'E', 1,  SYSDATE); 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
2, 'Earning-Allowance', 'Earning-Allowance', 'E', 2,  SYSDATE); 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
4, 'Deduction-Advance', 'Deduction-Advance', 'D', 2,  SYSDATE); 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
5, 'Deduction-Other', 'Deduction-Other', 'D', 1,  SYSDATE); 
INSERT INTO EGPAY_CATEGORY_MASTER ( ID, NAME, DESCRIPTION, CAT_TYPE, CREATEDBY, CREATEDTIMESTAMP
 ) VALUES ( 
6, 'Deduction-BankLoan', 'Deduction-BankLoan', 'D', 1,  SYSDATE); 
--COMMIT;
 
INSERT INTO EGPAY_EMPPAYROLLTYPES ( ID, PAYTYPE, NARRATION, CREATEDDATE, CREATEDBY
 ) VALUES ( 
1, 'Normal PaySlip', 'Normal Payslip',  SYSDATE
, 1); 
INSERT INTO EGPAY_EMPPAYROLLTYPES ( ID, PAYTYPE, NARRATION, CREATEDDATE, CREATEDBY
 ) VALUES ( 
2, 'Exception PaySlip', 'Exception Payslip',  SYSDATE
, 1); 
INSERT INTO EGPAY_EMPPAYROLLTYPES ( ID, PAYTYPE, NARRATION, CREATEDDATE, CREATEDBY
 ) VALUES ( 
3, 'Final Settlement  PaySlip', 'Supplimentary Payslip',  SYSDATE
, 1); 
INSERT INTO EGPAY_EMPPAYROLLTYPES ( ID, PAYTYPE, NARRATION, CREATEDDATE, CREATEDBY
 ) VALUES ( 
4, 'Arrear PaySlip', 'Arrear Payslip',  SYSDATE
, 1); 
INSERT INTO EGPAY_EMPPAYROLLTYPES ( ID, PAYTYPE, NARRATION, CREATEDDATE, CREATEDBY
 ) VALUES ( 
5, 'Leave Encashment PaySlip', 'Leave Encashment Payslip',  SYSDATE
, 1); 
--COMMIT;


/************Required for Payroll (FOR EIS UAT UPDATE) ****************/

INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'PaySlip', 'Created',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'PaySlip', 'DeptApproved', SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'PaySlip', 'AccountsApproved',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'PaySlip', 'AuditApproved',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'PaySlip', 'Cancelled',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'Salaryadvance', 'Created',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'Salaryadvance', 'Sanctioned',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'Salaryadvance', 'Disbursed',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'EmpException', 'Created',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'EmpException', 'Approved',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'EmpException', 'Closed',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'Salaryadvance', 'Rejected',  SYSDATE); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION,
LASTMODIFIEDDATE ) VALUES ( 
SEQ_EGW_STATUS.nextval, 'Salaryadvance', 'Closed',  SYSDATE); 
--COMMIT;

INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Suspend Pay', 'Deputation'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Suspend Pay', 'Resignation'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Suspend Pay', 'Termination of services'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Supplementary Pay', 'Leave'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Supplementary Pay', 'Others'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Suspend Pay', 'Others'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'Designation Change', 'Promotion or Demotion'); 
INSERT INTO EGPAY_EXCEPTION_MSTR ( ID, TYPE, REASON ) VALUES ( 
EISPAYROLL_EXCEPTIONMSTR_SEQ.nextval, 'PayScaleHeader change', 'Change in PayScaleHeader'); 
--COMMIT;


/*********** START PAYROLL Actions *****************/

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payroll',  SYSDATE, 1, NULL, '/staff/index.jsp', (select ID_MODULE from eg_module where module_name like 'EIS-Payroll'), 'Payroll', 1); 


INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'payrollmainpage', NULL, NULL,  TO_Date( '03/27/2008 12:14:47 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/eGov_payroll.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 1, NULL, 0, ''); 



INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payroll reports',  TO_Date( '09/01/2008 11:13:11 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 'Reports', NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Reports', NULL); 

INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PaySummaryReport', NULL, NULL,  TO_Date( '09/01/2008 11:13:11 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/reports/paysummary.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll reports'), 1, 'Pay Summary', 1, NULL); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payhead',  TO_Date( '04/24/2008 07:47:40 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1, 'Payhead'
, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Payhead', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ModifyPayhead', NULL, NULL,  TO_Date( '04/24/2008 07:44:03 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payhead/searchPayhead.do', 'mode=modify', NULL, (select ID_MODULE from eg_module where module_name like 'Payhead'), 3, 'Modify', 1, '/payroll/onlineHelp/ModifyPayhead_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'Viewpayhead', NULL, NULL,  TO_Date( '04/24/2008 07:47:40 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payhead/searchPayhead.do', 'mode=view', NULL, (select ID_MODULE from eg_module where module_name like 'Payhead'), 2, 'View', 1, '/payroll/onlineHelp/Viewpayhead_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'CreatePayhead', NULL, NULL,  TO_Date( '04/24/2008 07:44:03 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payhead/beforePayhead.do', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payhead'), 1, 'Create', 1, '/payroll/onlineHelp/CreatePayhead_AP.htm'); 



INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payscale',  TO_Date( '03/26/2008 09:57:37 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1, 'Payscale'
, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Payscale', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'Payscaleview', NULL, NULL,  TO_Date( '03/26/2008 09:57:53 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/payScaleSearch.jsp', 'mode=view', NULL, (select ID_MODULE from eg_module where module_name like 'Payscale'), 2, 'View', 1, '/payroll/onlineHelp/Payscaleview_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PayscaleModify', NULL, NULL,  TO_Date( '03/26/2008 09:57:53 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/payScaleSearch.jsp', 'mode=modify', NULL, (select ID_MODULE from eg_module where module_name like 'Payscale'), 3, 'Modify', 1, '/payroll/onlineHelp/PayscaleModify_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PayscaleCreat', NULL, NULL,  TO_Date( '03/26/2008 09:57:53 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/CreatePayScale.jsp', 'mode=create', NULL, (select ID_MODULE from eg_module where module_name like 'Payscale'), 1, 'Create', 1, '/payroll/onlineHelp/PayscaleCreat_AP.htm'); 



INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Advance',  TO_Date( '05/02/2008 08:13:16 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Advance', 2); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ViewAdvance', NULL, NULL,  TO_Date( '05/02/2008 08:13:16 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/advance/searchAdvanceModify.jsp', 'mode=view', NULL, (select ID_MODULE from eg_module where module_name like 'Advance'), 2, 'View', 1, '/payroll/onlineHelp/ViewAdvance_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'SanctionAdvance', NULL, NULL,  TO_Date( '05/02/2008 09:33:39 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/advance/searchAdvances.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Advance'), 4, 'Sanction', 1, '/payroll/onlineHelp/SanctionAdvance_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ModifyAdvance', NULL, NULL,  TO_Date( '05/02/2008 09:33:39 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/advance/searchAdvanceModify.jsp', 'mode=modify', NULL, (select ID_MODULE from eg_module where module_name like 'Advance'), 3, 'Modify', 1, '/payroll/onlineHelp/ModifyAdvance_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'CreateAdvance', NULL, NULL,  TO_Date( '05/02/2008 09:33:39 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/beforeSalaryadvance.do', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Advance'), 1, 'Create', 1, '/payroll/onlineHelp/CreateAdvance_AP.htm'); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Adv Disbursement',  TO_Date( '03/26/2008 08:06:28 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 'Advance Disbursement', NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Disburse advance', NULL); 
INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'By Cheque',  TO_Date( '03/26/2008 08:11:41 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1
, 'advance disbursement by cheque', NULL, (select ID_MODULE from eg_module where module_name like 'Adv Disbursement'), 'By Cheque', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'View', NULL, NULL,  TO_Date( '03/26/2008 08:14:38 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/disbursementByCheque.do', 'submitType=beforeChequeDisbursementList\&mode=view'
, NULL, (select ID_MODULE from eg_module where module_name like 'By Cheque'), 2, 'View', 1, '/payroll/onlineHelp/View_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'Create', NULL, NULL,  TO_Date( '03/26/2008 08:14:38 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/disbursementByCheque.do', 'submitType=beforeCreateDisbursement'
, NULL, (select ID_MODULE from eg_module where module_name like 'By Cheque'), 1, 'Create', 1, '/payroll/onlineHelp/Create_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'Modify', NULL, NULL,  TO_Date( '03/26/2008 08:14:38 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/disbursementByCheque.do', 'submitType=beforeChequeDisbursementList\&mode=modify'
, NULL, (select ID_MODULE from eg_module where module_name like 'By Cheque'), 3, 'Modify', 1, '/payroll/onlineHelp/Modify_AP.htm'); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'By Cash/Direct',  TO_Date( '04/23/2008 06:06:40 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 'By Cash/Direct', NULL, (select ID_MODULE from eg_module where module_name like 'Adv Disbursement'), 'By Cash/Direct', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ModifyAdvDisbrscash', NULL, NULL,  TO_Date( '04/23/2008 06:06:40 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/advanceDisbursement.do', 'submitType=beforeDisbursementList\&mode=modify'
, NULL, (select ID_MODULE from eg_module where module_name like 'By Cash/Direct'), 3, 'Modify', 1, '/payroll/onlineHelp/ModifyAdvDisbrscash_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ViewAdvDisbursmentCash', NULL, NULL,  TO_Date( '03/26/2008 09:42:55 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/advanceDisbursement.do', 'submitType=beforeDisbursementList\&mode=view'
, NULL, (select ID_MODULE from eg_module where module_name like 'By Cash/Direct'), 2, 'View', 1, '/payroll/onlineHelp/ViewAdvDisbursmentCash_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'createAdvDisbursmentCash', NULL, NULL,  TO_Date( '03/26/2008 09:42:55 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/salaryadvance/advanceDisbursement.do', 'submitType=beforeCreateDisbursement', NULL
, (select ID_MODULE from eg_module where module_name like 'By Cash/Direct'), 1, 'Create', 1, '/payroll/onlineHelp/createAdvDisbursmentCash_AP.htm'); 




INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Provident Fund',  TO_Date( '07/04/2008 05:45:53 PM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 'Provident fund', NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Provident Fund', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PF Report', NULL, NULL,  TO_Date( '07/04/2008 05:55:03 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/reports/PFReport.jsp', NULL, 2, (select ID_MODULE from eg_module where module_name like 'Provident Fund'), 2, 'PF Report', 1, '/payroll/onlineHelp/PF Report_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PF Setup', NULL, NULL,  TO_Date( '07/04/2008 05:55:03 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/providentfund/pfsetup.do', 'submitType=loadData', 1, (select ID_MODULE from eg_module where module_name like 'Provident Fund'), 1, 'PF Setup', 1, '/payroll/onlineHelp/PF Setup_AP.htm'); 


INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payroll Exception',  TO_Date( '03/26/2008 10:07:24 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, 'payroll exception', NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Payslip Exceptions', NULL); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ExceptionCreate', NULL, NULL,  TO_Date( '03/26/2008 10:07:24 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/exception/beforeException.do', 'submitType=getExceptionInfo', NULL, (select ID_MODULE from eg_module where module_name like 'Payroll Exception'), 1, 'Create'
, 1, '/payroll/onlineHelp/ExceptionCreate_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ExceptionView', NULL, NULL,  TO_Date( '03/26/2008 10:07:24 AM', 'MM/DD/YYYY HH:MI:SS AM')
, '/exception/searchException.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll Exception'), 2, 'View/Approve/Reject', 1, '/payroll/onlineHelp/ExceptionView_AP.htm'); 



INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'Payslip',  TO_Date( '06/23/2008 07:03:33 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1, NULL
, NULL, (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Payslip', 1); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ViewPayslipPdf', NULL, NULL,  TO_Date( '05/23/2008 08:30:19 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/generatePDF.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 7, 'View Payslips PDF format', 1, '/payroll/onlineHelp/ViewPayslipPdf_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ApprovePayslipWithoutJBPM', NULL, NULL,  TO_Date( '05/26/2008 05:40:25 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslipapprove/payslipApproveCriteria.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 8, 'Approve Not Jbpm'
, 0, '/payroll/onlineHelp/ApprovePayslipWithoutJBPM_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ManualPayCreate', NULL, NULL,  TO_Date( '05/23/2008 08:30:19 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/ManualGenPaySlipsCentralAction.do', 'mode=central', NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 1, 'Create for new employee'
, 0, '/payroll/onlineHelp/ManualPayCreate_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ViewPayslip', NULL, NULL,  TO_Date( '06/23/2008 07:03:33 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/BeforeviewGenPaySlips.do', 'mode=viewcentral', NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 2, 'View payslips'
, 1, '/payroll/onlineHelp/ViewPayslip_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'GenerateBatchPayslips', NULL, NULL,  TO_Date( '05/23/2008 08:30:19 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/genBatchPaySlips.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 4, 'Generate batch payslips', 1
, '/payroll/onlineHelp/GenerateBatchPayslips_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'SupplementaryPayslips', NULL, NULL,  TO_Date( '05/23/2008 08:30:19 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/payslip/generateSuppPaySlip.do', 'submitType=beforeSuppPayslip', NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 6, 'Supplementary payslips'
, 1, '/payroll/onlineHelp/SupplementaryPayslips_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'FailurePayslips', NULL, NULL,  TO_Date( '06/02/2008 10:39:03 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/reports/paySlipsReport.jsp', 'mode=resolve', NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 5, 'Unprocessed payslips'
, 1, '/payroll/onlineHelp/FailurePayslips_AP.htm'); 
INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'ApprovePayslip', NULL, NULL,  TO_Date( '05/23/2008 08:30:19 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/inbox/payslipInbox.jsp', NULL, NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 3, 'Approve', 1, '/payroll/onlineHelp/ApprovePayslip_AP.htm'); 

/*********** END PAYROLL Actions *****************/

/*********** Creating Access Permissions for super user *****************/
INSERT INTO EG_ROLEACTION_MAP(ROLEID,ACTIONID)
SELECT ID_ROLE , ID FROM EG_ACTION , EG_ROLES WHERE ROLE_NAME like 'SUPER USER' and id >= (select ea.id from eg_action ea where ea.name like 'payrollmainpage');


update eg_action set action_help_url=null where action_help_url like '/payroll/onlineHelp/%';

/******************** Update Payroll Help URLS *********************/

UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_payslip_view.html' WHERE name='ViewPayslip';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_batchpayslips.html' WHERE name='GenerateBatchPayslips';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_suppayslip.html' WHERE name='SupplementaryPayslips';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_unprocessedpayslip.html' WHERE name='FailurePayslips';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_sanction.html' WHERE name='SanctionAdvance';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_modify.html' WHERE name='ModifyAdvance';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_create.html' WHERE name='CreateAdvance';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbchq_view.html' WHERE name='View';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbchq_create.html' WHERE name='Create';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbchq_modify.html' WHERE name='Modify';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbcash_modify.html' WHERE name='ModifyAdvDisbrscash';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbcash_view.html' WHERE name='ViewAdvDisbursmentCash';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_disbcash_create.html' WHERE name='createAdvDisbursmentCash';

UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_payscexcptn_create.html' WHERE name='ExceptionCreate';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_payscexcptn_view.html' WHERE name='ExceptionView';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_reports_PDFpayslips.html' WHERE name='ViewPayslipPdf';


UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_advance_view.html' WHERE name='ViewAdvance';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_reports_paysumm.html' WHERE name='PaySummaryReport';

#DOWN