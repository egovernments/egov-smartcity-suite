CREATE OR REPLACE PROCEDURE BILL_UPDATE
AS
  v_num		  varchar2(100);
  v_date	  date;
  v_billid	  number default 0;
  v_VHID	  number default 0;
  v_BILLNUM	  varchar2(100) default 0;

  CURSOR C1
	is
	select * from SALARYBILLDETAIL
	where BILLID is NULL;


BEGIN
	 dbms_output.put_line('Getting into the loop');

	 FOR SALBILL_cursor in C1
	 LOOP
	 	v_VHID := SALBILL_cursor.voucherheaderid;

		 select vouchernumber,voucherdate into v_num,v_date from voucherheader where ID=v_VHID;

		 dbms_output.put_line('Current Voucher'||v_num);

  		 insert into EG_BILLREGISTER (ID,BILLNUMBER,BILLDATE,BILLAMOUNT,BILLSTATUS,PASSEDAMOUNT,EXPENDITURETYPE,CREATEDBY,CREATEDDATE,STATUSID)
  		  values
		   (seq_eg_billregister.nextval,v_num,v_date,SALBILL_cursor.NETPAY,'PASSED',SALBILL_cursor.NETPAY,'Salary',1,to_date('20-Jul-2007'),30);

		 v_BILLNUM := v_num;
		select ID into v_billid from eg_billregister where billnumber=v_BILLNUM;

		insert into EG_BILLREGISTERMIS (ID,BILLID,DEPARTMENTID,MONTH,FINANCIALYEARID,LASTUPDATEDTIME)
		 values
		   (seq_eg_billregisterMIS.nextval,v_billid,1,SALBILL_cursor.mmonth,SALBILL_cursor.financialyearid,to_date('20-Jul-2007'));

		 insert into EG_ACTIONDETAILS(ID,MODULETYPE,MODULEID,ACTIONDONEBY,ACTIONDONEON,CREATEDBY,LASTMODIFIEDDATE,ACTIONTYPE)
		 values
		   (SEQ_EG_ACTIONDETAILS.NEXTVAL,'SALBILL',v_billid,1,to_date('20-Jul-2007'),1,to_date('20-Jul-2007'),'Salary Voucher Created');

		 insert into EGW_SATUSCHANGE (ID,MODULETYPE,MODULEID,FROMSTATUS,TOSTATUS,CREATEDBY,LASTMODIFIEDDATE)
		 values
		   (SEQ_EGW_SATUSCHANGE.NEXTVAL,'SALBILL',v_billid,29,30,1,to_date('20-Jul-2007'));

		 update SALARYBILLDETAIL
		 set BILLID=v_billid where voucherheaderid=v_VHID;

		 dbms_output.put_line ('Successfully inserted into BillRegister for ->'||v_num);
	END LOOP;

	dbms_output.put_line ('Finished!!');
END;
/
CREATE OR REPLACE PROCEDURE Deleter(tname VARCHAR2) AS
CURSOR c1 IS SELECT  tname FROM tab;
tablname VARCHAR2(100);  
BEGIN
OPEN c1;
LOOP
FETCH c1 INTO tablname;
IF(SUBSTR(tablname,0,3)= tname)
THEN
dbms_output.put_line(tablname);
END IF;
EXIT WHEN c1%NOTFOUND;
END LOOP;
END;
/
CREATE OR REPLACE procedure emp_assignment as


temp_funcode number default 0;
temp_depcode  varchar2(25);
funid  number default 0;
deptid  number default 0;
id_emp  number default 0;
id_design  number default 0;
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
id_funct number default 0;

cursor empmaster is select distinct empcode,empname,designation,scale,presentbasic,incdate from tmp_empmaster;

