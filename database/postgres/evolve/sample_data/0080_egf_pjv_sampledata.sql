#UP

update eg_appconfig_values set value='N' where key_id=(select id from eg_appconfig where key_name='pjv_saveasworkingcopy_enabled' and module='EGF');


update eg_designation set designation_name='SECTION MANAGER' where designation_name='Accounts Manager';
update eg_designation set designation_name='ACCOUNTS OFFICER' where designation_name='Accounts Officer';


update EG_EMP_ASSIGNMENT set main_dept=(select id_dept from eg_department where dept_code='A') where designationid=(select designationid from eg_designation where 
designation_name='SECTION MANAGER');

update EG_EMP_ASSIGNMENT set main_dept=(select id_dept from eg_department where dept_code='A') where designationid=(select designationid from eg_designation where 
designation_name='ACCOUNTS OFFICER');


-- position
INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE)
 VALUES('Assistant', seq_pos.NEXTVAL, 10, 7,(select designationid from eg_designation where designation_name='ASSISTANT'), TO_DATE('03/26/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

-- user
INSERT INTO EG_USER(ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES (seq_eg_user.NEXTVAL, 'assistant', 'assistant', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', TO_DATE('03/17/2008 11:59:39', 'MM/DD/YYYY HH24:MI:SS'), 'N', 1, TO_DATE('01/01/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2011 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_EMPLOYEE (ID, CODE, DATE_OF_BIRTH, BLOOD_GROUP, MOTHER_TONUGE, RELIGION_ID, COMMUNITY_ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, DATE_OF_FIRST_APPOINTMENT, IDENTIFICATION_MARKS1, LANGUAGES_KNOWN_ID, MODE_OF_RECRUIMENT_ID, RECRUITMENT_TYPE_ID, CATEGORY_ID, QULIFIED_ID, SALARY_BANK, BANK,  PAY_FIXED_IN_ID, GRADE_ID, PRESENT_DESIGNATION, SCALE_OF_PAY, BASIC_PAY, SPL_PAY, PP_SGPP_PAY, ANNUAL_INCREMENT_ID, GPF_AC_NUMBER, RETIREMENT_AGE, PRESENT_DEPARTMENT, IF_ON_DUTY_ARRANGMENT_DUTY_DEP, LOCATION, COST_CENTER, ID_DEPT, ID_USER, ISACTIVE, EMPFATHER_FIRSTNAME, EMPFATHER_LASTNAME, EMPFATHER_MIDDLENAME, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME, IDENTIFICATION_MARKS2, PAN_NUMBER, NAME, MATURITY_DATE,EMPCATMSTR_ID)
 VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, '10121', TO_DATE('11/11/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 
    NULL, NULL, '0', '0', '0', 
    TO_DATE('11/11/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 1, 1, 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, NULL, NULL,  NULL, 
    NULL, NULL, NULL,  '453423434', 
    NULL, NULL, NULL, NULL, NULL, 
    NULL, (SELECT id_user FROM EG_USER WHERE user_name ='assistant'), 1, NULL, NULL, 
    NULL, 'assistant', NULL, NULL, NULL, 
    NULL, 'assistant', NULL,3);

INSERT INTO EG_EMP_ASSIGNMENT_PRD (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 VALUES (seq_ass_prd.NEXTVAL, TO_DATE('01/01/1990 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, (SELECT id FROM EG_EMPLOYEE WHERE name='assistant'));


 INSERT INTO EG_EMP_ASSIGNMENT (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, PCT_ALLOCATION, REPORTS_TO, ID_EMP_ASSIGN_PRD, FIELD, MAIN_DEPT, POSITION_ID,GOVT_ORDER_NO, GRADE_ID)
 VALUES (seq_ass.NEXTVAL, 11, 1, (select designationid from eg_designation where designation_name='ASSISTANT'), 
    NULL, NULL, NULL,(SELECT id FROM EG_EMP_ASSIGNMENT_PRD WHERE id_employee =(SELECT id FROM EG_EMPLOYEE WHERE name='assistant')), NULL, 
    1, (SELECT id FROM EG_POSITION WHERE position_name='Assistant'),NULL,NULL);

 
 INSERT INTO EG_USER_JURLEVEL(ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES (SEQ_EG_USER_JURLEVEL.NEXTVAL, (SELECT id_user FROM EG_USER WHERE first_name='assistant'), 1, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO EG_USER_JURVALUES (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, TODATE, IS_HISTORY)
 VALUES ((SELECT id_user_jurlevel FROM EG_USER_JURLEVEL WHERE id_user=(SELECT id_user FROM EG_USER WHERE first_name='assistant')), 1, SEQ_EG_USER_JURVALUES.NEXTVAL, TO_DATE('08/11/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('08/11/2099 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

 
INSERT INTO EG_USERROLE (id_role, id_user, ID, fromdate, todate, is_history)
VALUES ((SELECT id_role FROM EG_ROLES WHERE ROLE_NAME='SuperUser'), (SELECT id_user FROM EG_USER WHERE user_name = 'assistant'), seq_eg_userrole.NEXTVAL,TO_DATE('01-01-2000','mm-dd-yyyy'), '', 'N' );
	
#DOWN
