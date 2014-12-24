CREATE OR REPLACE package emp_payroll as
	procedure knn_emp_assignment;
	function get_token(the_list  varchar2,the_index number,delim  varchar2 := ',') return varchar2;
	procedure knn_position_assignment;
	Procedure knn_emp_roll (saldate date);
	FUNCTION GENERATEFINYEAR (vdate date) return number;
	/* procedure empupdate; */
	procedure empupdate_masters ; 
end emp_payroll;
/

CREATE OR REPLACE package body EMP_PAYROLL as

FUNCTION GENERATEFINYEAR (vdate date) return number as

fyid number default 0;

begin
	begin
	select id into fyid from
	financialyear
	where startingdate<=vdate and endingdate>=vdate
	and isactiveforposting=1;
	exception
		WHEN NO_DATA_FOUND then
			dbms_output.put_line('Invalid Financial Year'||vdate);
			fyid:= -1;
	end;

return(fyid);
end GENERATEFINYEAR;

function get_token(
    the_list  varchar2,
    the_index number,
    delim     varchar2 := ','
 )
    return    varchar2
 is
    start_pos number;
    end_pos   number;
 begin
    if the_index = 1 then
        start_pos := 1;
    else
        start_pos := instr(the_list, delim, 1, the_index - 1);
        if start_pos = 0 then
            return null;
        else
            start_pos := start_pos + length(delim);
        end if;
    end if;
 
    end_pos := instr(the_list, delim, start_pos, 1);
 
    if end_pos = 0 then
        return substr(the_list, start_pos);
    else
        return substr(the_list, start_pos, end_pos - start_pos);
    end if;
 
 end get_token;



procedure knn_emp_assignment as


temp_funcode number default 0;
temp_depcode  varchar2(25);
funid  number default 0;
deptid  number default 0;
desgid  number default 0;
skip number default 0;
skip1 number default 0;
id_emp_assg_prd number default 0;
v_idpay number default 0;
v_incr number default 0;
vsucc number default 0;
vfail number default 0;
v_incdate date;
errstr varchar2(500);
vPnotA number default 0;
temp_ccode varchar2(100);
id_functionary number default 0;
vempid number default 0;
acdettype NUMBER;
mapped_deptname varchar2(100);
mapped_secname varchar2(100);
id_grade number default 0;

cursor empmaster is select distinct empid,empname,apptdate, sex,birthdate,FATHERNAME, designation,basic,scale,incrmonth,curr_dept,curr_section,incrdate,class from knn_payroll
where empid not in (select distinct code from eg_employee);

begin

  SELECT ID
      INTO acdettype
      FROM accountdetailtype
     WHERE UPPER (NAME) = UPPER ('Employee');