begin
for emp in empmaster loop
	skip:=0;
	skip1:=0;
	errstr:='';
	begin
	select tc.functioncode,tc.department,tc.centercode into temp_funcode,temp_depcode,temp_ccode
	from tmp_centermaster tc, tmp_paygendetails tp
	where tc.centercode=tp.paygen_cenid and tp.paygen_empid=emp.empcode;
	exception
	when no_data_found then
		skip:=1;
	end;

	if skip=0 then
		begin
		select id into funid from function where code=temp_funcode;
		exception
		when no_data_found then
			errstr:=errstr||'Function code not found'||temp_funcode;
			skip:=1;
		end;

		begin
		select id_dept into deptid from eg_department where upper(dept_name)=upper(temp_depcode);
		exception
		when no_data_found then
			errstr:=errstr||'Dept Code not found'||temp_depcode;
			skip:=1;
		end;
		
		begin
		select id into id_funct from functionary where code=temp_ccode;
		exception
		when no_data_found then
			 errstr:=errstr||'Functionary not found'||temp_ccode;
			 skip:=1;
		end;
	end if;

	begin
	select id into id_emp from eg_employee where code=emp.empcode;
	exception
	when no_data_found then
		dbms_output.put_line('Emp Code does not exist in Employee Master:'||emp.empcode);
		skip:=1;
	end;

	begin
	select designationid into id_design from eg_designation where designation_name=emp.designation;
	exception
	when no_data_found then
		dbms_output.put_line('Designation does not exist:'||emp.designation);
		skip:=1;
	end;

	if emp.incdate is null then
		v_incdate:=to_date('31-Dec-2999');
		else
		v_incdate:=to_date(emp.incdate);
	end if;

	select id,incrementamount into v_idpay,v_incr from egpay_payscale_header where name=emp.scale;
	insert into egpay_payscale_employee (ID,ID_PAYHEADER,ID_EMPLOYEE,EFFECTIVEFROM,ANNUAL_INCREMENT,CURR_BASICPAY)
	VALUES
	(seq_egpay_payscale_employee.nextval,v_idpay,id_emp,to_date('01-Apr-2007'),to_date(v_incdate),emp.presentbasic);
	dbms_output.put_line('Inserted for Payscale:'||id_emp||':'||emp.empname);

	if skip=0 then

		insert into eg_emp_assignment_prd (id,from_date,to_date,id_employee)
		values (seq_eg_emp_assignment_prd.nextval,to_date('01-Jan-2008'),to_date('31-Dec-2999'),id_emp);
		select max(id) into id_emp_assg_prd from eg_emp_assignment_prd;

		insert into eg_emp_assignment (ID,ID_FUND,ID_FUNCTION,DESIGNATIONID,ID_EMP_ASSIGN_PRD,MAIN_DEPT,ID_FUNCTIONARY)
		values (seq_eg_emp_assignment.nextval,11,funid,id_design,id_emp_assg_prd,deptid,id_funct);

		/* dbms_output.put_line('Inserted for Assignment:'||id_emp||':'||emp.empname); */
	end if;

	if skip=1 then
		vPnotA:=vPnotA+1;
		dbms_output.put_line('Inserted for Payscale but NOT Assignment:'||id_emp||':'||emp.empname);
	else
		vsucc:=vsucc+1;
		dbms_output.put_line('Inserted for Payscale AND Assignment:'||id_emp||':'||emp.empname);
	end if;
	if errstr is not null then
		dbms_output.put_line('ERROR:'||errstr);
		vfail:=vfail+1;
	end if;

end loop;

dbms_output.put_line ('Inserted :'||vsucc||'P not A'||vPnotA||' Failed:'||vfail);

end;
/
CREATE OR REPLACE procedure emp_assignment2 as


temp_funcode number default 0;
temp_depcode  varchar2(25);
funid  number default 0;
deptid  number default 0;
id_emp  number default 0;
id_design  number default 0;
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
id_funct number;
id_epa number;

cursor empmaster is select distinct empcode,empname,designation,scale,presentbasic,incdate from tmp_empmaster;

