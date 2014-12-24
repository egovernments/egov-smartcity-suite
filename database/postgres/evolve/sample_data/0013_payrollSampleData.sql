#UP

/** create payheads **/
INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) 
VALUES ( EISPAYROLL_SALARYCODES_SEQ.nextval, 'Basic', 1, 1,  sysdate, 1,  sysdate, 'BASIC PAY', 'N'
, 'MonthlyFlatRate', (select id from chartofaccounts where glcode='2101001'), NULL, 1, NULL, 'BASIC PAY', NULL, 'Y', 'Y', 'Y'); 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) 
VALUES ( EISPAYROLL_SALARYCODES_SEQ.nextval, 'DA', 1, 1,  sysdate, 1,  sysdate
, 'DEARNESS ALLOWANCE', 'N', 'ComputedValue', (select id from chartofaccounts where glcode='2101002'), (select id from EGPAY_SALARYCODES where head = 'Basic'), 2, NULL, 'DEARNESS ALLOWANCE'
, NULL, 'Y', 'Y', 'Y'); 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'HRA', 2, 1,  sysdate, 1,  sysdate
, 'HRA', 'N', 'MonthlyFlatRate', (select id from chartofaccounts where glcode='2101003'), NULL, 3, NULL, 'HRA', 
NULL, 'Y', 'Y', 'Y'); 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'CCA', 2, 1,  sysdate, 1,  sysdate
, 'CITY ALLOWANCE', 'N', 'SlabBased', NULL, NULL, 4, NULL, 'CITY ALLOWANCE', NULL, 'Y'
, 'Y', 'Y'); 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'PF', 5, 1,  sysdate, 1,  sysdate
, 'PROVIDENT FUND', 'N', 'MonthlyFlatRate', NULL, NULL, 8, NULL, 'PROVIDENT FUND', NULL
, 'Y', 'Y', 'Y'); 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'PT', 3, 1,  sysdate, 1,  sysdate
, 'PT', 'N', 'MonthlyFlatRate', NULL, NULL, 9, NULL, 'PT', NULL, 'Y', 'Y', 'Y'); 


INSERT INTO EG_PARTYTYPE (ID, CODE, PARENTID,DESCRIPTION, CREATEDBY, CREATEDDATE,LASTMODIFIEDBY, LASTMODIFIEDDATE) 
			  VALUES (SEQ_EG_PARTYTYPE.nextval, 'Employee',null , 'Employee', 1,sysdate , 1, sysdate);

Insert into tds
	   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING,RECOVERY_MODE)
	 Values
	   (SEQ_tds.nextVal, 'CCA', (select id from chartofaccounts where glcode = 2101004), 1, sysdate, 1, 'Self', 'CCA', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '1','A');

Insert into eg_deduction_details
   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
 Values
   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 2549, sysdate, 0);
Insert into eg_deduction_details
   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
 Values
   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2550, 2999, sysdate, 55);
Insert into eg_deduction_details
   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
 Values
   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3000, 5499, sysdate, 80);
Insert into eg_deduction_details
   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
 Values
   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 5500, 7999, sysdate, 120);	
Insert into eg_deduction_details
   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, LASTMODIFIEDDATE, AMOUNT)
 Values
   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 8000, sysdate, 150);

UPDATE EGPAY_SALARYCODES
SET TDS_ID = (select id from tds where type='CCA'),
CAL_TYPE = 'SlabBased'
where UPPER(HEAD) LIKE 'CCA';

Insert into tds
   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING,RECOVERY_MODE)
 Values
   (SEQ_tds.nextVal, 'PT', (select id from chartofaccounts where glcode = 3502004), 1, sysdate, 1, 'PT-Govt', 'PT', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '0','M');

UPDATE EGPAY_SALARYCODES
SET TDS_ID = (select id from tds where type='PT')
where UPPER(HEAD) LIKE 'PT';

Insert into tds
   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING,RECOVERY_MODE)
 Values
   (SEQ_tds.nextVal, 'PF', (select id from chartofaccounts where glcode='3502001'), 1, sysdate, 1, 'PF-Govt', 'Provident Fund', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '0','M');

UPDATE EGPAY_SALARYCODES
SET TDS_ID = (select id from tds where type='PF')
where UPPER(HEAD) LIKE 'PF';


