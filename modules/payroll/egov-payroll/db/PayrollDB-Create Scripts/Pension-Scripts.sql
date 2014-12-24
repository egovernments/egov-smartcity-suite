/*********ADDED BILL REGISTER REFERENCE FROM PENSION DETAILS TO EG_BILLREGISTER*****/

ALTER TABLE EGPAY_PENSION_DETAILS
  ADD ID_BILLREGISTER NUMBER(12);

ALTER TABLE EGPAY_PENSION_DETAILS ADD 
CONSTRAINT FK_ID_BILLREGISTER_PENDET
 FOREIGN KEY (ID_BILLREGISTER)
 REFERENCES EG_BILLREGISTER (ID) ENABLE
 VALIDATE;

/*********added a row for contigency bill in eg_number table*********/

Insert into eg_numbers
(ID, VOUCHERTYPE, VOUCHERNUMBER, FISCIALPERIODID)
Values
(249, 'CEN', 1, 7);
COMMIT;


/************* EIS DB changes 05-sep-2008 *************************/
ALTER TABLE EG_EMPLOYEE
   RENAME COLUMN STATUS_ID TO EMPLOYMENT_STATUS;

   ALTER TABLE EG_EMPLOYEE
  ADD STATUS NUMBER(12);


ALTER TABLE EG_EMPLOYEE
 ADD CONSTRAINT STATUS_EGW_FK
 FOREIGN KEY (STATUS)
 REFERENCES EGW_STATUS (ID);


 INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'Pension','Employed','01/sep/2008');
 
 INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'Pension','Retired','01/sep/2008');

 INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'Pension','Deceased','01/sep/2008');

 INSERT INTO  EGW_STATUS E (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE) VALUES (SEQ_EGW_STATUS.nextval, 'Pension','Suspended','01/sep/2008');

 UPDATE EG_EMPLOYEE E SET E.STATUS=(SELECT ID FROM EGW_STATUS WHERE DESCRIPTION LIKE 'Employed') WHERE E.ISACTIVE=1;

 UPDATE EG_EMPLOYEE E SET E.STATUS=(SELECT ID FROM EGW_STATUS WHERE DESCRIPTION LIKE 'Suspended') WHERE E.ISACTIVE=0;

 

   Insert into egeis_nominee_type
      (ID, NOMINEE_TYPE, FULL_BENEFIT_ELEGIBLE, GENDER, ELEGIBLE_AGE, ELIG_STATUS_IF_MARRIED, ELIG_STATUS_IF_EMPLOYED)
    Values
      (3, 'DAUGHTER', 0, 'F', 25, '0', '0');
   Insert into egeis_nominee_type
      (ID, NOMINEE_TYPE, FULL_BENEFIT_ELEGIBLE, GENDER)
    Values
      (4, 'HUSBAND', 1, 'M');
   COMMIT;
   
  ALTER TABLE EGEIS_NOMINEE_DETAILS
     ADD NAME VARCHAR2(256);
     
   UPDATE  EGEIS_NOMINEE_TYPE n SET n.ELEGIBLE_AGE=25 WHERE n.NOMINEE_TYPE='SON';
     
    
/************** Menu Tree Items ***********************/
Insert into eg_module
   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, PARENTID, MODULE_DESC)
 Values
   (SEQ_MODULEMASTER.nextval, 'Pension', TO_DATE('09/05/2008 17:44:32', 'MM/DD/YYYY HH24:MI:SS'), 1, (select ID_MODULE from eg_module where module_name like 'EIS'), 'pension');
COMMIT;

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'CreatePension', TO_DATE('09/05/2008 17:44:58', 'MM/DD/YYYY HH24:MI:SS'), '/empPension/BeforePensionSearchAction.do', 'submitType=execute\&mode=populateDetails', 1, (select ID_MODULE from eg_module where module_name like 'Pension'), 'Create', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'ViewPension', TO_DATE('09/05/2008 17:44:58', 'MM/DD/YYYY HH24:MI:SS'), '/empPension/BeforePensionSearchAction.do', 'submitType=execute\&mode=beforeViewDetails', 2, (select ID_MODULE from eg_module where module_name like 'Pension'), 'View', 1);
COMMIT;

Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   (5, (select ID from eg_action where name like 'CreatePension'));
Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   (5, (select ID from eg_action where name like 'ViewPension'));
COMMIT;
  
  
ALTER TABLE EGEIS_NOMINEE_DETAILS
  ADD GRATUITY_AMOUNT NUMBER(12);
  
  ALTER TABLE EGEIS_NOMINEE_DETAILS
  ADD GROSS_PAY_AMOUNT NUMBER(12);  
  
    
/***********inserted a row in egw_status for PensionDetails ststus on 08/09/2008**********/

insert into  egw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','Created',sysdate);
insert into  egw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','Approved',sysdate);
insert into  egw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','GratuityDisbursed',sysdate);
insert into  egw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','Suspended',sysdate);
insert into  egw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','PensionTerminated',sysdate);


/***********inserted a row in eg_object_type for gratuity on 09/09/2008 **********/