begin
for emp in empmaster loop
	skip:=0;
	skip1:=0;
	errstr:='';
	begin
	select tc.functioncode,tc.department,tc.centercode into temp_funcode,temp_depcode,temp_ccode
	from tmp_centermaster tc, tmp_paygendetails tp
	where tc.centercode=tp.paygen_cenid and tp.paygen_empid=emp.empcode;
	exception
	when no_data_found then
		skip:=1;
	end;

	if skip=0 then
		begin
		select id into funid from function where code=temp_funcode;
		exception
		when no_data_found then
			errstr:=errstr||'Function code not found'||temp_funcode;
			skip:=1;
		end;

		begin
		select id_dept into deptid from eg_department where upper(dept_name)=upper(temp_depcode);
		exception
		when no_data_found then
			errstr:=errstr||'Dept Code not found'||temp_depcode;
			skip:=1;
		end;
	end if;

	begin
	select id into id_emp from eg_employee where code=emp.empcode;
	exception
	when no_data_found then
		dbms_output.put_line('Emp Code does not exist in Employee Master:'||emp.empcode);
		skip:=1;
	end;

	begin
	select designationid into id_design from eg_designation where designation_name=emp.designation;
	exception
	when no_data_found then
		dbms_output.put_line('Designation does not exist:'||emp.designation);
		skip:=1;
	end;

	if emp.incdate is null then
		v_incdate:=to_date('31-Dec-2999');
		else
		v_incdate:=to_date(emp.incdate);
	end if;

	if skip=0 then

	    select id into id_emp_assg_prd from eg_emp_assignment_prd where id_employee=id_emp;
		select id into id_epa from eg_emp_assignment where id_emp_assign_prd=id_emp_assg_prd;
		select id into id_funct from functionary where code=temp_ccode;
		
		update eg_emp_assignment set id_functionary=id_funct where id=id_epa;
			
		/* dbms_output.put_line('Inserted for Assignment:'||id_emp||':'||emp.empname); */
	end if;

	if skip=1 then
		vPnotA:=vPnotA+1;
		dbms_output.put_line('Inserted for Payscale but NOT Assignment:'||id_emp||':'||emp.empname);
	else
		vsucc:=vsucc+1;
		dbms_output.put_line('Inserted for Payscale AND Assignment:'||id_emp||':'||emp.empname);
	end if;
	if errstr is not null then
		dbms_output.put_line('ERROR:'||errstr);
		vfail:=vfail+1;
	end if;

end loop;

dbms_output.put_line ('Inserted :'||vsucc||'P not A'||vPnotA||' Failed:'||vfail);

end;
/
CREATE OR REPLACE procedure GVMCSALARYJVLOAD  as

CURSOR VHEADER is select voucherdate,vouchernum,fund,department from salary_load@remote_connect;
 CURSOR GLEDGER is select vouchernum,function,accounthead,earndeduct,sum(amount) as sumamount from salary_load@remote_connect
 group by vouchernum,function,accounthead,earndeduct;
 CURSOR GLDETAIL is select voucherdate,vouchernum,accounthead,empno,amount from salary_load_details@remote_connect;

ct number default 0;
errstr varchar2(1000);
vfunctionid number default 0;
vhid number default 0;
newvoucher number default 0;
skip number default 0;
skipgl number default 0;
skipgldet number default 0;
fperiodid number default 0;
vearning number default 0;
vdeduction number default 0;
vnetpay number default 0;
cumulativenetpay number default 0;
earnded varchar2(2);
vglcodeid number default 0;
vdetline number default 0;
vglcode number default 0;
vglname varchar2(150);
vdetailid number default 0;
vhmonth number default 0;
vouchercount number default 0;
vglamount number default 0;
vtempglid number default 0;
vdate date;
detailtypeid number default 0;
vdetkey number default 0;
vgldtlid number default 0;
vname varchar2(25);
vtype varchar2(25);
vdepartmentid number default 0;
vfundid number default 0;
vcgntype varchar2(5);
vcgvntype varchar2(5);
vdetailtypeid number default 0;
vglid number default 0;
vaccounthead varchar2(10);




 BEGIN
 dbms_output.enable(1000000);
 dbms_output.put_line('STARTING');
 cumulativenetpay:=0;
 vouchercount:=0;


FOR VH in VHEADER LOOP
errstr:='';
newvoucher:=0;
skip:=0;
vhmonth:=extract(month from vh.voucherdate);
vdate:=to_date(vh.voucherdate);

/* Check if the vouchernum already exists in the header */
begin
select id into VHID from voucherheader where vouchernumber=vh.vouchernum;
exception
WHEN NO_DATA_FOUND then
	/* new voucher */
	newvoucher:=1;