/** create payscales **/
INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE) 
VALUES (seq_payheader.nextval,'4440-7440','',to_date('01-Apr-2006'),4440,7440,'Ordinary');

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,4440,7440,100,(select id from egpay_payscale_header where name = '4440-7440'));
		    
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '4440-7440'),
 (select id from egpay_salarycodes where head = 'Basic'),0,4440);

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '4440-7440'),
 (select id from egpay_salarycodes where head = 'HRA'),0,900);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '4440-7440'),
 (select id from egpay_salarycodes where head = 'DA'),20,0);
 
INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE) 
VALUES (seq_payheader.nextval,'5200-20200','',to_date('01-Apr-2006'),5200,20200,'Ordinary');

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,5200,20200,100,(select id from egpay_payscale_header where name = '5200-20200'));

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
 (select id from egpay_salarycodes where head = 'Basic'),0,5200);

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
 (select id from egpay_salarycodes where head = 'HRA'),0,1100);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
 (select id from egpay_salarycodes where head = 'DA'),20,0);


INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE)  
VALUES (seq_payheader.nextval,'9300-34800','',to_date('01-Apr-2006'),9300,34800,'Ordinary');

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,9300,34800,100,(select id from egpay_payscale_header where name = '9300-34800'));

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '9300-34800'),
 (select id from egpay_salarycodes where head = 'Basic'),0,9300);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '9300-34800'),
 (select id from egpay_salarycodes where head = 'HRA'),0,2020);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '9300-34800'),
 (select id from egpay_salarycodes where head = 'DA'),25,0);

INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE)  VALUES 
(seq_payheader.nextval,'15600-39100','',to_date('01-Apr-2006'),15600,39100,'Ordinary');

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,15600,39100,100,(select id from egpay_payscale_header where name = '15600-39100'));

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '15600-39100'),
 (select id from egpay_salarycodes where head = 'Basic'),0,15600);

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '15600-39100'),
 (select id from egpay_salarycodes where head = 'HRA'),0,3150);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '15600-39100'),
 (select id from egpay_salarycodes where head = 'DA'),30,0);

INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE)  
VALUES (seq_payheader.nextval,'37400-67000','',to_date('01-Apr-2006'),37400,67000,'Ordinary');

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,37400,67000,100,(select id from egpay_payscale_header where name = '37400-67000'));

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '37400-67000'),
 (select id from egpay_salarycodes where head = 'Basic'),0,37400);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '37400-67000'),
 (select id from egpay_salarycodes where head = 'HRA'),0,6910);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '37400-67000'),
 (select id from egpay_salarycodes where head = 'DA'),35,0);


/** update employee payscale info **/

insert into egpay_payscale_employee (ID,ID_PAYHEADER,ID_EMPLOYEE,EFFECTIVEFROM,ANNUAL_INCREMENT,MONTHLY_PAY,DAILY_PAY)
	VALUES
	(SEQ_PAYSCALE_EMPLOYEE.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
	(select id from eg_employee where code=100),to_date('01-Apr-2008'),to_date('01-Jun-2009'),6400,null);

insert into egpay_payscale_employee (ID,ID_PAYHEADER,ID_EMPLOYEE,EFFECTIVEFROM,ANNUAL_INCREMENT,MONTHLY_PAY,DAILY_PAY)
	VALUES
	(SEQ_PAYSCALE_EMPLOYEE.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
	(select id from eg_employee where code=101),to_date('01-Apr-2008'),to_date('01-Jun-2009'),6400,null);

insert into egpay_payscale_employee (ID,ID_PAYHEADER,ID_EMPLOYEE,EFFECTIVEFROM,ANNUAL_INCREMENT,MONTHLY_PAY,DAILY_PAY)
	VALUES
	(SEQ_PAYSCALE_EMPLOYEE.nextval,(select id from egpay_payscale_header where name = '5200-20200'),
	(select id from eg_employee where code=102),to_date('01-Apr-2008'),to_date('01-Jun-2009'),6400,null);

/** create payslips **/

insert into egpay_emppayroll
(ID,ID_EMPLOYEE,ID_EMP_ASSIGNMENT,GROSS_PAY,NET_PAY,CREATEDBY,CREATEDDATE,
FINANCIALYEARID,NUMDAYS,MONTH,STATUS,BASIC_PAY,PAYTYPE,WORKINGDAYS,FROMDATE,TODATE,LASTMODIFIEDDATE)
values
(eispayroll_emppayroll_seq.nextval,(select id from eg_employee where code=100),
(select eea.id from eg_emp_assignment eea, eg_emp_assignment_prd eeap
     where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=100)),
     10100,9300,1,sysdate,(select id from financialyear where financialyear = '2008-09'),
