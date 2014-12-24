#UP

/*********added a row for contigency bill in eg_number table*********/

/* Insert into eg_numbers
(ID, VOUCHERTYPE, VOUCHERNUMBER, FISCIALPERIODID)
Values
(249, 'CEN', 1, 7); */


    
/***********inserted a row in egw_status for PensionDetails ststus on 08/09/2008**********/

INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','Created',sysdate);
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','Approved',sysdate);
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','GratuityDisbursed',sysdate);
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','Suspended',sysdate);
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','PensionTerminated',sysdate);


/***********inserted a row in eg_object_type for gratuity on 09/09/2008 **********/

insert into  EG_OBJECT_TYPE values (seq_object_type.nextVal,'Gratuity','Gratuity',sysdate);

   
/***********inserted a row in egw_status for PensionDetails ststus on 11/09/2008 **************/
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','Submitted',sysdate);
INSERT INTO  EGW_STATUS( ID, MODULETYPE, DESCRIPTION,LASTMODIFIEDDATE ) VALUES (SEQ_EGW_STATUS.nextval,'PensionDetails','Cancelled',sysdate);

/*****16/09/2008 - finance integration purpose id glcode mapping for gratuity/pension payable*/

insert into  EGF_ACCOUNTCODE_PURPOSE values (33,'Gratuity Payable');
insert into  EGF_ACCOUNTCODE_PURPOSE values (34,'Pension Payable');

 INSERT INTO ACCOUNTDETAILTYPE ( ID, NAME, DESCRIPTION, TABLENAME, COLUMNNAME, ATTRIBUTENAME,
NBROFLEVELS, ISACTIVE, CREATED, LASTMODIFIED, MODIFIEDBY ) VALUES (
SEQ_ACCOUNTDETAILTYPE.nextval, 'Nominee', 'Nominee', 'accountEntityMaster', 'id', 'Nominee_id', 1, 1, TO_DATE( '09/09/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL);


Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (seq_chartofaccountdetail.nextval, 757, (select id from ACCOUNTDETAILTYPE where name like 'Nominee' ), 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (seq_chartofaccountdetail.nextval, 755, (select id from ACCOUNTDETAILTYPE where name like 'Nominee' ), 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (seq_chartofaccountdetail.nextval, 757, (select id from ACCOUNTDETAILTYPE where upper(name) like 'EMPLOYEE' ), 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (seq_chartofaccountdetail.nextval, 755, (select id from ACCOUNTDETAILTYPE where upper(name) like 'EMPLOYEE' ), 1);


update chartofaccounts c set c.PURPOSEID=33 where c.ID=757;
update chartofaccounts c set c.PURPOSEID=34 where c.ID=755;



Insert into eg_module
   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
 Values
   (SEQ_MODULEMASTER.nextval, 'Pay-Pension', TO_DATE('09/19/2008 16:45:17', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Pension', (select ID_MODULE from eg_module where module_name like 'Payroll'), 'Pension');
Insert into eg_module
   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
 Values
   (SEQ_MODULEMASTER.nextval, 'ComputeGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Compute Gratuity', (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Compute Gratuity');


Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'viewGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=view', 2, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 2, 'View', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'createGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 1, 'Create', 1);


Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   ((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'createGratuity'));
Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   ((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'viewGratuity'));


 /********* ADDED ON 26/09/2008 **********************/
 
 Insert into eg_module
     (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
   Values
     (SEQ_MODULEMASTER.nextval, 'Recoveries', TO_DATE('09/26/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Recoveries', 
     (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Recoveries');
  
  
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'RecordRecoveries', TO_DATE('09/26/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), '/recordRecovery/search.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'Recoveries'), 1, 'Record Recoveries', 1);
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'modifyGratuity', TO_DATE('09/26/2008 14:16:51', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=modify', 3, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 3, 'Modify', 1);
  
  
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     ((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'modifyGratuity'));
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     ((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'RecordRecoveries'));


/********** ADDED ON 17/10/2008 ****************/
delete from eg_roleaction_map where actionid in (select id from eg_action where NAME='modifyGratuity');
delete from eg_action a where a.NAME='modifyGratuity';

/****01/11/2008 menue tree fro gratuity disburse***********/
Insert into eg_module
(ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
Values
(SEQ_MODULEMASTER.nextval, 'Disburse Gratuity', TO_DATE('10/31/2008 16:29:33', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Disburse Gratuity', 
(select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Disburse Gratuity');


Insert into eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
Values
(SEQ_EG_ACTION.nextval, 'createDisburseGratuity', TO_DATE('10/31/2008 16:30:14', 'MM/DD/YYYY HH24:MI:SS'), '/pension/searchForGratuityDisburse.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'Disburse Gratuity'), 1, 'Create', 1);
Insert into eg_action
(ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
Values
(SEQ_EG_ACTION.nextval, 'viewDisburseGratuity', TO_DATE('10/31/2008 16:30:14', 'MM/DD/YYYY HH24:MI:SS'), '/pension/searchForGratuityDisburse.jsp', 'mode=view', 2, (select ID_MODULE from eg_module where module_name like 'Disburse Gratuity'), 2, 'View', 1);


Insert into eg_roleaction_map
(ROLEID, ACTIONID)
Values
((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'createDisburseGratuity'));
Insert into eg_roleaction_map
(ROLEID, ACTIONID)
Values
((select e.id_ROLE from eg_roles e where e.ROLE_NAME='SUPER USER'), (select ID from eg_action where name like 'viewDisburseGratuity')); 
   
#DOWN