end;

if newvoucher=1 then
/* check if masters exist */

 	begin
 	select id into vfundid from fund where code=vh.fund;
 	exception
 	WHEN NO_DATA_FOUND then
 		errstr:=concat('SEG:',vh.fund);
 	end;



 	begin
 	select id_dept into vDEPARTMENTID from eg_department where dept_name=VH.DEPARTMENT;
 	exception
 	WHEN NO_DATA_FOUND then
 		vDEPARTMENTID:= egovcommon.insert_dept(VH.DEPARTMENT);
 	end;

   	/* get the current fiscalperiod */

   	fperiodid:=egovcommon.generatefiscal(VH.VOUCHERDATE);
   	if (fperiodid = -1) then
   		errstr:=errstr+concat(',FISCALPERIOD:',FPERIODID);
   	end if;

   	/* error processing for invalid masters, closed period */
   	if errstr is not NULL then
   		dbms_output.put_line('ROW ->'||VH.VOUCHERNUM||' FAILED:'||errstr);
   		skip:=1;
 	else
		dbms_output.put_line('INSERTING VOUCHER ->'||VH.VOUCHERNUM);
		skip:=0;
 	end if;

 	if skip=0 then  /* valid voucher header details */
 		vname:='Salary Journal';
		vtype:='Journal Voucher';
		vcgntype:='SAL';
		vcgvntype:='J';
 		VHID:=egovcommon.AP_CREATEVOUCHERHEADER(vh.voucherdate,fperiodid,vfundid,vh.vouchernum,vname,vtype,vcgntype,vcgvntype,vdepartmentid);
 		dbms_output.put_line('GENERATED Voucher Header:->'||VHID);
 		/* Insert into voucherheader, vouchermis, egf_record_status */

		if VHID>-1 then
		/* begin GL insert block */
			vearning:=0;
			vdeduction:=0;
			vdetline:=0;
			for GL in GLEDGER loop
				vglcodeid:=0;
				vglcode:=0;
				vglname:='';
				if gl.vouchernum = vh.vouchernum then
					/* check for Masters */
						if gl.function is not NULL then
							begin
								select id into vfunctionid from function where code=gl.function and isactive=1;
								exception
								WHEN NO_DATA_FOUND then
								errstr:=errstr+concat(':FUNCT:',gl.function);
							end;
						else
							vfunctionid:=NULL;
						end if;
						begin
						vaccounthead:=concat(substr(gl.accounthead,1,3),concat(substr(gl.accounthead,5,2),substr(gl.accounthead,8,2)));
						select id,glcode,name into vglcodeid,vglcode,vglname from chartofaccounts where glcode=vaccounthead and classification=4 and isactiveforposting=1;
						exception
						WHEN NO_DATA_FOUND then
							errstr:=concat(errstr,concat(':GLCODE:',vaccounthead));
						end;
					/* error processing for invalid masters*/
						if errstr is not NULL then
							dbms_output.put_line('ROW ->'||VH.VOUCHERNUM||' FAILED:'||errstr);
							skipgl:=1;
						else
							/* dbms_output.put_line('INSERTING GL DETAILS->'||VH.VOUCHERNUM||': '||vglcode||': '||gl.amount); */
							skipgl:=0;
						end if;
					if skipgl=0 then
						vglamount:=gl.sumamount;
						vdetline:=vdetline+1;
						vdetailid:=0;
						if gl.earndeduct ='D' then
							vdeduction:=vdeduction+vglamount;
							if vglamount>0 then
								insert into voucherdetail
								(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
								values
								(seq_voucherdetail.nextval,vdetline,vhid,vglcode,vglname,0,vglamount);
								select id into vdetailid from voucherdetail where voucherheaderid=vhid and lineid=vdetline;
								dbms_output.put_line('Inserting->'||vdetailid||':'||vhid||':'||':'||vfunctionid||':'||vglcode||':'||vglcodeid||':'||gl.sumamount);

								begin
								insert into generalledger
								(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid,FUNCTIONID)
								values
								(seq_generalledger.nextval,vdetailid,vdate,vglcodeid,vglcode,0,vglamount,vhid,vfunctionid);
								exception
								when others then
								     errstr:=concat(errstr,substr(SQLERRM,1,200));
								     dbms_output.put_line(errstr);
								     skipgl:=1;
								end;
							else
								insert into voucherdetail
								(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
								values
								(seq_voucherdetail.nextval,vdetline,vhid,vglcode,vglname,-vglamount,0);
								select id into vdetailid from voucherdetail where voucherheaderid=vhid and lineid=vdetline;
								/* dbms_output.put_line('Inserting->'||vdetailid||':'||vhid||':'||':'||vfunctionid||':'||vglcode||':'||vglcodeid||':'||gl.amount); */

								begin
								insert into generalledger
								(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid,FUNCTIONID)
								values
								(seq_generalledger.nextval,vdetailid,vdate,vglcodeid,vglcode,-vglamount,0,vhid,vfunctionid);
								exception
								when others then
									errstr:=concat(errstr,substr(SQLERRM,1,200));
									dbms_output.put_line(errstr);
									skipgl:=1;
								end;
							end if;
						elsif gl.earndeduct='E' then
							vearning:=vearning+vglamount;
							if gl.sumamount>0 then
								insert into voucherdetail
								(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
								values
								(seq_voucherdetail.nextval,vdetline,vhid,vglcode,vglname,vglamount,0);
								select id into vdetailid from voucherdetail where voucherheaderid=vhid and lineid=vdetline;
								dbms_output.put_line('Inserting->'||vdetailid||':'||vhid||':'||':'||vfunctionid||':'||vglcode||':'||vglcodeid||':'||gl.sumamount);

								begin
								insert into generalledger
								(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid,FUNCTIONID)
								values
								(seq_generalledger.nextval,vdetailid,vdate,vglcodeid,vglcode,vglamount,0,vhid,vfunctionid);
								exception
								when others then
								     errstr:=concat(errstr,substr(SQLERRM,1,200));
								     dbms_output.put_line(errstr);
								     skipgl:=1;
								end;
							else
								insert into voucherdetail
								(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
								values
								(seq_voucherdetail.nextval,vdetline,vhid,vglcode,vglname,0,-vglamount);
								select id into vdetailid from voucherdetail where voucherheaderid=vhid and lineid=vdetline;
								/* dbms_output.put_line('Inserting->'||vdetailid||':'||vhid||':'||':'||vfunctionid||':'||vglcode||':'||vglcodeid||':'||gl.amount); */

								begin
								insert into generalledger
								(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid,FUNCTIONID)
								values
								(seq_generalledger.nextval,vdetailid,vdate,vglcodeid,vglcode,0,-vglamount,vhid,vfunctionid);
								exception
								when others then
									errstr:=concat(errstr,substr(SQLERRM,1,200));
									dbms_output.put_line(errstr);
								        skipgl:=1;
								end;
							end if;
						end if; /* gl-row insert */
						if skipgl=0 then /* insert subledger details */
							skipgldet:=0;
							for GLD in GLDETAIL loop
								if (gld.vouchernum=gl.vouchernum) and (gld.accounthead=gl.accounthead) then
										begin
										select id into vglid from generalledger where voucherheaderid=vhid and glcode=gl.accounthead;
										exception
										when NO_DATA_FOUND then
											dbms_output.put_line('GLENTRY NOT FOUND FOR: '||GLD.VOUCHERNUM||' : '||GLD.ACCOUNTHEAD);
											skipgldet:=1;
										end;

										begin
										select cad.detailtypeid into vdetailtypeid from chartofaccountdetail cad,chartofaccounts ca
										where cad.glcodeid=ca.id and ca.glcode=gl.accounthead;
										exception
										when NO_DATA_FOUND then
											dbms_output.put_line('ACCOUNT HEAD IS NOT SETUP AS A SUB-LEDGER CODE:'||GL.ACCOUNTHEAD);
											skipgldet:=1;
										end;
										if skipgldet=0 then
											vdetkey:=egovcommon.detailkey('Employee',gld.empno);
											if vdetkey>-1 then
												insert into generalledgerdetail (id,generalledgerid,detailkeyid,detailtypeid,amount)
												values (seq_generalledgerdetail.nextval,vglid,vdetkey,vdetailtypeid,gld.amount);
												select id into vgldtlid from generalledgerdetail where generalledgerid=vglid and detailkeyid=vdetkey;

												insert into eg_remittance_gldtl (id,gldtlid,gldtlamt,lastmodifieddate,remittedamt)
												values (seq_eg_remittance_gldtl.nextval,vgldtlid,gld.amount,sysdate,0);
											end if;
										end if; /* skipgldet */
								end if;
							end loop; /* for GLDETAIL */
						end if; /* skipgl=0: inner */
					end if; /* skipgl=0: outer */

				end if; /* vh.vouchernum=gl.vouchernum */
			if skipgl=1 or skipgldet=1 then
				rollback;
			end if;
			END LOOP; /* GLEDGER CURSOR */
 			if skipgl=0 then
 				vnetpay:=vearning-vdeduction;
 				begin
				select id,glcode,name into vglcodeid,vglcode,vglname from chartofaccounts where purposeid=31;
				exception
				WHEN NO_DATA_FOUND then
					errstr:=concat(errstr,':SALARY PAYABLE CODE NOT FOUND');
					dbms_output.put_line(errstr);
				end;
				if errstr is NULL then
					dbms_output.put_line('Net Payable->'||vnetpay);
					cumulativenetpay:=cumulativenetpay+vnetpay;
					vdetline:=vdetline+1;
					/* insert the Salary Payable row in GL for the net amount */
					insert into voucherdetail
					(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
					values
					(seq_voucherdetail.nextval,vdetline,vhid,vglcode,vglname,0,vnetpay);
					select max(id) into vdetailid from voucherdetail where voucherheaderid=vhid and glcode=vglcode;

					/* insert into generalldeedger */
					begin
					/* dbms_output.put_line('Inserting->'||vdetailid||':'||vhid||':'||':'||vfunctionid||':'||vglcode||':'||vglcodeid||':'||vnetpay); */
					insert into generalledger
					(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid,FUNCTIONID)
					values
					(seq_generalledger.nextval,vdetailid,vdate,vglcodeid,vglcode,0,vnetpay,vhid,vfunctionid);
					/* dbms_output.put_line('INSERTING GL DETAILS->'||VH.VOUCHERNUM||': '||vglcode||': '||vnetpay); */
					exception
					when others then
					     dbms_output.put_line(substr(SQLERRM,1,200));
					     skipgl:=1;
					end;

					/* insert into salarybilldetail */

      					insert into salarybilldetail
					(ID,VOUCHERHEADERID,MMONTH,GROSSPAY,TOTALDEDUCTIONS,NETPAY,PAIDAMOUNT,ISREVERSED)
					values
					(seq_salarybilldetail.nextval,vhid,vhmonth,vearning,vdeduction,vnetpay,0,0);
					dbms_output.put_line('SUCCESSFULLY INSERTED ->'||VH.VOUCHERNUM);
					vouchercount:=vouchercount+1;
					end if;
 			end if; /* skipgl=0 for salary payable entry */
		end if; /* vhid>-1*/
		if vhid=-1 then
			rollback;
		end if;
	end if; /*skip=0*/
	if skip=1 then
		rollback;
	end if;
end if; /* newvoucher=1 */
END LOOP; /* voucherheader */
dbms_output.put_line('NET PAY->'||cumulativenetpay);
dbms_output.put_line('INSERTED VOUCHER NUMBERS->'||vouchercount);

END GVMCSALARYJVLOAD;
/
CREATE OR REPLACE PROCEDURE INSERTGLCODE (mcode varchar2,mname varchar2) as

vid number default 0;
vuser number default 0;
vinsert number default 0;
vparentid number default 0;
vtype varchar2(2);

begin
	if substr(mcode,1,1)='1' then
		vtype:='I';
	elsif substr(mcode,1,1)='2' then
		vtype:='E';
	elsif substr(mcode,1,1)='3' then
		vtype:='L';
	elsif substr(mcode,1,1)='4' then
		vtype:='A';
	end if;
	select id_user into vuser from eg_user where user_name='egovernments';
	begin
	select id into vid from chartofaccounts where glcode=mcode;
	exception
	WHEN NO_DATA_FOUND then
		vinsert:=1;
		begin
		select id into vparentid from chartofaccounts where glcode=substr(mcode,1,3);
		exception
		WHEN NO_DATA_FOUND then
		vinsert:=0;
		dbms_output.put_line('Major code does not exist for:'||mcode);
		end;
	end;
	if vinsert=1 then
		insert into chartofaccounts
		(ID,GLCODE,NAME,ISACTIVEFORPOSTING,ISACTIVE,LASTMODIFIED,MODIFIEDBY,CREATED,CLASSIFICATION,PARENTID,LEVELNUMBER,GROUPID,TYPE)
		values
		(seq_chartofaccounts.nextval,mcode,mname,0,1,sysdate,vuser,sysdate,2,vparentid,5,1,VTYPE);
		select id into vparentid from chartofaccounts where glcode=mcode;
		insert into chartofaccounts
		(ID,GLCODE,NAME,ISACTIVEFORPOSTING,ISACTIVE,LASTMODIFIED,MODIFIEDBY,CREATED,CLASSIFICATION,PARENTID,LEVELNUMBER,GROUPID,TYPE)
		values
		(seq_chartofaccounts.nextval,concat(mcode,'00'),mname,0,1,sysdate,vuser,sysdate,4,vparentid,5,1,VTYPE);
	end if;
	if vinsert=0 then
		dbms_output.put_line('Major code already exists for:'||mcode);
	end if;
commit;

end insertglcode;
/
CREATE OR REPLACE procedure position_assignment as


vposid number default 0;
vdesname varchar2(50) default null;
vposname varchar2(100) default null;
vtemppos varchar2(100) default null;
skip number default 0;
vct number default 0;
vrun number default 0;

cursor empass is select id,designationid,position_id from eg_emp_assignment;

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
values
(vposname,egeis_position_seq.nextval,emp.designationid,sysdate);

dbms_output.put_line(vposname);

end loop;

end position_assignment;
/
CREATE OR REPLACE PROCEDURE SAL_BILLDETAIL
AS
V_BILLID	NUMBER DEFAULT 0;
V_GLCODEID	NUMBER DEFAULT 0;
V_DEBITAMOUNT	NUMBER DEFAULT 0;
V_CREDITAMOUNT	NUMBER DEFAULT 0;
v_cnt		NUMBER DEFAULT 0;

  CURSOR C1
	is
	SELECT BILLID,GLCODEID,DEBITAMOUNT,CREDITAMOUNT
	FROM
	SALARYBILLDETAIL,GENERALLEDGER
	WHERE SALARYBILLDETAIL.VOUCHERHEADERID=GENERALLEDGER.VOUCHERHEADERID;


BEGIN
	 dbms_output.put_line('Getting into the loop');

	 FOR SBD_CURSOR in C1
	 LOOP
		if SBD_CURSOR.DEBITAMOUNT>0 then
			  insert into EGP_SALARY_BILLDETAILS (ID,BILLID,GLCODEID,SALTYPE,AMOUNT,LASTMODIFIEDDATE)
			  	VALUES (seq_egp_salary_billdetails.nextval,SBD_CURSOR.BILLID,SBD_CURSOR.GLCODEID,'Earnings',SBD_CURSOR.DEBITAMOUNT,to_date('21-July-2007'));
			 end if;

			 if SBD_CURSOR.CREDITAMOUNT>0 then
			 	if SBD_CURSOR.GLCODEID<>842 then
			 	 insert into EGP_SALARY_BILLDETAILS (ID,BILLID,GLCODEID,SALTYPE,AMOUNT,LASTMODIFIEDDATE)
			 	 VALUES (seq_egp_salary_billdetails.nextval,SBD_CURSOR.BILLID,SBD_CURSOR.GLCODEID,'Deduction',SBD_CURSOR.CREDITAMOUNT,to_date('21-July-2007'));
			 	end if;
	 	 	end if;

		v_cnt:=v_cnt+1;
		dbms_output.put_line(v_cnt);

 	END LOOP;

	dbms_output.put_line ('Finished!!');
END;
/
CREATE OR REPLACE PROCEDURE Test2205 (tbname IN String, tbowner IN String)
IS
       V_SIZE  NUMBER;
       V_TOT_SIZE NUMBER := 0;
       V_COLUMN VARCHAR2(255);
       V_INIT_SIZE NUMBER;

       CURSOR COL_SIZE_CUR IS
         SELECT column_name
         FROM all_tab_columns
         WHERE table_name=UPPER(tbname)
         AND   owner = UPPER(tbowner);

         COL_SIZE_REC COL_SIZE_CUR%ROWTYPE;

       CURSOR AVG_COL_SIZE IS
         SELECT AVG(VSIZE(V_COLUMN))
         FROM (SELECT tbowner || '.' || tbname FROM dual);

BEGIN
        dbms_output.put_line(CHR(10));
        FOR COL_SIZE_REC IN COL_SIZE_CUR LOOP
            V_COLUMN := COL_SIZE_REC.COLUMN_NAME;
            OPEN AVG_COL_SIZE;
            FETCH AVG_COL_SIZE INTO V_SIZE;
            CLOSE AVG_COL_SIZE;
            dbms_output.put_line('Col. Name: '||V_COLUMN||'  '||' Ave. Size: '||V_SIZE);
            SELECT V_TOT_SIZE + V_SIZE INTO V_TOT_SIZE
FROM dual;  
         END LOOP;

       dbms_output.put_line('Average Table Rowsize is : '||V_TOT_SIZE||' bytes');

END;
/
CREATE OR REPLACE PROCEDURE Vouchemis_Update
AS
  v_VHID		 NUMBER;
  v_date	DATE;
  v_fs	  		NUMBER DEFAULT NULL;
 
 
  CURSOR C1
	IS
	SELECT * FROM VOUCHERHEADER
	WHERE ID NOT IN(SELECT voucherheaderid FROM VOUCHERMIS);		
	  
BEGIN
	 DBMS_OUTPUT.PUT_LINE('Getting into the loop');
	
	 FOR VOUCHERHD_cursor IN C1
	 LOOP
		 v_VHID := VOUCHERHD_cursor.ID;
	 	 SELECT cgdate,fundsourceid INTO v_date,v_fs FROM VOUCHERHEADER WHERE ID= v_VHID;
		
		 
  		 INSERT INTO VOUCHERMIS (ID,VOUCHERHEADERID,CREATETIMESTAMP,FUNDSOURCEID)
  		  VALUES
		   (seq_VOUCHERMIS.NEXTVAL,v_VHID,v_date,v_fs);
		 
		 DBMS_OUTPUT.PUT_LINE ('Successfully inserted into Vouchermis for ->'||v_VHID);
	END LOOP;	
	
	DBMS_OUTPUT.PUT_LINE ('Finished!!');
END;
/
CREATE OR REPLACE PROCEDURE VOUCHEMIS_UPDATE1
AS
  v_VHID		 NUMBER;
  v_date	DATE;
  v_fd	  		NUMBER DEFAULT NULL;
 
 
  CURSOR C1
	IS
	SELECT * FROM EGF_VOUCHERMIS
	WHERE VOUCHERHEADERID IN(SELECT voucherheaderid FROM VOUCHERMIS);		
	  
BEGIN
	 DBMS_OUTPUT.PUT_LINE('Getting into the loop');
	
	 FOR VOUCHERHD_cursor IN C1
	 LOOP
		 v_VHID := VOUCHERHD_cursor.VOUCHERHEADERID;
	 	 SELECT fieldid INTO v_fd FROM EGF_VOUCHERMIS WHERE VOUCHERHEADERID= v_VHID;
		
		 
  		UPDATE VOUCHERMIS SET divisionid=v_fd WHERE voucherheaderid=v_VHID AND divisionid IS NULL;
		 
		 DBMS_OUTPUT.PUT_LINE ('Successfully inserted into Vouchermis for ->'||v_VHID);
	END LOOP;	
	
	DBMS_OUTPUT.PUT_LINE ('Finished!!');
END;
/

