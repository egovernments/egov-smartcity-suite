#UP

/***Menue tree for payslip exception report********/

INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
SEQ_EG_ACTION.nextval, 'PayslipExceptionReport', NULL, NULL,  TO_Date( '02/04/2009 07:03:33 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/reports/payslipExceptionReport.jsp', '', NULL, (select ID_MODULE from eg_module where module_name like 'Payslip'), 2, 'Payslips exception report'
, 1, ''); 

Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   ((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'PayslipExceptionReport'));



/***Added for deleting qurtz scheduler AutoGenerationPaySlipJobScheduler*****/


DELETE FROM QRTZ_CRON_TRIGGERS  WHERE trigger_name IN (SELECT t.trigger_name FROM QRTZ_TRIGGERS t WHERE t.job_name IN (SELECT q.job_name FROM  QRTZ_JOB_DETAILS q WHERE q.JOB_CLASS_NAME LIKE 'org.egov.payroll.client.payslip.AutoGenerationPaySlipJobScheduler'));

DELETE FROM QRTZ_TRIGGERS t WHERE t.job_name IN (SELECT q.job_name FROM  QRTZ_JOB_DETAILS q WHERE q.JOB_CLASS_NAME LIKE 'org.egov.payroll.client.payslip.AutoGenerationPaySlipJobScheduler');

DELETE FROM QRTZ_JOB_DETAILS q WHERE q.JOB_CLASS_NAME LIKE 'org.egov.payroll.client.payslip.AutoGenerationPaySlipJobScheduler';

#DOWN