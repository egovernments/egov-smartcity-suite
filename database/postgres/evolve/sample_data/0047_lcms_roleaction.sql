#UP

-- sample data for User
INSERT INTO EG_USER (ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, 
UPDATEUSERID, EXTRAFIELD1,EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,TODATE)
VALUES (SEQ_EG_USER.nextval, NULL, NULL, 'lc_officer', NULL, NULL, NULL, NULL, NULL, 'coclcms', '-13ulkptjb9cn', 'egovfinancials',  SYSDATE, NULL, NULL, 
NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_DATE( '05/05/2099 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

INSERT INTO EG_USER (ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,ID_DEPARTMENT, LOCALE, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, 
UPDATEUSERID, EXTRAFIELD1,EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO, ISACTIVE, FROMDATE,TODATE)
VALUES (SEQ_EG_USER.nextval, NULL, NULL, 'lc_officer1', NULL, NULL, NULL, NULL, NULL, 'coclcms1', '-13ulkptjb9cn', 'egovfinancials',  SYSDATE, NULL, NULL, 
NULL, NULL, NULL, 'N', NULL, NULL, 1,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_DATE( '05/05/2099 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

-- sample data for Roles
INSERT INTO EG_ROLES (ID_ROLE, ROLE_NAME, ROLE_DESC, ROLE_NAME_LOCAL, ROLE_DESC_LOCAL, UPDATETIME,UPDATEUSERID) 
VALUES (SEQ_EG_ROLES.nextval, 'LCO', 'LEGAL CELL OFFICER', NULL, NULL,  SYSDATE, NULL); 

Insert into eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 Values
   (SEQ_EG_USER_JURLEVEL.nextVal, (select id_user from eg_user where first_name='lc_officer'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

 Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_user_jurlevel from eg_user_jurlevel where id_user=(select id_user from eg_user where first_name='lc_officer')), 1, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
 

-- sample data for User Role
INSERT INTO EG_USERROLE (ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY) 
VALUES ((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'), (select ID_USER from eg_user where USER_NAME ='coclcms'),
SEQ_EG_USERROLE.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), NULL, 'N'); 
INSERT INTO EG_USERROLE (ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY) 
VALUES ((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'), (select ID_USER from eg_user where USER_NAME ='coclcms1'),
SEQ_EG_USERROLE.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), NULL, 'N'); 


/* Formatted on 2009/08/29 22:34 (Formatter Plus v4.8.5) */
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/governmentDept!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/interimOrder!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/judgmentType!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!edit.action') and a.queryparams = 'type=Search'));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/advocateMaster!search.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcase!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/casetypeMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/courttypeMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/courtMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/dueDateReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/employeeReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/judgmentReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/pwrReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/employeeReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/judgmentReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/pwrReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!search.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!aggregatedReports.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!flatReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!edit.action') and a.queryparams = 'type=Report'));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/eGov_LCMS.jsp')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.name IN ('DEFAULT')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcase!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/hearings!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/hearings!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/lcinterimorder!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/lcinterimorder!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/vacateStay!edit.action'))); 
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgment!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgment!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgmentImplement!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgmentImplement!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcaseDisposal!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcaseDisposal!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/timeSeriesReport!edit.action')));          
INSERT INTO eg_roleaction_map VALUES ((SELECT id_role FROM eg_roles WHERE role_name = 'LCO'),(SELECT a.ID FROM eg_action a WHERE a.url IN ('/workflow/inbox.action') and context_root='egi'));
INSERT INTO eg_roleaction_map VALUES ((SELECT id_role FROM eg_roles WHERE role_name = 'LCO'),(SELECT a.ID FROM eg_action a WHERE a.url IN ('/eGov.jsp') and context_root='egi'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select ID_ROLE from eg_roles where ROLE_NAME like 'LCO'),(select ID from EG_ACTION where NAME like 'Standing Counse Fee Entry')); 
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ((select ID_ROLE from eg_roles where ROLE_NAME like 'LCO'),(select ID from EG_ACTION where NAME like 'View Past Payment'));


DELETE FROM EG_ROLEACTION_MAP WHERE ROLEID = (select ID_ROLE from eg_roles where ROLE_NAME like 'SuperUser')
AND ACTIONID = (select ID from EG_ACTION where NAME like 'Standing Counse Fee Entry'); 
        
-- sample data for designation
INSERT INTO EG_DESIGNATION (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL,OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO,ANN_INCREMENT, REPORTSTO, GRADE_ID) 
VALUES (SEQ_DESIGNATION.nextVal, NULL, 'LCO', NULL, NULL, 'LEGAL CELL OFFICER', 1, 1, NULL, NULL, NULL, NULL,(SELECT GRADE_ID FROM EGEIS_GRADE_MSTR WHERE GRADE_VALUE='B')); 

-- sample data for position
INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID,EFFECTIVE_DATE) 
VALUES ('LCO1', SEQ_POS.NEXTVAL, 1, 1, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='ASSISTANT'), SYSDATE); 
INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID,EFFECTIVE_DATE) 
VALUES ('LCO2', SEQ_POS.NEXTVAL, 1, 1, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='LCO'), SYSDATE); 
INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID,EFFECTIVE_DATE) 
VALUES ('LCO3', SEQ_POS.NEXTVAL, 1, 1, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='LCO'), SYSDATE); 