30,1,(select id from egw_status where moduletype='PaySlip' and description='AuditApproved'),
6400,1,30,'01-Jan-2009','30-Jan-2009',sysdate);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'Basic'),6400);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'DA'),20,1280);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'HRA'),2300);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'CCA'),120);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'PF'),500);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=100) and month=1),
(select id from egpay_salarycodes where head = 'PT'),200);

/** create position hierarchy **/

INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,
ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,
EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,
TODATE ) VALUES ( 
SEQ_EG_USER.nextval, NULL, NULL, 'HOD', NULL, 'HOD', NULL, NULL, NULL, 'hod', 't27o223b7q3k0mtic20k1u32n'
, 'egovfinancials',  TO_Date( '03/27/2008 12:51:46 PM', 'MM/DD/YYYY HH:MI:SS AM'), NULL
, NULL, NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_Date( '03/21/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL); 


INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,
ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,
EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,
TODATE ) VALUES ( 
SEQ_EG_USER.nextval, NULL, NULL, 'accountofficer', NULL, 'accountofficer', NULL, NULL, NULL, 'accountofficer', 't27o223b7q3k0mtic20k1u32n'
, 'egovfinancials',  TO_Date( '03/27/2008 12:51:46 PM', 'MM/DD/YYYY HH:MI:SS AM'), NULL
, NULL, NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_Date( '03/21/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL); 


INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,
ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,
EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,
TODATE ) VALUES ( 
SEQ_EG_USER.nextval, NULL, NULL, 'AUDIT', NULL, 'AUDIT', NULL, NULL, NULL, 'audit', 't27o223b7q3k0mtic20k1u32n'
, 'egovfinancials',  TO_Date( '03/27/2008 12:51:46 PM', 'MM/DD/YYYY HH:MI:SS AM'), NULL
, NULL, NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_Date( '03/21/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL); 


INSERT INTO EG_USERROLE (  ID_ROLE, ID_USER, ID,   FROMDATE, TODATE, IS_HISTORY) 
	VALUES ((SELECT r.ID_ROLE FROM EG_ROLES r WHERE r.ROLE_NAME LIKE'SuperUser') ,(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'hod') , SEQ_EG_USERROLE.NEXTVAL, '01-Apr-2008','01-Apr-2099', 'N');

INSERT INTO EG_USERROLE (  ID_ROLE, ID_USER, ID,   FROMDATE, TODATE, IS_HISTORY) 
	VALUES ((SELECT r.ID_ROLE FROM EG_ROLES r WHERE r.ROLE_NAME LIKE'SuperUser') ,(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'accountofficer') , SEQ_EG_USERROLE.NEXTVAL, '01-Apr-2008','01-Apr-2099', 'N');

INSERT INTO EG_USERROLE (  ID_ROLE, ID_USER, ID,   FROMDATE, TODATE, IS_HISTORY) 
	VALUES ((SELECT r.ID_ROLE FROM EG_ROLES r WHERE r.ROLE_NAME LIKE'SuperUser') ,(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'audit') , SEQ_EG_USERROLE.NEXTVAL, '01-Apr-2008','01-Apr-2099', 'N');		



UPDATE EG_EMPLOYEE e SET e.ID_USER=(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'hod') WHERE e.CODE=101;
UPDATE EG_EMPLOYEE e SET e.ID_USER=(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'accountofficer') WHERE e.CODE=102;
UPDATE EG_EMPLOYEE e SET e.ID_USER=(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'audit') WHERE e.CODE=103;


INSERT INTO  EG_POSITION_HIR (id,position_from,position_to,object_type_id)VALUES
	   (SEQ_POSITION_HIR.NEXTVAL,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100) ,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101),
	   (SELECT o.ID FROM EG_OBJECT_TYPE o WHERE o.TYPE LIKE'payslip'));

INSERT INTO  EG_POSITION_HIR (id,position_from,position_to,object_type_id)VALUES
	   (SEQ_POSITION_HIR.NEXTVAL,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101) ,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=102),
	   (SELECT o.ID FROM EG_OBJECT_TYPE o WHERE o.TYPE LIKE'payslip'));	