for emp in empmaster loop
	skip:=0;
	skip1:=0;
	errstr:='';
	dbms_output.put_line('Start processing for emp ->'|| emp.empid );
	BEGIN
	dbms_output.put_line('Mapping for DEPT ->'||emp.curr_dept || ' SECTION ->' || emp.curr_section );
	   SELECT deptname
	     INTO mapped_deptname
	     FROM TMP_DEPT_SEC_MAPPING
	    WHERE OLD_DEPTNAME like emp.curr_dept and OLD_SECTIONNAME like emp.curr_section;
	EXCEPTION
	   WHEN NO_DATA_FOUND
	   THEN
	      errstr :=
		      errstr || 'Dept mapping  not found:' || emp.curr_dept || '::' || emp.curr_section ;
	      SKIP := 1;
        END;
	if SKIP = 0 THEN
	
	
	begin
	select id_dept into deptid from eg_department where upper(dept_name)=upper(mapped_deptname);
	exception
	when no_data_found then
		dbms_output.put_line('Inserting DEPT ->'||mapped_deptname);
		insert into eg_department (id_dept,dept_name,updatetime)
		values (seq_eg_department.nextval,mapped_deptname,sysdate);
		select id_dept into deptid from eg_department where upper(dept_name)=upper(mapped_deptname);
	end;

	BEGIN
	   SELECT sectionname
	     INTO mapped_secname
	     FROM TMP_DEPT_SEC_MAPPING
	    WHERE OLD_DEPTNAME like emp.curr_dept and OLD_SECTIONNAME like emp.curr_section;
	EXCEPTION
	   WHEN NO_DATA_FOUND
	   THEN
	      dbms_output.put_line('section mapping  not found:' || emp.curr_dept || '::' || emp.curr_section );
			      
        END;
	id_functionary := null;
	if mapped_secname is not null and mapped_secname not like 'Ward%' then
		begin
		select id into id_functionary from functionary where upper(name)=upper(mapped_secname);
		exception
		when no_data_found then
			select SEQ_FUNCTIONARY.nextval into id_functionary from dual;
			insert into functionary (id,name,code,createtimestamp,updatetimestamp,isactive)
			values (id_functionary,mapped_secname,id_functionary,sysdate,sysdate,1);
		end;
	end if;
	
	/* if mapped_secname is not null and mapped_secname  like 'Ward%' then
		-- need to insert jurisdiction value
	end if;
	*/

	begin
	select designationid into desgid from eg_designation where upper(designation_name)=upper(emp.designation);
	exception
	when no_data_found then
		insert into eg_designation (DESIGNATIONID,DESIGNATION_NAME)
		values (seq_designation.nextval,emp.designation);
		select max(designationid) into desgid from eg_designation where upper(designation_name)=upper(emp.designation);
		dbms_output.put_line('Inserted Designation ->'||emp.designation);
	end;
	
	id_grade := null;
	if emp.class is not null then
		begin
		select id_grade into id_grade from EGEIS_GRADE_MSTR where upper(GRADE_VALUE)=upper(trim(emp.class));
		exception
		when no_data_found then
			select EGPIMS_GRADE_MSTR_SEQ.nextval into id_grade from dual;
			INSERT INTO EGEIS_GRADE_MSTR ( GRADE_ID, GRADE_VALUE, START_DATE, END_DATE,AGE ) VALUES ( 
			id_grade, upper(trim(emp.class)), TO_DATE('01-04-2000', 'DD-MM-YYYY'), TO_DATE('01-04-2099','DD-MM-YYYY'), 60); 

		end;
	end if;

	begin
	select id into vempid from eg_employee where code=emp.empid;
	exception
	when no_data_found then
		/* vempid:=egovcommon.DETAILKEY('Employee',emp.empid,emp.empname,'Payroll Employee Creation'); */
		
		SELECT EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL into vempid FROM DUAL;
		INSERT INTO eg_employee
			(ID, code,
			 emp_firstname, name, DATE_OF_FIRST_APPOINTMENT, GENDER, DATE_OF_BIRTH, EMPFATHER_LASTNAME,lastmodified_date
			)
		 VALUES (vempid, emp.empid,
			 emp.empname, emp.empname, emp.apptdate, emp.sex, emp.BIRTHDATE, emp.FATHERNAME,sysdate
			);
		INSERT INTO accountdetailkey
			(ID, groupid, detailtypeid,
			 detailname, detailkey
			)
			 VALUES (seq_accountdetailkey.NEXTVAL, 1, acdettype,
				 'Employee_id', vempid
			);

		dbms_output.put_line('Inserted Employee ->'||vempid||'/'||emp.empname);
	end;

	if upper(emp.incrmonth) LIKE upper('Jan%') then emp.incrdate:=to_date('31-Jan-2008');
	elsif upper(emp.incrmonth) LIKE upper('Feb%') then emp.incrdate:=to_date('28-Feb-2008');
	elsif upper(emp.incrmonth) LIKE upper('Mar%') then emp.incrdate:=to_date('31-Mar-2008');
	elsif upper(emp.incrmonth) LIKE upper('Apr%') then emp.incrdate:=to_date('30-Apr-2008');
	elsif upper(emp.incrmonth) LIKE upper('May%') then emp.incrdate:=to_date('31-May-2008');
	elsif upper(emp.incrmonth) LIKE upper('Jun%') then emp.incrdate:=to_date('30-Jun-2008');
	elsif upper(emp.incrmonth) LIKE upper('Jul%') then emp.incrdate:=to_date('31-Jul-2008');
	elsif upper(emp.incrmonth) LIKE upper('Aug%') then emp.incrdate:=to_date('30-Aug-2008');
	elsif upper(emp.incrmonth) LIKE upper('Sep%') then emp.incrdate:=to_date('30-Sep-2008');
	elsif upper(emp.incrmonth) LIKE upper('Oct%') then emp.incrdate:=to_date('31-Oct-2008');
	elsif upper(emp.incrmonth) LIKE upper('Nov%') then emp.incrdate:=to_date('30-Nov-2008');
	elsif upper(emp.incrmonth) LIKE upper('Dec%') then emp.incrdate:=to_date('31-Dec-2008');
	end if;

	if emp.incrdate is null then
		emp.incrdate:=to_date('31-Dec-2999');
	end if;
	dbms_output.put_line(emp.incrmonth||':'||emp.incrdate);
	
	begin
	select head.id,incr.incslabamt into v_idpay,v_incr from egpay_payscale_header head, EGPAY_PAYSCALE_INCRDETAILS incr 
		where head.name=emp.scale and head.id = incr.ID_PAYHEADER and incr.INCSLABFRMAMT <= emp.basic and incr.INCSLABTOAMT >= emp.basic;
	exception
	when no_data_found then
		select head.id,incr.incslabamt into v_idpay,v_incr from egpay_payscale_header head, EGPAY_PAYSCALE_INCRDETAILS incr 
				where head.name=emp.scale and head.id = incr.ID_PAYHEADER;
	end;
	
	insert into egpay_payscale_employee (ID,ID_PAYHEADER,ID_EMPLOYEE,EFFECTIVEFROM,ANNUAL_INCREMENT,CURR_BASICPAY)
	VALUES
	(SEQ_PAYSCALE_EMPLOYEE.nextval,v_idpay,vempid,to_date('01-Apr-2008'),to_date(emp.incrdate),emp.basic);
	dbms_output.put_line('Inserted for Payscale:'||vempid||':'||emp.empname);

	
		insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
		values (SEQ_ASS_PRD.nextval,to_date('01-Apr-2008'),to_date('31-Dec-2999'),vempid);
		select max(id) into id_emp_assg_prd from eg_emp_assignment_prd;

		insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION, ID_FUNCTIONARY, DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,GRADE_ID)
		values (SEQ_ASS.nextval,11,81,id_functionary,desgid,id_emp_assg_prd,deptid,id_grade);

		update eg_employee set isactive=1 where id=vempid;

		/* dbms_output.put_line('Inserted for Assignment:'||vempid||':'||emp.empname); */
	end if;
	
	if skip=1 then
		vPnotA:=vPnotA+1;
		dbms_output.put_line('Inserted for Payscale but NOT Assignment:'||vempid||':'||emp.empname);
	else
		vsucc:=vsucc+1;
		dbms_output.put_line('Inserted for Payscale AND Assignment:'||vempid||':'||emp.empname);
	end if;
	if errstr is not null then
		dbms_output.put_line('ERROR:'||errstr);
		vfail:=vfail+1;
	end if;

	
