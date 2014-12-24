#UP
INSERT INTO eg_employee
(ID, code,
 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date,ISACTIVE,EMPCATMSTR_ID
)
VALUES (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL,30005, 'ANUSHKA', 'ANUSHKA', TO_DATE('19-11-2003','dd-mm-yyyy'), 'M', 
TO_DATE('01-10-1964','dd-mm-yyyy'), 'PQR',sysdate,1,3
);


INSERT INTO accountdetailkey
(ID, groupid, detailtypeid,
 detailname, detailkey
)
 VALUES (seq_accountdetailkey.NEXTVAL, 1, (select id from accountdetailtype where UPPER (NAME) = UPPER ('Employee')),
	 'Employee_id', (select id from eg_employee where code=30005));


insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Jun-2008'),to_date('01-Mar-2009'),(select id from eg_employee where code=30005));


insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID,IS_PRIMARY)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='Zonal Officer'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=30005) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='A'),
(select id from eg_position where position_name='ZONAL OFFICER_02'),'N');

insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
values (SEQ_ASS_PRD.nextval,to_date('01-Jun-2008'),to_date('01-Mar-2009'),(select id from eg_employee where code=30005));


insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID,POSITION_ID,IS_PRIMARY)
values (SEQ_ASS.nextval,(select id from fund where name='Municipal General Fund'),
(select id from function where name = 'Public Health' and isnotleaf=0),
(select id from functionary where name='MEDICAL  ESTABLISHMENT'),
(select designationid from eg_designation where designation_name='PHARMACIST'),
(select id from eg_emp_assignment_prd where id_employee = (select id from eg_employee where code=30005) and id not in(select id_emp_assign_prd from eg_emp_assignment) ),
(select id_dept from eg_department where dept_name='H-Health'),(select GRADE_ID from egeis_grade_mstr where grade_value='B'),
(select id from eg_position where position_name='PHARMACIST_1'),'Y');
#DOWN
