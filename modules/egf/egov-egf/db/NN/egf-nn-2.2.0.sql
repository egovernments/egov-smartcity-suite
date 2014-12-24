update  eg_action set URL='/BndryAdmin/boundarySearch.jsp' where  display_name='Create Boundary' and name='Boundary';

EXEC RECREATE_SEQUENCE('SEQ_EGW_STATUS','EGW_STATUS','ID');

INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Estimate Recorded ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Financial Details Recorded ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Financial Approval Done',  TO_Date( '02/16/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Technical Approval Done ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Admin Approval Done ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Agreement Issued ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Billed ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Completion Certificate Issued ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WO', 'Cancelled ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'MB', 'Created ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'MB', 'Checked ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'MB', 'Billed ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'MB', 'Cancelled ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Created ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Technical Approved ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Financial Approved ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Admin Approved ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Payment made ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Confirmed ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Cancelled ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CB', 'Reversed ',  TO_Date( '02/14/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'CREATED',  TO_Date( '06/01/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'VERIFIED',  TO_Date( '06/01/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'ACCOUNTS APPROVAL',  TO_Date( '06/01/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'Voucher Created',  TO_Date( '06/01/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'PAYMENT APPROVED',  TO_Date( '06/27/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'PAYMENT CONFIRMED',  TO_Date( '06/27/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'SALBILL', 'Created',  TO_Date( '07/10/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'SALBILL', 'Approved',  TO_Date( '07/10/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'SALBILL', 'Passed',  TO_Date( '07/10/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'SALBILL', 'Paid',  TO_Date( '07/10/2007 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'SALBILL', 'Cancelled',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WORKSBILL', 'Pending',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WORKSBILL', 'Passed',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WORKSBILL', 'Cancelled',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'WORKSBILL', 'Paid',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'PURCHBILL', 'Pending',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'PURCHBILL', 'Passed',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'PURCHBILL', 'Cancelled',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'PURCHBILL', 'Paid',  TO_Date( '02/01/2008 07:28:02 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 
INSERT INTO EGW_STATUS ( ID, MODULETYPE, DESCRIPTION, LASTMODIFIEDDATE, CODE,
ORDER_ID ) VALUES ( 
seq_egw_status.nextval, 'CBILL', 'Cancelled',  TO_Date( '03/17/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL); 

commit;


INSERT INTO eg_action (ID, NAME, entityid, taskid, updatedtime,url, queryparams, urlorderid, module_id, order_number,display_name, is_enabled, action_help_url)
     VALUES (seq_eg_action.NEXTVAL, 'BS Schedules', NULL, NULL, SYSDATE,'/Reports/BSSchedules.jsp', '', NULL, NULL, NULL,'BS Schedules', 1, NULL);

INSERT INTO eg_action (ID, NAME, entityid, taskid, updatedtime,url, queryparams, urlorderid, module_id, order_number,display_name, is_enabled, action_help_url)
     VALUES (seq_eg_action.NEXTVAL, 'RP Schedules', NULL, NULL, SYSDATE,'/Reports/RPSchedules.jsp', '', NULL, NULL, NULL,'RP Schedules', 1, NULL);     
     
EXEC RECREATE_SEQUENCE('SEQ_SCHEDULEMAPPING','SCHEDULEMAPPING','ID');
-- RP schedules create and map glcodes
-- schedulemapping for ReceiptPayment
update chartofaccounts set receiptoperation=null, receiptscheduleid=null, paymentoperation=null, paymentscheduleid=null;
delete from schedulemapping where reporttype='RP';
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-01', 'Opening Cash and Bank Balances', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-02', 'Tax Revenue', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-03', 'Assigned Revenues  Compensations', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-04', 'Rental Income from Municipal Properties', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-05', 'Fees User Charges and Other Charges', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-06', 'Sale   Hire Charges', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-07', 'Grants and Contributions', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-08', 'Interest/ Dividend Earned', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-09', 'Other Income', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-10', 'Prior Period Item', 1, sysdate, 'ROP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-11', 'Grants and Contributions for Specific Purposes', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-12', 'Secured Loans Received', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-13', 'Unsecured Loans Received', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-14', 'Deposits Received', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-15', 'Other Liabilities', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-16', 'Sale / Disposal of Fixed Assets', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-17', 'Investments', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-18', 'Recovery of Loans, Advances and Deposits', 1, sysdate, 'RNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-19', 'Remission   Refund', 1, sysdate, 'POP', 1);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-20', 'Human Resource Expenses', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-21', 'General Expenses', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-22', 'Operations   Maintenance', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-23', 'Interest   Finance Charges', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-24', 'Programme Expenses, Grants etc.', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-25', 'Prior Period Item', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-26', 'Purchase of Stores', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-27', 'Expenditure out of Earmarked Funds', 1, sysdate, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-28', 'Repayment of Secured Loans', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-29', 'Repayment of Unsecured Loans', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-30', 'Refund of Deposits Received', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-31', 'Repayment of Other Liabilities', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-32', 'Payment for acquisition of Fixed Assets', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-33', 'Payment for Capital Work in progress (CWIP)', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-34', 'Investments', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-35', 'Loans, Advances and Deposits', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-36', 'Miscellaneous Expenditure', 1, sysdate, 'PNOP');
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, REPSUBTYPE)
 Values
   (seq_schedulemapping.nextval, 'RP', 'R-37', 'Closing Cash & Bank Balances', 1, sysdate, 'PNOP');


-- update chartofaccounts
update chartofaccounts set receiptoperation=null,receiptscheduleid=null,paymentoperation=null,paymentscheduleid=null;

update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-01') where glcode in('471','472','473','474','478','479');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-02') where glcode in('111','112','118');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-03') where glcode in('121');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-04') where glcode in('131','132','133','134','138');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-05') where glcode in('141','142','143','144','145','146','147','148');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-06') where glcode in('151','152','153','154','155','158');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-07') where glcode in('161','162','163');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-08') where glcode in('171','172','173','174','175','178');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-09') where glcode in('182','183','185','188');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-10') where glcode in('281','282');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-11') where glcode in('341','342','343','344','345','346','348');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-12') where glcode in('351','352','353','354','355','357','358');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-13') where glcode in('361','362','363','364','365','367','368');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-14') where glcode in('371','372','373','374','378');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-15') where glcode in('384','385','386','388');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-16') where glcode in('411','412','413','414','415','416','417','418','419');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-17') where glcode in('441','442','443','445','448');
update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-18') where glcode in('481','482','485','486','488');