end loop;

update eg_employee set status = (select id from egw_status where moduletype='Employee' and description='Employed');

 UPDATE EG_EMPLOYEE e SET e.RETIREMENT_DATE=
  (SELECT grade.RETIREMENT_DATE FROM EGEIS_EMPLOYEE_GRADE grade WHERE grade.EMP_ID=e.ID);

dbms_output.put_line ('Inserted :'||vsucc||'P not A'||vPnotA||' Failed:'||vfail);

end knn_emp_assignment;


procedure knn_position_assignment as


vposid number default 0;
vdesname varchar2(50) default null;
vposname varchar2(100) default null;
vtemppos varchar2(100) default null;
skip number default 0;
vct number default 0;
vrun number default 0;


cursor empass is select id,designationid,position_id from eg_emp_assignment where position_id is null;

begin
dbms_output.enable(1000000);


for emp in empass loop
	vrun:=vrun+1;
	dbms_output.put_line(vrun||':'||emp.designationid);

	select designation_name into vdesname from eg_designation where designationid=emp.designationid;
	vposname:=vdesname;
	select count(*) into vct from eg_position where desig_id=emp.designationid;

	if vct=0 then
	    dbms_output.put_line('New designation');
	    vposname:=vposname||'_'||1;
	else
	    vposname:=vposname||'_'||to_char(vct+1);
	end if;

	insert into eg_position (POSITION_NAME,ID,DESIG_ID,EFFECTIVE_DATE)
	values (vposname,seq_pos.nextval,emp.designationid,sysdate);

	select max(id) into vposid from eg_position where desig_id=emp.designationid;
	update eg_emp_assignment set position_id=vposid where id=emp.id;


	dbms_output.put_line(vposname);

