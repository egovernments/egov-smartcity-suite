insert into egeis_deptdesig(id,designation,department,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('SEQ_EIS_DEPTDESIG'),
(select id from eg_designation where UPPER(name)='ASSISTANT ENGINEER'),
(select id from eg_department where code='L'),current_date,current_date,1,1,0);

insert into egeis_deptdesig(id,designation,department,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('SEQ_EIS_DEPTDESIG'),
(select id from eg_designation where UPPER(name)='ADDITIONAL HEALTH OFFICER'),
(select id from eg_department where code='H'),current_date,current_date,1,1,0);

insert into egeis_deptdesig(id,designation,department,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('SEQ_EIS_DEPTDESIG'),
(select id from eg_designation where UPPER(name)='HEALTH OFFICER'),
(select id from eg_department where code='H'),current_date,current_date,1,1,0);

insert into egeis_deptdesig(id,designation,department,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values (nextval('SEQ_EIS_DEPTDESIG'),
(select id from eg_designation where UPPER(name)='COMMISSIONER'),
(select id from eg_department where code='G'),current_date,current_date,1,1,0);


insert into eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) 
values(nextval('seq_eg_position'),'L_ASSISTANT ENGINEER_1',
(select id from egeis_deptdesig where department=((select id from eg_department where code='L'))
 and designation=((select id from eg_designation where UPPER(name)='ASSISTANT ENGINEER'))),current_date,current_date,1,1,'FALSE',0);

 insert into eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) 
values(nextval('seq_eg_position'),'H_ADDITIONAL HEALTH OFFICER_1',
(select id from egeis_deptdesig where department=((select id from eg_department where code='H'))
 and designation=((select id from eg_designation where UPPER(name)='ADDITIONAL HEALTH OFFICER'))),current_date,current_date,1,1,'FALSE',0);

 
 insert into eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) 
values(nextval('seq_eg_position'),'H_HEALTH OFFICER_1',
(select id from egeis_deptdesig where department=((select id from eg_department where code='H'))
 and designation=((select id from eg_designation where UPPER(name)='HEALTH OFFICER'))),current_date,current_date,1,1,'FALSE',0);

 
 insert into eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) 
values(nextval('seq_eg_position'),'G_COMMISSIONER_1',
(select id from egeis_deptdesig where department=((select id from eg_department where code='G'))
 and designation=((select id from eg_designation where UPPER(name)='COMMISSIONER'))),current_date,current_date,1,1,'FALSE',0);



update eg_emp_assignment set modifiedby=1,modifieddate=current_date,createddate=current_date,createdby=1,id_employee=(select id from eg_employee where upper(code)='E006'),
from_date='01-Apr-2015',to_date='31-Mar-2020' where id_emp_assign_prd=1;


 insert into eg_emp_assignment(id,id_fund,id_function,designationid,id_functionary,main_dept,position_id,grade_id,is_primary,modifiedby,modifieddate,createddate,createdby,id_employee,from_date,to_date)
values(nextval('SEQ_ASS'),(select id from fund where code='01'),(select id from function where code='81'),
(select id from eg_designation where upper(name)='ASSISTANT ENGINEER'),
(select id from functionary where name='ADMIN'),
(select id from eg_department where code='L'),
(select id from eg_position where upper(name)='L_ASSISTANT ENGINEER_1'),
(select grade_id from EGEIS_GRADE_MSTR where GRADE_VALUE='A'),'Y',1,current_date,current_date,1,
(select id from eg_employee where upper(code)='E007'),'01-Apr-2015','31-Mar-2020');

insert into eg_emp_assignment(id,id_fund,id_function,designationid,id_functionary,main_dept,position_id,grade_id,is_primary,modifiedby,modifieddate,createddate,createdby,id_employee,from_date,to_date)
values(nextval('SEQ_ASS'),(select id from fund where code='01'),(select id from function where code='81'),
(select id from eg_designation where upper(name)='ADDITIONAL HEALTH OFFICER'),
(select id from functionary where name='ADMIN'),
(select id from eg_department where code='H'),
(select id from eg_position where upper(name)='H_ADDITIONAL HEALTH OFFICER_1'),
(select grade_id from EGEIS_GRADE_MSTR where GRADE_VALUE='A'),'Y',1,current_date,current_date,1,
(select id from eg_employee where upper(code)='E048'),'01-Apr-2015','31-Mar-2020');


update eg_emp_assignment set modifiedby=1,modifieddate=current_date,createddate=current_date,createdby=1,id_employee=(select id from eg_employee where upper(code)='E006'),
from_date='01-Apr-2015',to_date='31-Mar-2020' where id_emp_assign_prd=1;
insert into eg_emp_assignment(id,id_fund,id_function,designationid,id_functionary,main_dept,position_id,grade_id,is_primary,modifiedby,modifieddate,createddate,createdby,id_employee,from_date,to_date)
values(nextval('SEQ_ASS'),(select id from fund where code='01'),(select id from function where code='81'),
(select id from eg_designation where upper(name)='COMMISSIONER'),
(select id from functionary where name='ADMIN'),
(select id from eg_department where code='G'),
(select id from eg_position where upper(name)='G_COMMISSIONER_1'),
(select grade_id from EGEIS_GRADE_MSTR where GRADE_VALUE='A'),'Y',1,current_date,current_date,1,
(select id from eg_employee where upper(code)='E058'),'01-Apr-2015','31-Mar-2020');


insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Grievance Officer'),
(select id from eg_user where username='krishna'));



insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Redressal Officer'),
(select id from eg_user where username='julian'));


update eg_user set username='surya' where username='surya ';

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Redressal Officer'),
(select id from eg_user where username='surya'));

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Redressal Officer'),
(select id from eg_user where username='prabhu'));


insert into pgr_router(id,position,bndryid,version,createdby,createddate,lastmodifieddate,lastmodifiedby) 
values(nextval('SEQ_pgr_router'),(select id from eg_position where upper(name)='L_ASSISTANT ENGINEER_1'),
(select id from eg_boundary where name='Corporation of Chennai' and boundarytype=(select id from eg_boundary_type where name='City' and hierarchytype=1)),0,1,current_date,current_date,1);


insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Grievance Operator'),
(select id from eg_user where username='surya'));

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Grievance Operator'),
(select id from eg_user where username='julian'));

update eg_boundary set localname=name where localname is null;