INSERT INTO  EG_POSITION_HIR (id,position_from,position_to,object_type_id)VALUES
	   (SEQ_POSITION_HIR.NEXTVAL,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=102) ,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=103),
	   (SELECT o.ID FROM EG_OBJECT_TYPE o WHERE o.TYPE LIKE'payslip'));	   

/** create rule payhead **/

INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, pct_basis,
             order_id, local_lang_desc, isattendancebased, isrecomputed,
             isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'H.REN', 2, 1,
             sysdate, 1,
             sysdate,
             'HOUSE RENT', 'N', 'ComputedValue', 541, (select id from egpay_salarycodes where head='Basic'),
             5, 'H.Rent Allowance', 'Y', 'Y',
             'Y'
            );
INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, order_id,
             local_lang_desc, isattendancebased, isrecomputed, isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'pctOfBasicHRA', 1, 1,
            sysdate, 1,
             sysdate,
             'pctOfBasicHRA', 'N', 'RuleBased', 633, 11,
             'pctOfBasicHRA', 'N', 'N', 'Y'
            );

INSERT INTO EGPAY_EMPPAYROLL
            (ID, id_employee, id_emp_assignment, gross_pay, net_pay,
             createdby, createddate, financialyearid,
             MONTH, status, basic_pay, paytype,
             fromdate,
             todate, workingdays, numdays,
             lastmodifieddate
            )
     VALUES (eispayroll_emppayroll_seq.NEXTVAL, 
     		(select id from eg_employee where code=101), 
     		(select id from eg_emp_assignment where id_emp_assign_prd=(select id from eg_emp_assignment_prd where id_employee=(select id from eg_employee where code=101))), 
     		344, 341,
             1,sysdate, (select id from financialyear where financialyear = '2008-09'),
             4, (select id from egw_status where moduletype='PaySlip' and description='Created'), 2000, 1,
             TO_DATE ('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
             TO_DATE ('04/30/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 30, 5,
             sysdate
            );

Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='DA'), 10, 5.5);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval,
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='Basic'), 333);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='pctOfBasicHRA'), 34.4);
Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4),
   (select id from egpay_salarycodes where head='H.REN'), 10, 5.5);

Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4),
   (select id from egpay_salarycodes where head='HRA'), 10, 5.5);




Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicHRA') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'pctOfBasicHRA',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicHRA'));



Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID) Values (seq_chartofaccountdetail.nextval, 757, (select id from ACCOUNTDETAILTYPE where name like 'Nominee' ));
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID) Values (seq_chartofaccountdetail.nextval, 755, (select id from ACCOUNTDETAILTYPE where name like 'Nominee' ));
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID) Values (seq_chartofaccountdetail.nextval, 757, (select id from ACCOUNTDETAILTYPE where upper(name) like 'EMPLOYEE' ));
Insert into chartofaccountdetail (ID, GLCODEID, DETAILTYPEID) Values (seq_chartofaccountdetail.nextval, 755, (select id from ACCOUNTDETAILTYPE where upper(name) like 'EMPLOYEE' ));


update chartofaccounts c set c.PURPOSEID=33 where c.ID=757;
update chartofaccounts c set c.PURPOSEID=34 where c.ID=755;

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'VEHDED', 4, 1,  sysdate, 1,  sysdate
, 'VEHICLE ADVANCE', 'N', 'MonthlyFlatRate', (select id from chartofaccounts where glcode='1712003'), NULL, 7, NULL, 'VEHICLE ADVANCE', NULL
, 'Y', 'Y', 'Y'); 



/************* Payhead rule file*****************/

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED, ISRECURRING,
CAPTURE_RATE ) VALUES ( 
eispayroll_salarycodes_seq.NEXTVAL, 'GradePay', 2, 1,  TO_DATE( '09/15/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1,  TO_DATE( '09/07/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'GradePay', 'N'
, 'MonthlyFlatRate', (select id from chartofaccounts where glcode='2101014'), NULL, 6, NULL, 'Grade pay', NULL, 'N', 'N', 'Y', 'N'); 


INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, order_id,
             local_lang_desc, isattendancebased, isrecomputed, isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'pctOfBasicRule', 1, 1,
            sysdate, 1,
             sysdate,
             'pctOfBasicRule', 'N', 'RuleBased', 633, 12,
             'rule based payhead pct on basic', 'N', 'N', 'Y'
            );

Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='pctOfBasicRule'), 10, 5.5);

Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicRule') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'pctOfBasicRule',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasic'));


INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, order_id,
             local_lang_desc, isattendancebased, isrecomputed, isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'pctOfBasicGradePay', 1, 1,
            sysdate, 1,
             sysdate,
             'pctOfBasicGradePay', 'N', 'RuleBased', 633, 13,
             'rule based payhead pct on basic plus gradepay', 'N', 'N', 'Y'
            );

insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='pctOfBasicGradePay'), 10, 5.5);

insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='GradePay'), 10, 5.5);
   
insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicGradePay') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'pctOfBasicGradePay',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicGradePay'));



/********Added for pct of basic and attendance based Y rule file *******************/

INSERT INTO EGPAY_SALARYCODES
            (ID, head, categoryid, createdby,
             createddate, lastmodifiedby,
             lastmodifieddate,
             description, is_taxable, cal_type, glcodeid, order_id,
             local_lang_desc, isattendancebased, isrecomputed, isrecurring
            )
     VALUES (eispayroll_salarycodes_seq.NEXTVAL, 'pctOfBasicAttendanceY', 1, 1,
            SYSDATE, 1,
             SYSDATE,
             'pctOfBasicAttendanceBasedY', 'N', 'RuleBased', 633, 14,
             'rule based payhead pct on basic and attendance based', 'N', 'N', 'Y'
            );

INSERT INTO EGPAY_EARNINGS
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 VALUES
   (EISPAYROLL_EARNINGS_SEQ.NEXTVAL, 
   (SELECT id FROM EGPAY_EMPPAYROLL WHERE id_employee=(SELECT id FROM EG_EMPLOYEE WHERE code=101) AND MONTH=4 ),
   (SELECT id FROM EGPAY_SALARYCODES WHERE head='pctOfBasicAttendanceY'), 10, 5.5);
   
Insert into egpay_payhead_rule
   (ID, ID_SALARYCODE, EFFECTIVE_FROM, DESCRIPTION,ID_WF_ACTION)
 Values
   (EISPAYROLL_PAYHEAD_RULE_SEQ.nextval, (SELECT id FROM EGPAY_SALARYCODES s WHERE s.HEAD like 'pctOfBasicAttendanceY') , TO_DATE('01/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'pctOfBasicAttendanceY',(select wfa.id from eg_wf_actions wfa where wfa.type='PayHeadRule' and wfa.name='PctBasicAttendanceY'));

   
/***** Added for subledger details in salary bill **************/
insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where name='Salary Payable'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));

insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502001'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));
 
insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502004'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));

insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502005'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));

insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502006'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));

insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502007'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));

insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID)
 values
 (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3502008'),
 (select id from accountdetailtype where upper(name) ='EMPLOYEE'));


/***** added for testing MONTHLY FLAT RATE payhead can be attendancebased  *******/
INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) 
VALUES ( EISPAYROLL_SALARYCODES_SEQ.nextval, 'MonthlyFlatAttendance', 1, 1,  sysdate, 1,  sysdate, 'MonthlyFlatAttendanceBased', 'N'
, 'MonthlyFlatRate', (select id from chartofaccounts where glcode='2101003'), NULL, 10, NULL, 'MonthlyFlatAttendanceBased', NULL, 'Y', 'Y', 'Y'); 

Insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE, PCT, AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4),
   (select id from egpay_salarycodes where head='MonthlyFlatAttendance'), null, 1000);


/**Sample data added for view advance  which contains advance schedule also. ***/
INSERT INTO EGPAY_SALADVANCES ( ID, REQUESTED_AMT, ADVANCE_AMT, INTEREST_PCT, INTEREST_TYPE,
NUM_OF_INST, INTEREST_AMT, INST_AMT, PENDING_AMT, SANCTION_NUM, SANCTIONED_BY, SANCTIONED_DATE,
CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, ID_SALCODE, ID_EMPLOYEE, STATUS,
PAYMENT_TYPE, ADVANCE_TYPE, BANKACCOUNT_ID,
MAINTAIN_SCHEDULE ) VALUES ( 
EISPAYROLL_SALADVANCES_SEQ.NEXTVAL, 1200, 1200, 3, 'simple', 3, 9, 403, 1209, NULL, NULL, NULL, 1,  TO_Date( '07/24/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1,  TO_Date( '07/24/2009 12:57:06 PM', 'MM/DD/YYYY HH:MI:SS AM'), 9, 3, null, 'cash'
, 'interest',  NULL, 'Y'); 

INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3) , 1, 400, 0, 'Y'); 
INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3), 2, 400, 0, NULL); 
INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3), 3, 400, 0, NULL); 
INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3), 4, 0, 3, NULL); 
INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3), 5, 0, 3, NULL); 
INSERT INTO EGPAY_ADVANCE_SCHEDULE ( ID, ID_SALADVANCE, INSTALLMENT_NO, PRINCIPAL_AMT, INTEREST_AMT,
RECOVER ) VALUES ( 
EISPAYROLL_ADV_SCHEDULE_SEQ.nextval, (select id from egpay_saladvances where advance_amt=1200 and ID_EMPLOYEE=3), 6, 0, 3, NULL); 