end loop;

end knn_position_assignment;



Procedure knn_emp_roll (saldate date) as

skip number default 0;
skip1 number default 0;
vfail number default 0;
vempid number default 0;
veeaid number default 0;
vegproll number default 0;
vsuccess number default 0;
vfyid number default 0;
vsalcodeid number default 0;
salid number default 0;
emptotal number default 0;
empdedn number default 0;
v_createdstatus number;

cursor emppaygen is select EMPID,EMPNAME,TOTAL,NETPAYMENT,nvl(BASIC,0) as BASIC,nvl(DA,0) as DA,nvl(HRA,0) as HRA,nvl(CA,0) as CA,nvl(VA,0) as VA,
nvl(OTHERALL,0) as OTHERALL,nvl(PF,0)+nvl(PFADVANCE,0) AS VPF,nvl(GENINSURANCE,0) as GENINSURANCE,
nvl(BLDGDED,0) as BLDGDED,nvl(VEHDEC,0) as VEHDEC,nvl(IT,0) as IT,nvl(BANKLOAN,0) as BANKLOAN,nvl(INSTAMT,0) as INSTAMT,
BANK,BANKACC FROM KNN_PAYROLL
where empid not in (select distinct ega.code from eg_employee ega, egpay_emppayroll egm where ega.id=egm.id_employee and egm.month=extract(month from saldate));

begin
dbms_output.enable(100000000000000000000);
vfail:=0;
vsuccess:=0;
   select id into v_createdstatus from egw_status where moduletype like 'PaySlip' and description like 'Created';