update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-19') where glcode in('119','139','149');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-20') where glcode in('211','212','213','214','215');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-21') where glcode in('221','222','223','224','225','226','227','228');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-22') where glcode in('231','232','233','234','235','236','237','238','239');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-23') where glcode in('241','242','243','244','245','246','248');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-24') where glcode in('251','252','253','254','255','256');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-25') where glcode in('286','288');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-26') where glcode in('451','452');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-27') where glcode in('321','325','327','328');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-28') where glcode in('351','352','353','354','355','357','358');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-29') where glcode in('361','362','363','364','365','367','368');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-30') where glcode in('371','372','373','374','378');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-31') where glcode in('384','385','386','388');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-32') where glcode in('411','412','413','414','415','416','417','418','419');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-33') where glcode in('432','433','434','435','438');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-34') where glcode in('441','442','443','445','448');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-35') where glcode in('481','482','483','484','485','486','488');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-36') where glcode in('491','498');
update chartofaccounts set paymentoperation='A', paymentscheduleid=(select id from schedulemapping where schedule='R-37') where glcode in('471','472','473','474','478','479');

update chartofaccounts set receiptoperation='A', receiptscheduleid=(select id from schedulemapping where schedule='R-18') where glcode in('481','482','483','484','485','486','488');
commit;


-- Bug ID 13303 starts code fix

update propertytaxintermediate pti set pti.period=(select financialyear from financialyear 
	   where substr(financialyear,1,4)=substr(period,1,4)
	   );
alter table propertytaxintermediate add  financialyearid number;
update propertytaxintermediate set financialyearid=(select id from financialyear where financialyear=period);
alter table propertytaxintermediate modify financialyearid not null;

alter table propertytaxintermediate add 
	(
	CONSTRAINT FK_fy_pti FOREIGN KEY (financialyearid) 
	    REFERENCES financialyear (ID)
	);
	
	
-- Bug ID 13303 ends code fix