Insert into EGPAY_SALADVANCES_ARF (ID,ADVANCE_ID) 
values (SEQ_EG_ADVANCEREQUISITION.nextval,(select id from egpay_saladvances where advance_amt=1200 and inst_amt=403));

Insert into EG_ADVANCEREQUISITION (ID,ADVANCEREQUISITIONNUMBER,ADVANCEREQUISITIONDATE,ADVANCEREQUISITIONAMOUNT,NARRATION,ARFTYPE,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,STATUSID,STATE_ID) 
values ((select id from egpay_saladvances_arf where advance_id=(select id from egpay_saladvances where advance_amt=1200 and inst_amt=403)),'ARF/H/VEHDED/8/2010-11',to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),1200,'Narration','Employee',1,to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),1,to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),228,null);

Insert into EG_ADVANCEREQUISITIONDETAILS (ID,ADVANCEREQUISITIONID,GLCODEID,CREDITAMOUNT,DEBITAMOUNT,LASTUPDATEDTIME,NARRATION,FUNCTIONID) 
values (SEQ_EG_ADVANCEREQDETAILS.nextval,(select id from egpay_saladvances_arf where advance_id=(select id from egpay_saladvances where advance_amt=1200 and inst_amt=403)),511,0,1200,to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),'Debit Narration',111);
Insert into EG_ADVANCEREQUISITIONDETAILS (ID,ADVANCEREQUISITIONID,GLCODEID,CREDITAMOUNT,DEBITAMOUNT,LASTUPDATEDTIME,NARRATION,FUNCTIONID) 
values (SEQ_EG_ADVANCEREQDETAILS.nextval,(select id from egpay_saladvances_arf where advance_id=(select id from egpay_saladvances where advance_amt=1200 and inst_amt=403)),841,4343,0,to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),'Credit Narration',111);

Insert into EG_ADVANCEREQUISITIONMIS (ID,ADVANCEREQUISITIONID,FUNDID,FIELDID,SUBFIELDID,FUNCTIONARYID,LASTUPDATEDTIME,DEPARTMENTID,FUNDSOURCEID,PAYTO,PAYBYDATE,SCHEMEID,SUBSCHEMEID,VOUCHERHEADERID,SOURCEPATH,PARTYBILLNUMBER,PARTYBILLDATE,REFERENCENUMBER) values 
(SEQ_EG_ADVANCEREQUISITIONMIS.nextval,(select id from egpay_saladvances_arf where advance_id=(select id from egpay_saladvances where advance_amt=1200 and inst_amt=403)),11,null,null,76,to_timestamp('04-06-10','DD-MM-RR HH12:MI:SSXFF AM'),10,null,'name',null,null,null,null,null,null,null,null);


INSERT INTO EGPAY_DEDUCTIONS ( ID, ID_EMPPAYROLL, ID_SALCODE, AMOUNT, ID_SAL_ADVANCE,
ID_ACCOUNTCODE, REFERENCE_NO, ID_ADVANCE_SCHEDULER ) VALUES ( 
eispayroll_deductions_seq.nextval, 1, (select id from egpay_salarycodes where head = 'VEHDED'), 400, NULL, NULL, NULL, (select id from EGPAY_ADVANCE_SCHEDULE where installment_no=1 and principal_amt=400 and recover='Y')); 