for empg in emppaygen loop
skip:=0;
skip1:=0;
emptotal:=nvl(empg.basic,0)+nvl(empg.basic*0.5,0)+nvl(empg.da,0)+nvl(empg.hra,0)+nvl(empg.ca,0)+nvl(empg.va,0)+nvl(empg.otherall,0);
empdedn:=nvl(empg.vpf,0)+nvl(empg.geninsurance,0)+nvl(empg.bldgded,0)+nvl(empg.vehdec,0)+nvl(empg.it,0)+nvl(empg.instamt,0);
dbms_output.put_line ('Gross'||emptotal);
	begin
	select id into vempid from eg_employee where code=empg.empid;
	exception
	when no_data_found then
		vfail:=vfail+1;
		dbms_output.put_line(vfail||' :Employee not found in master :'||empg.empid);
		skip:=1;
	end;
	if emptotal<=0 then
		skip:=1;
		vfail:=vfail+1;
		dbms_output.put_line('Skipping Employee: no Gross Pay->'||empg.empname);
	end if;

	if skip=0 then
		dbms_output.put_line('Processing:'||empg.empname||':'||vempid);
		/* insert into EGPAY_EMPPAYROLL,EGPAY_EARNINGS,EGPAY_DEDUCTIONS */
		begin
		select eea.id into VEEAID from eg_emp_assignment eea, eg_emp_assignment_prd eeap
		where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=vempid;
		exception
		when no_data_found then
			vfail:=vfail+1;
			dbms_output.put_line(vfail||' :Employee Assignment not found :'||empg.empid);
			skip1:=1;
		end;


		if skip1=0 then
			update egpay_payscale_employee set curr_basicpay=empg.basic where id_employee=vempid;
			dbms_output.put_line('Processing -'||empg.empid);

			vfyid:=generatefinyear(saldate);

			if vfyid>0 then
				insert into egpay_emppayroll
				(ID,ID_EMPLOYEE,ID_EMP_ASSIGNMENT,GROSS_PAY,NET_PAY,CREATEDBY,CREATEDDATE,
				FINANCIALYEARID,NUMDAYS,MONTH,STATUS,BASIC_PAY,PAYTYPE,WORKINGDAYS,FROMDATE,TODATE,LASTMODIFIEDDATE)
				values
				(eispayroll_emppayroll_seq.nextval,vempid,veeaid,emptotal,emptotal-empdedn,1,sysdate,vfyid,
				(last_day(saldate)-last_day(add_months(saldate,-1))),extract(month from saldate),v_createdstatus,empg.basic,1,(last_day(saldate)-last_day(add_months(saldate,-1))),to_date(saldate),last_day(to_date(saldate)),sysdate);
				select max(id) into vegproll from egpay_emppayroll;
				dbms_output.put_line('Earnings ----');
				/* insert earnings */
				if empg.basic>0 then
					select id into salid from egpay_salarycodes where upper(head)='BASIC';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,empg.BASIC);
					dbms_output.put_line('Basic ----');
					select id into salid from egpay_salarycodes where upper(head)='SDA';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,50,(0.5*empg.BASIC));
					dbms_output.put_line('SDA ----');
				end if;
				if empg.da>0 then
					select id into salid from egpay_salarycodes where upper(head)='DA';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,PCT,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,70.5,empg.DA);
					dbms_output.put_line('DA ----');
				end if;
				if empg.hra>0 then
				select id into salid from egpay_salarycodes where upper(head)='HRA';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,empg.HRA);
					dbms_output.put_line('HRA ----');
				end if;
				if empg.ca>0 then
				select id into salid from egpay_salarycodes where upper(head)='CCA';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,empg.CA);
					dbms_output.put_line('CA ----');
				end if;
				if empg.OTHERALL>0 then
				select id into salid from egpay_salarycodes where upper(head)='OTHERALL';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,empg.otherall);
					dbms_output.put_line('Otherall ----');
				end if;
				if empg.VA>0 then
				select id into salid from egpay_salarycodes where upper(head)='VA';
					insert into egpay_earnings
					(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
					values
					(eispayroll_earnings_seq.nextval,vegproll,salid,empg.VA);
					dbms_output.put_line('VA ----');
				end if;
				/* insert deductions */
				if empg.vpf>0 then
				select id into salid from egpay_salarycodes where upper(head)='PF';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.vPF);
				end if;
				if empg.IT>0 then
				select id into salid from egpay_salarycodes where upper(head)='IT';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.IT);
				end if;
				if empg.INSTAMT>0 then
				select id into salid from egpay_salarycodes where upper(head)='BANK';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.instamt);
				end if;
				if empg.geninsurance>0 then
				select id into salid from egpay_salarycodes where upper(head)='GIS';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.geninsurance);
				end if;
				if empg.bldgded>0 then
				select id into salid from egpay_salarycodes where upper(head)='BLDGDED';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.bldgded);
				end if;
				if empg.VEHDEC>0 then
				select id into salid from egpay_salarycodes where upper(head)='VEHDED';
							insert into egpay_deductions
							(ID,ID_EMPPAYROLL,ID_SALCODE,AMOUNT)
							values
							(eispayroll_deductions_seq.nextval,vegproll,salid,empg.VEHDEC);
				end if;

				vsuccess:=vsuccess+1;
				dbms_output.put_line(vsuccess||':Employee inserted:'||empg.empname||'/'||empg.empid);
			end if; /* vfyid>0 */
		end if; /* skip1=0 */
	end if; /* skip=0 */
