INSERT INTO eg_designation (id, name, description, chartofaccounts, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
 VALUES (nextval('SEQ_EG_DESIGNATION'), 'Examiner of Accounts', 'Examiner of Accounts', NULL, 0, current_date,  current_date, 1, 1);

insert into egeis_deptdesig (id,designation,department,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(select id from eg_designation where upper(name)=upper('Examiner of Accounts')),(select id from eg_department where upper(name)=upper('Accounts')),1,1,current_date,current_date,1,1);

insert into eg_position(name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version)
values('A_Examiner of Accounts_1',nextval('seq_eg_position'),(select id from egeis_deptdesig where designation=(select id from eg_designation where upper(name)=upper('Examiner of Accounts')) ),current_date,current_date,1,1,false,0);

insert into eg_user(id,locale,username,password,pwdexpirydate,mobilenumber,createddate,lastmodifieddate,createdby,
lastmodifiedby,active,name,gender,type,version)
values(nextval('seq_eg_user'),'en_IN','EOA',
'$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK','01-Jan-2099','9889007656',
current_date,current_date,1,1,true,'Babu Mahendra',1,'EMPLOYEE',0);

insert into egeis_employee(id,code,employeestatus,employeetype,version) 
values((select id from eg_user where username='EOA'),'109090','EMPLOYED',(select id from egeis_employeetype where name='Permanent'),0);

insert into egeis_assignment (id,designation,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,version,isprimary,employee)
values(nextval('seq_egeis_assignment'),(select id from eg_designation where upper(name)=upper('Examiner of Accounts')),
(select id from eg_department where upper(name)=upper('Accounts')),(select id from eg_position where name='A_Examiner of Accounts_1'),(select grade_id from egeis_grade_mstr where grade_value='A'),
1,current_date,current_date,1,'01-Apr-2015','31-Mar-2017',0,false,(select id from egeis_employee where upper(code)=upper('109090')));

insert into eg_userrole(roleid,userid) values((select id from eg_role where upper(name)='EMPLOYEE'),(select id from eg_user where username='EOA'));