/**Advance sample data for deduction-bankloan **/
INSERT INTO EGPAY_SALADVANCES ( ID, REQUESTED_AMT, ADVANCE_AMT, INTEREST_PCT, INTEREST_TYPE,
NUM_OF_INST, INTEREST_AMT, INST_AMT, PENDING_AMT, SANCTION_NUM, SANCTIONED_BY, SANCTIONED_DATE,
CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, ID_SALCODE, ID_EMPLOYEE, STATUS,
PAYMENT_TYPE, ADVANCE_TYPE, BANKACCOUNT_ID,MAINTAIN_SCHEDULE ) VALUES 
( EISPAYROLL_SALADVANCES_SEQ.NEXTVAL, 1500, 1500, null,null,
3, null, 500, 1500, NULL, NULL, NULL, 
1,  TO_Date( '08/24/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1,  TO_Date( '08/24/2009 12:57:06 PM', 'MM/DD/YYYY HH:MI:SS AM'), 9, 3, 105,
 'cash', 'nonInterest',  null, 'N'); 

/********Update employee type master for salsrypayable code**********************/
UPDATE EGEIS_TYPE_MASTER set id_chartofaccount=(select id from CHARTOFACCOUNTS where purposeid=31);  



/********Junit for billaggregation for emptype amster chnages************/
INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,EMPLOYMENT_STATUS,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,110, 'Nirbhay', 'Nirbhay', TO_DATE('19-11-2003','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1964','dd-mm-yyyy'), 'NATTHU',sysdate,(select status_id from EGEIS_TYPE_MASTER where name like 'Contract'),3
);

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),(select id from eg_employee where code=110));

insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='MEDICAL OFFICER'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=110) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='A'),
(select id from eg_position where position_name='MEDICAL OFFICER_4'));



insert into egpay_emppayroll
(ID,ID_EMPLOYEE,ID_EMP_ASSIGNMENT,GROSS_PAY,NET_PAY,CREATEDBY,CREATEDDATE,
FINANCIALYEARID,NUMDAYS,MONTH,STATUS,BASIC_PAY,PAYTYPE,WORKINGDAYS,FROMDATE,TODATE,LASTMODIFIEDDATE)
values
(eispayroll_emppayroll_seq.nextval,(select id from eg_employee where code=110),
(select eea.id from eg_emp_assignment eea, eg_emp_assignment_prd eeap
     where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=110)),
     10100,9300,1,sysdate,(select id from financialyear where financialyear = '2008-09'),
30,1,(select id from egw_status where moduletype='PaySlip' and description='AuditApproved'),
6400,1,30,'01-Jan-2009','30-Jan-2009',sysdate);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=110) and month=1),
(select id from egpay_salarycodes where head = 'Basic'),6400);

insert into egpay_earnings
(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
values
(eispayroll_earnings_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=110) and month=1),
(select id from egpay_salarycodes where head = 'DA'),20,1280);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=110) and month=1),
(select id from egpay_salarycodes where head = 'PF'),500);

insert into egpay_deductions
(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
values
(eispayroll_deductions_seq.nextval,(select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=110) and month=1),
(select id from egpay_salarycodes where head = 'PT'),200);

/************ Sample data for PF Setup *************/
INSERT INTO EGPAY_PFHEADER ( ID, PFACCOUNTID, PFINTEXPACCOUNTID, FREQUENCY, TDS_ID, ID_WF_ACTION,
PF_TYPE ) VALUES ( 
SEQ_PFHEADER.nextval , (select id from chartofaccounts where glcode='3502001' and isactiveforposting='1'), 
(select id from chartofaccounts where glcode='2405000' and isactiveforposting='1'), 'Monthly', NULL, NULL, 'GPF'); 

/************ Sample data on egpay_salarycodes *************/

 update EGPAY_SALARYCODES
  set CAPTURE_RATE = 'Y' WHERE HEAD='Basic';
  
/********* sample data modification for payslip generation using rule based increment with scripts.*********************************/

INSERT INTO EGPAY_PAYSCALE_DETAILS ( ID, ID_PAYHEADER, ID_SALARYCODES, PCT,
AMOUNT ) VALUES ( 
SEQ_PAYHEADER_DETAILS.nextval, (select id from egpay_payscale_header where name = '5200-20200'),
 (select id from egpay_salarycodes where head = 'GradePay'), 0, 1000);


/**********sample data for CPF ************************************/ 

INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,ISRECURRING,CAPTURE_RATE ) VALUES ( 
EISPAYROLL_SALARYCODES_SEQ.nextval, 'CPF', 5, 1,  sysdate, 1,  sysdate
, 'Contribution to Provident Fund', 'N', 'MonthlyFlatRate', NULL, NULL, 15, NULL, '���',NULL, 'Y', 'Y', 'Y','N'); 