end loop;
dbms_output.put_line ('----------------------------------------');
dbms_output.put_line ('Successful:'||vsuccess);
dbms_output.put_line ('Failed:'||vfail);

/* return(vsuccess+vfail); */
end knn_emp_roll;

/*
procedure empupdate as

vgen varchar2(5);
vbirth date;
vfather varchar2(50);
vid varchar2(1024);
vpan varchar2(20);
vappt date;
vpermadd varchar2(256);
vbank varchar2(256);
vbankacc varchar2(100);

cursor egemp is select id,emp_firstname from eg_employee;

begin

for ee in egemp loop

select permadd,sex,birthdate,apptdate,fathername,identification,pan,bank,bankacc into vpermadd,
vgen,vbirth,vappt,vfather,vid,vpan,vbank,vbankacc from knn_payroll where empname=ee.emp_firstname;

if vgen='M' then
   update eg_employee set gender=1,date_of_birth=vbirth,date_of_first_appointment=vappt,
   empfather_firstname=vfather,pan_number=vpan,identification_marks1=vid,
   bank=vbank,salary_bank=vbankacc,is_handicapped=0,is_med_report_available=0
   where id=ee.id;
elsif vgen='F' then
   update eg_employee set gender=0,date_of_birth=vbirth,date_of_first_appointment=vappt,
   empfather_firstname=vfather,pan_number=vpan,identification_marks1=vid,
   bank=vbank,salary_bank=vbankacc,is_handicapped=0,is_med_report_available=0
   where id=ee.id;
end if;
end loop;

end empupdate;
*/

procedure empupdate_masters as

vcadre varchar2(256);
vcaste varchar2(256);
vclass varchar2(256);
vappttype varchar2(256);
vemr number;
vcomm number;
vretirement date;
vmultiplier number;
vuser number;
vbankbranch varchar2(512);
vbank varchar2(256);
vbranch varchar2(256);
vbankacc varchar2(256);
vbankid number;
vbranchid number;
v_permaddr_type number;
vaddressid number;
vpermaddress varchar2(512);

cursor egemp is select id,code,emp_firstname,date_of_birth from eg_employee where id not in (select e_add.id from EGEIS_PERSON_ADDRESS e_add);

begin
select id_address_type into v_permaddr_type from EG_ADDRESS_TYPE_MASTER where name_address_type = 'PERMANENTADDRESS';