insert into  EG_OBJECT_TYPE values (10,'Gratuity','Gratuity',sysdate);

/******* Changed Bank Id to branch ID in Nominee details table on 10/09/2008 *********/
alter table EGEIS_NOMINEE_DETAILS drop constraint FK_BANK;
alter table EGEIS_NOMINEE_DETAILS RENAME COLUMN ID_BANK TO ID_BRANCH;

ALTER TABLE EGEIS_NOMINEE_DETAILS ADD 
CONSTRAINT FK_BANKBRANCH
FOREIGN KEY (ID_BRANCH)
REFERENCES BANKBRANCH (ID) ENABLE
VALIDATE;
 
 /******* Account Entity changes on 10/09/2008 *********/
 ALTER TABLE EGEIS_NOMINEE_DETAILS
   ADD ACCOUNTENTITY_ID NUMBER(12);
   
   ALTER TABLE EGEIS_NOMINEE_DETAILS
    ADD CONSTRAINT FK_ACCOUNTDETAILKEY
    FOREIGN KEY (ACCOUNTENTITY_ID)
  REFERENCES ACCOUNTENTITYMASTER (ID);
  
  ALTER TABLE  EGEIS_NOMINEE_DETAILS
  MODIFY (ACCOUNTENTITY_ID  NUMBER(22) );
  
  
 /***********inserted a row in egw_status for PensionDetails ststus on 11/09/2008 **************/
  insert into  EGw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','Submitted',sysdate);
  insert into  EGw_status values (SEQ_EGW_STATUS.nextval,'PensionDetails','Cancelled',sysdate);
  
  ALTER TABLE EGPAY_PENSION_DETAILS
    ADD CREATED_BY NUMBER(12);
  
  ALTER TABLE EGPAY_PENSION_DETAILS ADD 
  CONSTRAINT FK_CREATEDBY_PD
   FOREIGN KEY (CREATED_BY)
   REFERENCES EG_USER (ID_USER) ENABLE
   VALIDATE;           
/*****16/09/2008 - finance integration purpose id glcode mapping for gratuity/pension payable*/

