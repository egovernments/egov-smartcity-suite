#UP

INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,ID_DEPARTMENT, LOCALE, USER_NAME, PWD,
PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO,
ISACTIVE, FROMDATE,TODATE ) VALUES (
SEQ_EG_USER.nextval, NULL, 'MR.', 'citizenUser', NULL, NULL, TO_Date( '08/15/1947 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1, NULL, 'citizenUser', '51r3iclnt0aj8', '804AEO', sysdate, (select ID_USER from eg_user where USER_NAME like 'egovernments'),
NULL, NULL, NULL, NULL, 'N', 1, NULL, 1, TO_Date( '04/01/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), NULL);

INSERT INTO EG_USERROLE ( ID_ROLE, ID_USER, ID, FROMDATE, TODATE,
IS_HISTORY ) VALUES (
(select ID_ROLE from EG_ROLES where ROLE_NAME like 'PROPERTY_TAX_USER'), (select ID_USER from eg_user where USER_NAME like 'citizenUser'), SEQ_EG_USERROLE.nextval, TO_Date( '01/01/2000 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), null, 'N');

INSERT INTO EG_USER_JURLEVEL ( ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE,
UPDATETIME ) VALUES (
SEQ_EG_USER_JURLEVEL.nextval,(select ID_USER from eg_user where USER_NAME like 'citizenUser'), (select id_bndry_type from EG_BOUNDARY_TYPE where name='City' and ID_HEIRARCHY_TYPE = 1), sysdate);

INSERT INTO EG_USER_JURVALUES ( ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE,
IS_HISTORY ) VALUES (
(select ID_USER_JURLEVEL from EG_USER_JURLEVEL where id_user=(select ID_USER from eg_user where USER_NAME like 'citizenUser')),( select id_bndry from eg_boundary where name='chennaicmc'), SEQ_EG_USER_JURVALUES.nextval, TO_Date( '12/24/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), NULL, 'N');


Insert into eg_designation
   (DESIGNATIONID, DEPTID, DESIGNATION_NAME, DESIGNATION_LOCAL, OFFICER_LEVEL, DESIGNATION_DESCRIPTION, SANCTIONED_POSTS, OUTSOURCED_POSTS, BASIC_FROM, BASIC_TO, ANN_INCREMENT, REPORTSTO, GRADE_ID)
 Values
   (SEQ_DESIGNATION.NEXTVAL, 1, 'citizenUser', NULL, NULL, 'citizenUser', 5, 0, NULL, NULL, NULL, NULL, (select GRADE_ID from egeis_grade_mstr where grade_value='A'));


Insert into EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 Values
   ('citizenUser', seq_pos.nextval, 1, 0, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'citizenUser'), TO_DATE('08/15/1947 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into EG_EMPLOYEE
   (ID, MOTHER_TONUGE, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, EMPLOYMENT_STATUS, ID_USER, EMP_FIRSTNAME, CODE, CREATED_BY, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, 'Tamil', 'M', 'N', 'N', 1, (select id_user from eg_user where user_name = 'citizenUser'), 'citizenUser', 880, (select id_user from eg_user where user_name = 'citizenUser'), sysdate,3);

Insert into EG_EMP_ASSIGNMENT_PRD
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (SEQ_ASS_PRD.nextval, TO_DATE('08/15/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where code=880));

Insert into EG_EMP_ASSIGNMENT
   (ID, DESIGNATIONID, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID,id_functionary)
 Values
   (SEQ_ASS.nextval, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'citizenUser'), 
   (SELECT ID FROM EG_EMP_ASSIGNMENT_PRD WHERE ID_EMPLOYEE = (SELECT ID FROM EG_EMPLOYEE WHERE CODE=880) ), 
   (SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='Revenue'), 
   (SELECT ID FROM EG_POSITION WHERE POSITION_NAME='citizenUser'),(SELECT ID FROM FUNCTIONARY WHERE CODE = 801));
   
   delete from eg_roleaction_map where actionid=(select id from eg_action where name = 'WorkflowPropertyCreate');
   delete from eg_action where name = 'WorkflowPropertyCreate';
   

#DOWN