for ee in egemp loop
begin
	DBMS_OUTPUT.PUT_LINE('Processing for employee:' || ee.code || ' :: ' || ee.emp_firstname);
	select permadd, cadre,caste,class,appttype,bank || nvl2(bank_branch, ',' || bank_branch , '' )  ,bankacc into vpermaddress, vcadre,vcaste,vclass,vappttype,vbankbranch,vbankacc
	from knn_payroll where empid=ee.code;


	BEGIN 
		select id_user into vuser from eg_user where first_name=ee.emp_firstname;
	exception
			WHEN NO_DATA_FOUND then
				dbms_output.put_line('No User. Not Inserting '||ee.emp_firstname);
				vuser := null;

		end;
		 BEGIN 
			select mode_of_recruiment_id into vemr from egeis_mode_of_recruiment_mstr where
			 upper(vappttype) like '%' || upper(mode_of_recruiment_name) || '%';

		 exception
				WHEN NO_DATA_FOUND then
					dbms_output.put_line('No Matching mode of recruitment. Not Inserting '||vappttype);
					vemr := null;

		end;
		BEGIN 
			select community_id into vcomm from egeis_community_mstr where upper(community_name) like upper(vcaste);

		 exception
			WHEN NO_DATA_FOUND then
			dbms_output.put_line('No Matching community. Not Inserting '||vcaste);
			vcomm := null;

		end;

	select 60*12 into vmultiplier from dual;

	vretirement:=add_months(ee.date_of_birth,vmultiplier);

	update eg_employee set community_id=vcomm,mode_of_recruiment_id=vemr,id_user=vuser,recruitment_type_id=1,grade_id=1,
	retirement_age=60,maturity_date=to_date(vretirement) where id=ee.id;

	begin
	  select id_bank into vbankid from egeis_bank_det where id = ee.id;
	exception
	  WHEN NO_DATA_FOUND THEN
		vbank:= get_token(vbankbranch,1,',');
		vbranch:= get_token(vbankbranch,2,',');

		DBMS_OUTPUT.PUT_LINE('BANK and BRANCH '||vbank||' :: '|| vbranch || ' :: for '|| EE.ID);
		vbankid := null;
		BEGIN 
			select id into vbankid from bank where upper(name) like upper(vbank);
		exception
				WHEN NO_DATA_FOUND then
					dbms_output.put_line('No bank. Inserting '||vbank);
					SELECT SEQ_BANK.nextval into vbankid from dual;
					insert into bank (ID,CODE,NAME,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY)
					values (vbankid,vbankid,vbank,1,sysdate,sysdate,1);

			end;

		vbranchid := null;
		IF vbranch IS NOT NULL THEN	
			BEGIN 

				select id into vbranchid from bankbranch where upper(branchname) like upper(vbranch) and bankid = vbankid;
			exception
				WHEN NO_DATA_FOUND then
					dbms_output.put_line('No branch. Inserting '||vbranch);
					SELECT SEQ_BANKBRANCH.nextval into vbranchid from dual;
					insert into bankbranch (ID,BRANCHCODE,BRANCHNAME,BRANCHADDRESS1,BRANCHCITY,BANKID,ISACTIVE,CREATED,LASTMODIFIED,MODIFIEDBY)
					values (vbranchid,vbranchid,vbranch,vbranch,'Kanpur',vbankid,1,sysdate,sysdate,1);

			end;
		END IF;

		insert into egeis_bank_det (ID_BANK,ID,SALARY_BANK,BANK,BRANCH,ACCOUNT_NUMBER)
		values ( EGPIMS_BANK_DET_SEQ.nextVal,ee.id,1,vbankid,vbranchid,vbankacc);
	end;

	 begin
	 	SELECT ID_PERSON_ADDRESS into vaddressid FROM EGEIS_PERSON_ADDRESS where ID = ee.id;
	 exception
	 WHEN NO_DATA_FOUND THEN
		select SEQ_ADDRESS.nextval into vaddressid from dual;
		INSERT INTO EG_ADDRESS (ADDRESSID, STREETADDRESS1,ID_ADDRESSTYPEMASTER ,LASTUPDATEDTIMESTAMP) 
		VALUES ( vaddressid,vpermaddress ,v_permaddr_type, sysdate);

		-- TBD: create an entry in egeis_person_address for emp 
		INSERT INTO EGEIS_PERSON_ADDRESS (
		   ID_PERSON_ADDRESS, ID, ID_ADDRESS) 
		VALUES (EGPIMS_ADD_SEQ.nextval ,ee.id ,vaddressid );
	end;
exception
	WHEN NO_DATA_FOUND then
	dbms_output.put_line('Error: Could not update  '|| ee.code || ' :: ' || ee.emp_firstname);
end;

end loop;

end empupdate_masters;


end EMP_PAYROLL;
/