Insert into tds
   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING,RECOVERY_MODE)
 Values
   (SEQ_tds.nextVal, 'CPF', (select id from chartofaccounts where glcode = 3502008), 1, sysdate, 1, 'CPF-Govt', 'Contribution to Provident Fund', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '0','M');

UPDATE EGPAY_SALARYCODES
SET TDS_ID = (select id from tds where type='CPF')
where UPPER(HEAD) LIKE 'CPF';

  /**********sample data for Ot pay  ************************************/ 
INSERT INTO EGPAY_SALARYCODES ( ID, HEAD, CATEGORYID, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY,
LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID,
LOCAL_LANG_DESC, INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED,
ISRECURRING ) 
VALUES ( EISPAYROLL_SALARYCODES_SEQ.nextval, 'Ot', 1, 1,  sysdate, 1,  sysdate, 'Ot Pay', 'N'
, 'RuleBased', (select id from chartofaccounts where glcode='2305009'), NULL, 16, NULL, 'Ot pay', NULL, 'Y', 'Y', 'Y'); 

insert into egpay_earnings
   (ID, ID_EMPPAYROLL, ID_SALCODE,AMOUNT)
 Values
   (EISPAYROLL_EARNINGS_SEQ.nextval, 
   (select id from egpay_emppayroll where id_employee=(select id from eg_employee where code=101) and month=4 ),
   (select id from egpay_salarycodes where head='Ot'),100);

   insert into egeis_attendence(ID,ATT_DATE,EMP_ID,MONTH,FIN_YEAR_ID,TYPE_ID)

values(SEQ_ATTENDENCE.NEXTVAL,'01-Apr-2008',(select id from eg_employee where code=101),4,(select id from financialyear where financialyear = '2008-09'),
(select id from egeis_att_type where type_value ='OverTime'));


   /**********sample Payscale with rule  ************************************/ 
INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE,ID_WF_ACTION) 
VALUES (seq_payheader.nextval,'5000-8000','',to_date('01-Jan-2008'),5000,8000,'Ordinary'
,(select id from EG_WF_ACTIONS w where w.DESCRIPTION like 'Increment Basic Plus Gradepay for probation'));

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,5000,8000,500,(select id from egpay_payscale_header where name = '5000-8000'));
		    
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5000-8000'),
 (select id from egpay_salarycodes where head = 'Basic'),0,5500);

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5000-8000'),
 (select id from egpay_salarycodes where head = 'HRA'),0,750);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '5000-8000'),
 (select id from egpay_salarycodes where head = 'GradePay'),0,500);
 
 
 INSERT INTO egpay_payscale_header(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO,TYPE,ID_WF_ACTION) 
VALUES (seq_payheader.nextval,'6000-9000','',to_date('01-Jan-2008'),6000,9000,'Ordinary'
,(select id from EG_WF_ACTIONS w where w.DESCRIPTION like 'Increment Basic Plus Gradepay'));

INSERT INTO EGPAY_PAYSCALE_INCRDETAILS (ID,incSlabFrmAmt,incSlabToAmt,incSlabAmt,ID_PAYHEADER)			    
VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,6000,9000,450,(select id from egpay_payscale_header where name = '5000-8000'));
		    
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '6000-9000'),
 (select id from egpay_salarycodes where head = 'Basic'),0,6500);

INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
  VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '6000-9000'),
 (select id from egpay_salarycodes where head = 'HRA'),0,750);
 
INSERT INTO egpay_payscale_details (ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
   VALUES (SEQ_PAYHEADER_DETAILS.nextval,(select id from egpay_payscale_header where name = '6000-9000'),
 (select id from egpay_salarycodes where head = 'GradePay'),0,500);
 
 

INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,
ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,
EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,
TODATE ) VALUES ( 
SEQ_EG_USER.NEXTVAL, NULL, NULL, 'Nirbhay', NULL, 'Nirbhay', NULL, NULL, NULL, 'Nirbhay', 't27o223b7q3k0mtic20k1u32n'
, 'egovfinancials',  TO_DATE( '03/27/2008 12:51:46 PM', 'MM/DD/YYYY HH:MI:SS AM'), NULL
, NULL, NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_DATE( '03/21/2008 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL); 


UPDATE EG_EMPLOYEE e SET e.ID_USER=(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'Nirbhay') WHERE e.CODE=110;
#DOWN

