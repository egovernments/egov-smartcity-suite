#UP
-- sample data for law officer

INSERT INTO EG_DESIGNATION
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO,GRADE_ID)
 VALUES
   (SEQ_DESIGNATION.NEXTVAL, (select id_dept from eg_department where dept_name ='Y-Legal Cell'), 'LAW OFFICER', NULL, 
    1, 'LAW OFFICER.', 1, 1, 4500, 
    9000, NULL, 2,2);
  
INSERT INTO EG_POSITION
   (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES
   ('LAWOFFICER_1', seq_pos.NEXTVAL, 10, 7, 
    (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='LAW OFFICER' ), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

---user data
INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'LAWOFFICER', 'lawofficer', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

--employee data
INSERT INTO EG_EMPLOYEE
   (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '23030', TO_DATE('11/11/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL,  NULL, 
    NULL, NULL, NULL,  NULL, 
    NULL, NULL, NULL, NULL, NULL, 
     (select id_dept from eg_department where dept_name ='Y-Legal Cell'), (SELECT id_user FROM EG_USER WHERE user_name ='lawofficer'), 1, NULL, NULL, 
    NULL, 'LAWOFFICER', NULL, NULL, NULL, 
    NULL, 'LAWOFFICER', NULL,3);

INSERT INTO EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES
   (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='LAWOFFICER'));

INSERT INTO EG_EMP_ASSIGNMENT
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES
   (seq_ass.NEXTVAL, 12, 1, (SELECT designationid FROM EG_DESIGNATION WHERE designation_name='LAW OFFICER' ), 
    (select id from functionary where name='LEGAL CELL'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='LAW COMMISSIONER'),(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='LAWOFFICER')), NULL, 
    (select id_dept from eg_department where dept_name ='Y-Legal Cell'), (SELECT id FROM EG_POSITION WHERE position_name='LAWOFFICER_1'),NULL,NULL);

Insert into eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 Values
   (SEQ_EG_USER_JURLEVEL.nextVal, (select id_user from eg_user where first_name='LAWOFFICER'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

 Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_user_jurlevel from eg_user_jurlevel where id_user=(select id_user from eg_user where first_name='LAWOFFICER')), 1, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
 
INSERT INTO EG_ROLES (ID_ROLE, ROLE_NAME, ROLE_DESC, ROLE_NAME_LOCAL, ROLE_DESC_LOCAL, UPDATETIME,UPDATEUSERID) 
VALUES (SEQ_EG_ROLES.nextval, 'LAW OFFICER', 'LAW OFFICER', NULL, NULL,  SYSDATE, NULL); 

INSERT INTO EG_USERROLE (ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY) 
VALUES ((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'), (select ID_USER from eg_user where USER_NAME ='lawofficer'),
SEQ_EG_USERROLE.nextval,  TO_DATE( '05/05/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), NULL, 'N'); 

insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/governmentDept!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/interimOrder!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/judgmentType!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!edit.action') and a.queryparams = 'type=Search'));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/advocateMaster!search.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcase!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/casetypeMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/courttypeMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/masters/courtMaster!newform.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/dueDateReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/employeeReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/judgmentReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/pwrReport!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/employeeReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/judgmentReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/pwrReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!search.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!aggregatedReports.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!flatReport.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/legalcaseGenericReports!edit.action') and a.queryparams = 'type=Report'));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/eGov_LCMS.jsp')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.name IN ('DEFAULT')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcase!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/hearings!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/hearings!edit.action')));
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/lcinterimorder!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/lcinterimorder!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/vacateStay!edit.action'))); 
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgment!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgment!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgmentImplement!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/judgmentImplement!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcaseDisposal!newform.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/transactions/legalcaseDisposal!edit.action')));          
insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LAW OFFICER'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/timeSeriesReport!edit.action')));          
INSERT INTO eg_roleaction_map  VALUES ((SELECT id_role FROM eg_roles WHERE role_name = 'LAW OFFICER'),(SELECT a.ID FROM eg_action a  WHERE a.url IN ('/workflow/inbox.action') and context_root='egi'));
INSERT INTO eg_roleaction_map VALUES ((SELECT id_role  FROM eg_roles WHERE role_name = 'LAW OFFICER'), (SELECT a.ID   FROM eg_action a  WHERE a.url IN ('/eGov.jsp') and context_root='egi'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select ID_ROLE from eg_roles where ROLE_NAME like 'LAW OFFICER'),(select ID from EG_ACTION where NAME like 'View Past Payment')); 

#DOWN