insert into  EGF_ACCOUNTCODE_PURPOSE values (33,'Gratuity Payable');
insert into  EGF_ACCOUNTCODE_PURPOSE values (34,'Pension Payable');


Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (24, 757, 8, 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (25, 755, 8, 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (26, 757, 4, 1);
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID, ISCONTROLCODE) Values (27, 755, 4, 1);


update chartofaccounts c set c.PURPOSEID=33 where c.ID=757;
update chartofaccounts c set c.PURPOSEID=34 where c.ID=755;

COMMIT;

Insert into eg_module
   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
 Values
   (SEQ_MODULEMASTER.nextval, 'Pay-Pension', TO_DATE('09/19/2008 16:45:17', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Pension', (select ID_MODULE from eg_module where module_name like 'Payroll'), 'pension');
Insert into eg_module
   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
 Values
   (SEQ_MODULEMASTER.nextval, 'ComputeGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Compute Gratuity', (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Compute Gratuity');
COMMIT;

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'viewGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=view', 2, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 2, 'View', 1);
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'createGratuity', TO_DATE('09/19/2008 16:54:13', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 1, 'Create', 1);
COMMIT;

Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   (5, (select ID from eg_action where name like 'createGratuity'));
Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   (5, (select ID from eg_action where name like 'viewGratuity'));

COMMIT;

/********* ADDED ON 23/09/2008 TO change column names and set default values for husband and wife ********************/
alter table EGEIS_NOMINEE_TYPE rename column FULL_BENEFIT_ELEGIBLE TO FULL_BENEFIT_ELIGIBLE;
alter table EGEIS_NOMINEE_TYPE rename column ELEGIBLE_AGE TO ELIGIBLE_AGE;

update EGEIS_NOMINEE_TYPE NT SET ELIGIBLE_AGE=200 WHERE NT.NOMINEE_TYPE='HUSBAND' or  NT.NOMINEE_TYPE='WIFE';
update EGEIS_NOMINEE_TYPE NT SET NT.ELIG_STATUS_IF_MARRIED=2 WHERE NT.NOMINEE_TYPE='HUSBAND' or  NT.NOMINEE_TYPE='WIFE';
update EGEIS_NOMINEE_TYPE NT SET NT.ELIG_STATUS_IF_EMPLOYED=2 WHERE NT.NOMINEE_TYPE='HUSBAND' or  NT.NOMINEE_TYPE='WIFE';

COMMIT;

/* Drop unique constraint on NAME column in ACCOUNTENTITYMASTER -shd go to egf script*/
alter table ACCOUNTENTITYMASTER drop constraint SYS_C007084;	
COMMIT;


/********* ADDED ON 25/09/2008 **********************/

Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, DISPLAY_NAME, IS_ENABLED)
 Values
   (SEQ_EG_ACTION.nextval, 'ModifyPension', TO_DATE('09/05/2008 17:44:58', 'MM/DD/YYYY HH24:MI:SS'), '/empPension/BeforePensionSearchAction.do', 'submitType=execute\&mode=beforeModifyDetails', 1, (select ID_MODULE from eg_module where module_name like 'Pension'), 'Modify', 1);
   
 
Insert into eg_roleaction_map
   (ROLEID, ACTIONID)
 Values
   (5, (select ID from eg_action where name like 'ModifyPension'));
 
 COMMIT;

 /********* ADDED ON 26/09/2008 **********************/
 
 Insert into eg_module
     (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
   Values
     (SEQ_MODULEMASTER.nextval, 'Recoveries', TO_DATE('09/26/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Recoveries', 
     (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Recoveries');
  COMMIT;
  
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'RecordRecoveries', TO_DATE('09/26/2008 13:04:29', 'MM/DD/YYYY HH24:MI:SS'), '/recordRecovery/search.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'Recoveries'), 1, 'Record Recoveries', 1);
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'modifyGratuity', TO_DATE('09/26/2008 14:16:51', 'MM/DD/YYYY HH24:MI:SS'), '/pension/search.jsp', 'mode=modify', 3, (select ID_MODULE from eg_module where module_name like 'ComputeGratuity'), 3, 'Modify', 1);
  COMMIT;
  
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     (5, (select ID from eg_action where name like 'modifyGratuity'));
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     (5, (select ID from eg_action where name like 'RecordRecoveries'));
 
 commit;
 
 /******** ADDED ON 29/09/2008  ************/
 /********- shd go to egf script*/
 /*alter table eg_billdetails add NARRATION  VARCHAR2(250);  
 commit;*/


/********** ADDED ON 17/10/2008 ****************/
delete from eg_action a where a.NAME='modifyGratuity';

UPDATE EG_ACTION SET URL='/pims/BeforeSearchAction.do' , QUERYPARAMS='module=Pension\&masters=Pension\&mode=Modify' WHERE NAME ='ModifyPension'; 
UPDATE EG_ACTION SET URL='/pims/BeforeSearchAction.do' , QUERYPARAMS='module=Pension\&masters=Pension\&mode=Create' WHERE NAME ='CreatePension'; 
UPDATE EG_ACTION SET URL='/pims/BeforeSearchAction.do' , QUERYPARAMS='module=Pension\&masters=Pension\&mode=View' WHERE NAME ='ViewPension';


 CREATE OR REPLACE FORCE VIEW EG_EIS_EMPLOYEEINFO
	  (ASS_ID, PRD_ID, ID, CODE, NAME,
	   DESIGNATIONID, FROM_DATE, TO_DATE, REPORTS_TO, DATE_OF_FA,
	   ISACTIVE, DEPT_ID, FUNCTIONARY_ID, POS_ID, USER_ID,
	   STATUS)
	  AS
	  SELECT EEA.ID,EAP.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME,
	  EEA.DESIGNATIONID,EAP.FROM_DATE,EAP.TO_DATE,EEA.REPORTS_TO,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,
	  EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER,ee.STATUS
	  FROM EG_EMP_ASSIGNMENT_PRD EAP, EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE
	  WHERE EE.ID = EAP.ID_EMPLOYEE
	  AND EAP.ID=EEA.ID_EMP_ASSIGN_PRD;

/*****29/10/2008 for voucher reference from pensiondetails disburse gratuity*******/
ALTER TABLE EGPAY_PENSION_DETAILS
  ADD VOUCHER_HEADER_ID NUMBER(12);

ALTER TABLE EGPAY_PENSION_DETAILS ADD 
CONSTRAINT FK_ID_VOUCHER_HEADER
 FOREIGN KEY (VOUCHER_HEADER_ID)
 REFERENCES VOUCHERHEADER (ID) ENABLE
 VALIDATE;

/****01/11/2008 menue tree fro gratuity disburse***********/
Insert into eg_module
     (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
   Values
     (SEQ_MODULEMASTER.nextval, 'Disburse Gratuity', TO_DATE('10/31/2008 16:29:33', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Disburse Gratuity', 
     (select ID_MODULE from eg_module where module_name like 'Pay-Pension'), 'Disburse Gratuity');
  COMMIT;
  
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'createDisburseGratuity', TO_DATE('10/31/2008 16:30:14', 'MM/DD/YYYY HH24:MI:SS'), '/pension/searchForGratuityDisburse.jsp', 'mode=create', 1, (select ID_MODULE from eg_module where module_name like 'Disburse Gratuity'), 1, 'Create', 1);
  Insert into eg_action
     (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
   Values
     (SEQ_EG_ACTION.nextval, 'viewDisburseGratuity', TO_DATE('10/31/2008 16:30:14', 'MM/DD/YYYY HH24:MI:SS'), '/pension/searchForGratuityDisburse.jsp', 'mode=view', 2, (select ID_MODULE from eg_module where module_name like 'Disburse Gratuity'), 2, 'View', 1);
  COMMIT;
  
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     (5, (select ID from eg_action where name like 'createDisburseGratuity'));
  Insert into eg_roleaction_map
     (ROLEID, ACTIONID)
   Values
     (5, (select ID from eg_action where name like 'viewDisburseGratuity')); 

commit;