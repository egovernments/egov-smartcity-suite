insert into egeis_deptdesig(id,desig_id,dept_id) values (nextval('SEQ_EIS_DEPTDESIG'),
(select designationid from eg_designation where UPPER(designation_name)='JUNIOR ENGINEER'),
(select id_dept from eg_department where dept_code='L'));


insert into eg_position(id,position_name,id_deptdesig,effective_date) values(nextval('seq_pos'),'L_JUNIOR ENGINEER_1',
(select id from egeis_deptdesig where dept_id=((select id_dept from eg_department where dept_code='L'))
 and desig_id=((select designationid from eg_designation where UPPER(designation_name)='JUNIOR ENGINEER'))),current_date);

insert into eg_emp_assignment_prd(id,from_date,to_date,id_employee) values(nextval('SEQ_ASS_PRD'),'01-Apr-2015','31-Mar-2020',(select id from eg_employee where upper(code)='E006'));

insert into eg_emp_assignment(id,id_fund,id_function,designationid,id_functionary,id_emp_assign_prd,main_dept,position_id,grade_id,is_primary)
values(nextval('SEQ_ASS'),(select id from fund where code='01'),(select id from function where code='202402'),(select designationid from eg_designation where upper(designation_name)='JUNIOR ENGINEER'),
(select id from functionary where name='ADMIN'),(select id from eg_emp_assignment_prd where id_employee in(select id from eg_employee where upper(code)='E006')),
(select id_dept from eg_department where dept_code='L'),(select id from eg_position where upper(position_name)='L_JUNIOR ENGINEER_1'),(select grade_id from EGEIS_GRADE_MSTR where GRADE_VALUE='A'),'Y');
