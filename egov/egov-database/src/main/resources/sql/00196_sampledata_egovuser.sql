

insert into eg_employee (id, date_of_birth,mother_tonuge,gender,emp_firstname,emp_lastname,name,code,lastmodified_date,createdtime) values(nextval('EGPIMS_PERSONAL_INFO_SEQ'), '1981-01-10 00:00:00', null, 'M','egovernments',null,'EGOVERNMENTS','E099',current_date,current_date);

insert into eg_user (id,locale,username,password,pwdexpirydate,active,name,type) 
values(nextval('seq_eg_user'),'en_IN','egovernments','$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK',
'31-Dec-2020','TRUE','egovernments',2);


update eg_employee set id_user=(select id from eg_user where username=lower('egovernments'))  where code='E099';

insert into eg_userrole (roleid,userid) values ((select id from eg_role where name='Super User'),(select id from eg_user where username='egovernments'));


insert into eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) 
values(nextval('seq_eg_position'),'L_ASSISTANT ENGINEER_2',
(select id from egeis_deptdesig where department=((select id from eg_department where code='L'))
 and designation=((select id from eg_designation where UPPER(name)='ASSISTANT ENGINEER'))),current_date,current_date,1,1,'FALSE',0);

 insert into egeis_assignment(id,fund,function,designation,functionary,department,position,grade,isprimary,lastmodifiedby,lastmodifieddate,createddate,createdby,employee,fromdate,todate)
values(nextval('SEQ_egeis_assignment'),(select id from fund where code='01'),(select id from function where code='81'),
(select id from eg_designation where upper(name)='ASSISTANT ENGINEER'),
(select id from functionary where name='ADMIN'),
(select id from eg_department where code='L'),
(select id from eg_position where upper(name)='L_ASSISTANT ENGINEER_2'),
(select grade_id from EGEIS_GRADE_MSTR where GRADE_VALUE='A'),'Y',1,current_date,current_date,1,
(select id from eg_employee where upper(code)='E099'),'01-Apr-2015','31-Mar-2020');
