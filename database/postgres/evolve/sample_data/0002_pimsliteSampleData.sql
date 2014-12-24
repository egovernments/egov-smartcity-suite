#UP

INSERT INTO EGEIS_GRADE_MSTR ( GRADE_ID, GRADE_VALUE, START_DATE, END_DATE,AGE ) VALUES ( 
EGPIMS_GRADE_MSTR_SEQ.nextval, 'A', TO_DATE('01-04-2000', 'DD-MM-YYYY'), TO_DATE('01-04-2099','DD-MM-YYYY'), 60); 
INSERT INTO EGEIS_GRADE_MSTR ( GRADE_ID, GRADE_VALUE, START_DATE, END_DATE,AGE ) VALUES ( 
EGPIMS_GRADE_MSTR_SEQ.nextval, 'B',  TO_DATE('01-04-2000', 'DD-MM-YYYY'), TO_DATE('01-04-2099','DD-MM-YYYY'), 60); 
INSERT INTO EGEIS_GRADE_MSTR ( GRADE_ID, GRADE_VALUE, START_DATE, END_DATE,AGE ) VALUES ( 
EGPIMS_GRADE_MSTR_SEQ.nextval, 'C', TO_DATE('01-04-2000', 'DD-MM-YYYY'), TO_DATE('01-04-2099','DD-MM-YYYY'), 60); 
INSERT INTO EGEIS_GRADE_MSTR ( GRADE_ID, GRADE_VALUE, START_DATE, END_DATE,AGE ) VALUES ( 
EGPIMS_GRADE_MSTR_SEQ.nextval, 'D',  TO_DATE('01-04-2000', 'DD-MM-YYYY'), TO_DATE('01-04-2099','DD-MM-YYYY'), 60); 


insert into eg_designation (DESIGNATIONID,DESIGNATION_NAME, SANCTIONED_POSTS, OUTSOURCED_POSTS,GRADE_ID, designation_description)
values (seq_designation.nextval,'MEDICAL OFFICER',5,0,(select GRADE_ID from egeis_grade_mstr where grade_value='A'),'MEDICAL OFFICER');

insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'MEDICAL OFFICER_1',
(select designationid from eg_designation where designation_name = 'MEDICAL OFFICER'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'MEDICAL OFFICER_2',
(select designationid from eg_designation where designation_name = 'MEDICAL OFFICER'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'MEDICAL OFFICER_3',
(select designationid from eg_designation where designation_name = 'MEDICAL OFFICER'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'MEDICAL OFFICER_4',
(select designationid from eg_designation where designation_name = 'MEDICAL OFFICER'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'MEDICAL OFFICER_5',
(select designationid from eg_designation where designation_name = 'MEDICAL OFFICER'),sysdate,1,0);

insert into eg_designation (DESIGNATIONID,DESIGNATION_NAME, SANCTIONED_POSTS, OUTSOURCED_POSTS,GRADE_ID, designation_description)
values (seq_designation.nextval,'Account Clerk',5,0,(select GRADE_ID from egeis_grade_mstr where grade_value='A'),'Account Clerk');
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'Account Clerk_1',
(select designationid from eg_designation where designation_name = 'Account Clerk'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'Account Clerk_2',
(select designationid from eg_designation where designation_name = 'Account Clerk'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'Account Clerk_3',
(select designationid from eg_designation where designation_name = 'Account Clerk'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'Account Clerk_4',
(select designationid from eg_designation where designation_name = 'Account Clerk'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'Account Clerk_5',
(select designationid from eg_designation where designation_name = 'Account Clerk'),sysdate,1,0);

insert into eg_designation (DESIGNATIONID,DESIGNATION_NAME, SANCTIONED_POSTS, OUTSOURCED_POSTS,GRADE_ID,designation_description)
values (seq_designation.nextval,'PHARMACIST',5,0,(select GRADE_ID from egeis_grade_mstr where grade_value='B'),'PHARMACIST');
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'PHARMACIST_1',
(select designationid from eg_designation where designation_name = 'PHARMACIST'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'PHARMACIST_2',
(select designationid from eg_designation where designation_name = 'PHARMACIST'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'PHARMACIST_3',
(select designationid from eg_designation where designation_name = 'PHARMACIST'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'PHARMACIST_4',
(select designationid from eg_designation where designation_name = 'PHARMACIST'),sysdate,1,0);
insert into eg_position (ID,POSITION_NAME,DESIG_ID,EFFECTIVE_DATE,SANCTIONED_POSTS,OUTSOURCED_POSTS)
values (seq_pos.nextval,'PHARMACIST_5',
(select designationid from eg_designation where designation_name = 'PHARMACIST'),sysdate,1,0);


INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,100, 'VIMAL KISHORE', 'VIMAL KISHORE', TO_DATE('19-11-2003','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1964','dd-mm-yyyy'), 'NATTHU',sysdate,3
);
INSERT INTO accountdetailkey
(ID, groupid, detailtypeid,
 detailname, detailkey
)
 VALUES (seq_accountdetailkey.NEXTVAL, 1, (select id from accountdetailtype where UPPER (NAME) = UPPER ('Employee')),
	 'Employee_id', (select id from eg_employee where code=100));

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),(select id from eg_employee where code=100));
insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='MEDICAL OFFICER'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=100) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='A'),
(select id from eg_position where position_name='MEDICAL OFFICER_1'));

INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,101, 'LAXMI NARAYAN', 'LAXMI NARAYAN', TO_DATE('19-11-2005','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1966','dd-mm-yyyy'), 'KALLO',sysdate,3
);
INSERT INTO accountdetailkey
(ID, groupid, detailtypeid,
 detailname, detailkey
)
 VALUES (seq_accountdetailkey.NEXTVAL, 1, (select id from accountdetailtype where UPPER (NAME) = UPPER ('Employee')),
	 'Employee_id', (select id from eg_employee where code=101));

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),(select id from eg_employee where code=101));
insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='MEDICAL OFFICER'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=101) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='A'),
(select id from eg_position where position_name='MEDICAL OFFICER_2'));

INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,102, 'CHHIDDU PRASAD', 'CHHIDDU PRASAD', TO_DATE('19-11-2003','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1964','dd-mm-yyyy'), 'PANCHA',sysdate,3
);
INSERT INTO accountdetailkey
(ID, groupid, detailtypeid,
 detailname, detailkey
)
 VALUES (seq_accountdetailkey.NEXTVAL, 1, (select id from accountdetailtype where UPPER (NAME) = UPPER ('Employee')),
	 'Employee_id', (select id from eg_employee where code=102));

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),(select id from eg_employee where code=102));
insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='PHARMACIST'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=102) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='B'),
(select id from eg_position where position_name='PHARMACIST_1'));

INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,103, 'RAM PRASAD', 'RAM PRASAD', TO_DATE('19-11-2003','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1964','dd-mm-yyyy'), 'PRASAD',sysdate,3
);
INSERT INTO accountdetailkey
(ID, groupid, detailtypeid,
 detailname, detailkey
)
 VALUES (seq_accountdetailkey.NEXTVAL, 1, (select id from accountdetailtype where UPPER (NAME) = UPPER ('Employee')),
	 'Employee_id', (select id from eg_employee where code=103));

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),(select id from eg_employee where code=103));
insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='PHARMACIST'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=103) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='B'),
(select id from eg_position where position_name='PHARMACIST_2'));

UPDATE EG_EMPLOYEE SET ISACTIVE=1;
UPDATE EG_EMPLOYEE E SET E.STATUS=(SELECT ID FROM EGW_STATUS WHERE MODULETYPE = 'Employee' AND DESCRIPTION LIKE 'Employed') WHERE E.ISACTIVE=1;
UPDATE EG_EMPLOYEE E SET E.STATUS=(SELECT ID FROM EGW_STATUS WHERE MODULETYPE = 'Employee' AND DESCRIPTION LIKE 'Suspended') WHERE E.ISACTIVE=0;

UPDATE EG_EMPLOYEE e SET e.ID_USER=(SELECT u.ID_USER FROM EG_USER u WHERE u.USER_NAME LIKE 'egovernments') WHERE e.CODE=100;
#DOWN
