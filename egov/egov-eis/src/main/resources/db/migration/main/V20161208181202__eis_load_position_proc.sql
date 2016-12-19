------------------------load data to tmp_position-------------------------------------------------------
------------------------------Position Master ----------------------------------------------------------
----------------------------------***Temporary Table***-------------------------------------------------

create table tmp_position(
	srlno numeric primary key,
	department varchar(50),
	designation varchar(50),
	noofpost numeric,
	status varchar(50)
);

---------------------------------------------------------------------------------------------------------

create or replace function department_exist(deptName in character varying) returns boolean as 
$BODY$
declare isexist boolean default true;
begin
PERFORM id from eg_department dept where upper(dept.name)=upper(deptName);
IF NOT FOUND THEN
RAISE NOTICE 'Inside Department exit checking- DOES NOT EXIST %',deptName;
isexist:=false;
END IF;
return isexist;
end;
$BODY$
LANGUAGE plpgsql;

create or replace function designation_exist(desgName in character varying) returns boolean as 
$BODY$
declare isexist boolean default true;
begin
PERFORM id from eg_designation des where upper(des.name)=upper(desgName);
IF NOT FOUND THEN
RAISE NOTICE 'Inside Designation exit checking- DOES NOT EXIST %',desgName;
isexist:=false;
END IF;
return isexist;
end;
$BODY$
LANGUAGE plpgsql;


create or replace function deptdesig_exist(desgName in character varying,deptName in character varying) returns boolean as 
$BODY$
declare isexist boolean default true;
begin
PERFORM id from egeis_deptdesig des where designation in(select id from eg_designation where upper(name)=upper(desgName))
and department in(select id from eg_department where upper(name)=upper(deptName));
IF NOT FOUND THEN
RAISE NOTICE 'Inside DEPT-Designation exit checking- DOES NOT EXIST %',desgName;
isexist:=false;
END IF;
return isexist;
end;
$BODY$
LANGUAGE plpgsql;


------------------------------------procedure to load position data-------------------------------------

create or replace FUNCTION load_position() RETURNS void AS
$BODY$
declare
isDepartmentExist boolean default false;
isDesignationExist boolean default false;
isDeptDesigExist boolean default false;
srl bigint;
dept_name varchar(100);
desig_name varchar(100);
noofposts bigint;
deptdesig_id bigint;
max_position bigint;
deptid bigint;
dept_code varchar(50);
pos_seq bigint;
cur_emp tmp_position%ROWTYPE;
desig_id bigint;
diff bigint;
begin
RAISE NOTICE 'INSIDE Position data load';
for cur_emp in( select * from tmp_position where noofpost>0 and (status<>'Updated' or status is null))
loop
begin
srl:=cur_emp.srlno;
dept_name:=cur_emp.department;
desig_name:=cur_emp.designation;
noofposts:=cur_emp.noofpost;
isDepartmentExist:= department_exist(dept_name);
isDesignationExist:=designation_exist(desig_name);

if(isDepartmentExist=false) then
RAISE NOTICE 'Department doesnot exists for %',dept_name;
update tmp_position set status='Department does not exist' where srlno=srl ;
end if;

if(isDesignationExist=false) then
RAISE NOTICE 'Designation doesnot exists for %',desig_name;
update tmp_position set status='Designation does not exist' where srlno=srl ;
end if;

deptid:= (select id from eg_department where upper(name)=upper(dept_name));
desig_id:= (select id from eg_designation where upper(name) = upper(desig_name));

if(isDepartmentExist=true and isDesignationExist=true ) then
RAISE NOTICE 'Ready for Position Data Load';
isDeptDesigExist:=deptdesig_exist(desig_name,dept_name);
if(isDeptDesigExist=false) then
insert into egeis_deptdesig (id,designation,department,outsourcedposts,sanctionedposts,version,createddate,
lastmodifieddate,createdby,lastmodifiedby)values(nextval('seq_egeis_deptdesig'),(select id from eg_designation 
where upper(name)=upper(desig_name)),(select id from eg_department where upper(name)=upper(dept_name)),0,
1,0,current_date,current_date,1,1);
RAISE NOTICE 'Dept-desig inserted for %',desig_name;
END IF;
select id into deptdesig_id from egeis_deptdesig  des where designation in(select id from eg_designation where
upper(name)=upper(desig_name))and department in(select id from eg_department where
upper(name)=upper(dept_name));
select count(id) into max_position from eg_position where deptdesig=deptdesig_id;
if(noofposts < max_position) then
RAISE NOTICE 'Positions exceed';
update tmp_position set status='No of positions exceeded the limit' where srlno=srl ;
END IF; 
if(noofposts = max_position) then
update tmp_position set status='Updated' where srlno=srl ;
end if;
if(noofposts > max_position)then
diff:=noofposts-max_position;
max_position:=max_position+1;
WHILE diff > 0
LOOP 
select code into dept_code from eg_department where upper(name)=upper(dept_name);
select nextval('seq_eg_position') into pos_seq;
insert into eg_position(name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,
ispostoutsourced,version)values(dept_code||'_'||upper(desig_name)||'_'||max_position,pos_seq,deptdesig_id,
current_date,current_date,1,1,false,0);
diff:=diff-1;
max_position:=max_position+1;
END LOOP;
update tmp_position set status='Updated' where srlno=srl ;
END IF;
END IF;
end;
end loop;
end;
$BODY$
LANGUAGE plpgsql;