-- sample data for Employee
INSERT INTO EG_EMPLOYEE (ID, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID,GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1,
LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, EMPLOYMENT_STATUS, CATEGORY_ID,QULIFIED_ID, SALARY_BANK, PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY,
SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT,IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE,EMPFATHER_FIRSTNAME, 
EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME,EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE, CODE, BANK, CREATEDTIME,CREATED_BY, STATUS, DEATH_DATE, 
LASTMODIFIED_DATE, DEPUTATION_DATE, GOVT_ORDER_NO, RETIREMENT_DATE,PAYMENT_TYPE,EMPCATMSTR_ID)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, SYSDATE, NULL, NULL, NULL, NULL, '1', '0', '0', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 
NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT ID_USER FROM EG_USER WHERE USER_NAME='coclcms'), 1, NULL, NULL, NULL, 'E4', NULL, NULL, ' ', NULL, 'E4', NULL, 30001, NULL,  SYSDATE, 
1, 1, NULL,  SYSDATE, NULL, NULL, NULL, NULL,3); 
INSERT INTO EG_EMPLOYEE (ID, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID,GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1,
LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, EMPLOYMENT_STATUS, CATEGORY_ID,QULIFIED_ID, SALARY_BANK, PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY,
SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT,IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE,EMPFATHER_FIRSTNAME, 
EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME,EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE, CODE, BANK, CREATEDTIME,CREATED_BY, STATUS, DEATH_DATE, 
LASTMODIFIED_DATE, DEPUTATION_DATE, GOVT_ORDER_NO, RETIREMENT_DATE,PAYMENT_TYPE,EMPCATMSTR_ID)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, SYSDATE, NULL, NULL, NULL, NULL, '1', '0', '0', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 
NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT ID_USER FROM EG_USER WHERE USER_NAME='coclcms1'), 1, NULL, NULL, NULL, 'E5', NULL, NULL, ' ', NULL, 'E5', NULL, 30002, NULL,  SYSDATE, 
1, 1, NULL,  SYSDATE, NULL, NULL, NULL, NULL,3); 
INSERT INTO EG_EMPLOYEE (ID, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID,GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1,
LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, EMPLOYMENT_STATUS, CATEGORY_ID,QULIFIED_ID, SALARY_BANK, PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY,
SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT,IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE,EMPFATHER_FIRSTNAME, 
EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME,EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE, CODE, BANK, CREATEDTIME,CREATED_BY, STATUS, DEATH_DATE, 
LASTMODIFIED_DATE, DEPUTATION_DATE, GOVT_ORDER_NO, RETIREMENT_DATE,PAYMENT_TYPE,EMPCATMSTR_ID)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, SYSDATE, NULL, NULL, NULL, NULL, '1', '0', '0', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 
NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, 'E6', NULL, NULL, ' ', NULL, 'E6', NULL, 30003, NULL,  SYSDATE, 
1, 1, NULL,  SYSDATE, NULL, NULL, NULL, NULL,3); 

-- sample data for Employee Assignment Period
INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE,ID_EMPLOYEE)
VALUES (seq_ass_prd.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 
TO_DATE( '05/05/2035 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), (select id from eg_employee where emp_firstname='E4'));
INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE,ID_EMPLOYEE)
VALUES (seq_ass_prd.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 
TO_DATE( '05/05/2035 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), (select id from eg_employee where emp_firstname='E5'));
INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE,ID_EMPLOYEE)
VALUES (seq_ass_prd.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 
TO_DATE( '05/05/2035 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), (select id from eg_employee where emp_firstname='E6'));

-- sample data for Employee Assignment
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY,PCT_ALLOCATION, REPORTS_TO, 
ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID, GOVT_ORDER_NO,GRADE_ID)
VALUES (seq_ass.nextVal, (SELECT ID FROM FUND WHERE NAME='Salary Reserve Fund'), (SELECT ID FROM FUNCTION WHERE CODE='8100'), 
(select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT'), (SELECT ID FROM FUNCTIONARY WHERE NAME='A.E.-III (ELECTRICAL) D.P.STN.'), '100', NULL, 
(select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='E4'))), NULL, 
(select id_dept from eg_department where dept_name='Y-Legal Cell'), (select id from eg_position where position_name ='LCO1'), 
NULL, (SELECT GRADE_ID FROM EGEIS_GRADE_MSTR WHERE GRADE_VALUE='B')); 
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY,PCT_ALLOCATION, REPORTS_TO, 
ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID, GOVT_ORDER_NO,GRADE_ID)
VALUES (seq_ass.nextVal, (SELECT ID FROM FUND WHERE NAME='Salary Reserve Fund'), (SELECT ID FROM FUNCTION WHERE CODE='8100'), 
(select  DESIGNATIONID from eg_designation where designation_name='LCO'),(SELECT ID FROM FUNCTIONARY WHERE NAME='DIVISION II F4'), '100', NULL, 
(select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='E5'))), NULL, 
(select id_dept from eg_department where dept_name='L-Electrical'), (select id from eg_position where position_name ='LCO2'), 
NULL, (SELECT GRADE_ID FROM EGEIS_GRADE_MSTR WHERE GRADE_VALUE='B')); 
INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY,PCT_ALLOCATION, REPORTS_TO, 
ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID, GOVT_ORDER_NO,GRADE_ID)
VALUES (seq_ass.nextVal, NULL, NULL, 
(select  DESIGNATIONID from eg_designation where designation_name='LCO'), NULL, '100', NULL, 
(select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='E6'))), NULL, 
(select id_dept from eg_department where dept_name='L-Electrical'), (select id from eg_position where position_name ='LCO3'), 
NULL, (SELECT GRADE_ID FROM EGEIS_GRADE_MSTR WHERE GRADE_VALUE='B'));




#DOWN
