/* 15-4-2008: Changed the DETAILKEY function to pass key name and narration */

create or replace package EGOVCOMMON as
	FUNCTION NDMC_GENERATEVNUMBER (vFIELD number,VDATE date,FPERIODID number) return varchar2;
	FUNCTION GENERATECGN (FPERIODID number) return varchar2; 
	FUNCTION GENERATEFISCAL (vdate date) return number;
	FUNCTION NDMC_CREATEVOUCHERHEADER (vdate date,fperiodid number,vsegmentid number,vsubsegmentid number,vfieldid number,vsubfieldid number,vname varchar2,vtype varchar2,vdes varchar2,vdeptid number) return number;
	FUNCTION AP_CREATEVOUCHERHEADER (vdate date,fperiodid number,vfundid number,vnumber varchar2, vname varchar2,vtype varchar2,vcgntype varchar2,vcgvntype varchar2,vdeptid number,vdes varchar2) return number;
	FUNCTION AP_GENERATECGVN (FPERIODID number,vtype varchar2,vfundid number) return varchar2;
	FUNCTION AP_GENERATECGN (FPERIODID number,vtype varchar2) return varchar2;
	FUNCTION INSERT_DEPT(DEPTNAME varchar2) return number;
	FUNCTION DETAILKEY(vdettype varchar2,vcode varchar2,vname varchar2,vnarration varchar2) return number;
	FUNCTION CONTROLCODE(vglcode varchar2,vdettype varchar2) return number;
	FUNCTION GETNEXTCHEQUE(vbankacid number,vallottedto number) return varchar2;
	FUNCTION INSERTGL(VHID number, vLINEID number, vDATE date, vGLCODE varchar2, vdebitamount number,  vcreditamount number) return number;
	FUNCTION INSERTSUBLEDGER(vglid number, vglcode varchar2, vdetkey number, vdettype number, vamount number) return number;
	FUNCTION ACCOUNTDETTYPE(vdettype varchar2) return number;
	FUNCTION AVAILCHEQUECOUNT(vbankacid number,vallottedto number,vchcount number) return number;
	PROCEDURE ERRORINSERT(errmodule varchar2,errcategory varchar2,errsubcategory varchar2,errvalue varchar2,erridentifier varchar2,errmsg varchar2,errno varchar2);
	
end EGOVCOMMON;
/

create or replace package body EGOVCOMMON as

PROCEDURE ERRORINSERT (errmodule varchar2,errcategory varchar2,errsubcategory varchar2,errvalue varchar2,erridentifier varchar2,errmsg varchar2,errno varchar2) AS

begin
insert into errortable (ID,MODULE,CATEGORY,SUBCATEGORY,IDENTIFIER,VALUE,MESSAGE,ERRORNO,ERRORDATE)
values
(seq_errortable.nextval,errmodule,errcategory,errsubcategory,erridentifier,errvalue,errmsg,errno,to_date(sysdate));

end errorinsert;

FUNCTION GENERATEFISCAL (vdate date) return number as

fpid number default 0;

begin
	begin
	select fp.id into fpid
	from fiscalperiod fp, financialyear fy
	where fy.startingdate<=vdate and fy.endingdate>=vdate and
	fp.startingdate<=vdate and fp.endingdate>=vdate and
	fy.isactiveforposting=1 and fp.financialyearid=fy.id;
	exception
		WHEN NO_DATA_FOUND then
			dbms_output.put_line('Invalid Fiscal Period'||vdate);
			fpid:= -1;
	end;

return(fpid);
end GENERATEFISCAL;

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


FUNCTION NDMC_GENERATEVNUMBER (vFIELD number,VDATE date,FPERIODID number) return varchar2 as

vnumchar varchar2(20);
vfyear varchar2(4);
vmonchar varchar2(2);
vmth number default 0;
mustinsert number default 0;
begin
dbms_output.enable(100000000000000000000); 
	begin
		select lpad(to_char(VOUCHERNUMBER+1),5,'0') into vnumchar from eg_numbers where
		vouchertype=concat('DIV',vfield) and month=extract(month from vdate) and fiscialperiodid=FPERIODID;
	exception
		WHEN NO_DATA_FOUND then
		mustinsert:=1;
	end;
	
	 
	if length(extract(month from vdate))=1 then
		vmonchar:=concat('0',to_char(extract(month from vdate)));
	else
		vmonchar:=to_char(extract(month from vdate));
	end if;
	
	select concat(substr(FY.financialyear,3,2),substr(FY.financialyear,8,2)) into vfyear from financialyear FY, fiscalperiod FP
	where FP.id=FPERIODID and FP.financialyearid=FY.ID;
	
	if mustinsert=1 then
		insert into eg_numbers (ID,VOUCHERTYPE,VOUCHERNUMBER,MONTH,FISCIALPERIODID)
		values 
		(seq_egf_numbers.nextval,concat('DIV',vfield),1,extract(month from vdate),fperiodid);
		vnumchar:=lpad(to_char(1),5,'0');
 	else
 		update eg_numbers set vouchernumber=vouchernumber+1 
 		where vouchertype=concat('DIV',vfield) 
 		and month=extract(month from vdate) and fiscialperiodid=FPERIODID;
 	end if;
 dbms_output.put_line('inside vnumgen->'||vnumchar||':'||vfield||':'||vdate||':'||fperiodid); 
 dbms_output.put_line(concat(vfield,concat(vfyear,concat(vmonchar,vnumchar)))); 
 return(concat(vfield,concat(vfyear,concat(vmonchar,vnumchar))));
end NDMC_GENERATEVNUMBER;


FUNCTION GENERATECGN (FPERIODID number) return varchar2 as

vnumchar varchar2(20);
vfyear varchar2(4);
vmonchar varchar2(2);
vmth number default 0;
mustinsert number default 0;
begin
dbms_output.enable(100000000000000000000); 

	 dbms_output.put_line('inside cgn->'||vnumchar); 
	
	begin
		select lpad(to_char(VOUCHERNUMBER+1),8,'0') into vnumchar from eg_numbers where
		vouchertype like 'CGN%' and fiscialperiodid=fperiodid;
	
	exception
		WHEN NO_DATA_FOUND then /* generate a new series for the field for that fiscalperiod */
		mustinsert:=1;
		vnumchar:=lpad(to_char(1),8,0);
	end;
	
	/* year number */
	select concat(substr(FY.financialyear,3,2),substr(FY.financialyear,8,2)) into vfyear from financialyear FY, fiscalperiod FP
	where FP.id=FPERIODID and FP.financialyearid=FY.ID;
	
	if mustinsert=1 then
		insert into eg_numbers (ID,VOUCHERTYPE,VOUCHERNUMBER,FISCIALPERIODID) 
		values (seq_egf_numbers.nextval,concat('CGN',vfyear),1,fperiodid);		
 	else
 		update eg_numbers set vouchernumber=vouchernumber+1 
 		where vouchertype=concat('CGN',vfyear) and fiscialperiodid=FPERIODID;
 	end if;

dbms_output.put_line(concat(vfyear,vnumchar));
 
return concat(vfyear,vnumchar);
end GENERATECGN;


FUNCTION AP_GENERATECGN (FPERIODID number,vtype varchar2) return varchar2 as

vnumchar varchar2(20);
vfyear varchar2(4);
vmonchar varchar2(2);
vmth number default 0;
mustinsert number default 0;
maxcgn number default 0;

begin
dbms_output.enable(100000000000000000000); 

		begin
		select max(to_number(substr(cgn,4))) into maxcgn from voucherheader where substr(cgn,1,3)=vtype;
		exception
		WHEN NO_DATA_FOUND then
			maxcgn:=0;
		end;
		maxcgn:=maxcgn+1;

/* dbms_output.put_line('CGN:'||concat(vtype,to_char(maxcgn))); */

return (concat(vtype,to_char(maxcgn)));
end AP_GENERATECGN;


FUNCTION AP_GENERATECGVN (FPERIODID number,vtype varchar2,vfundid number) return varchar2 as

vnumchar varchar2(20);
mustinsert number default 0;
maxcgn number default 0;
retval varchar2(20);
vfid varchar2(2);

begin
dbms_output.enable(100000000000000000000); 
		begin
		select identifier into vfid from fund where id=vfundid;
		exception
			WHEN NO_DATA_FOUND THEN
				dbms_output.put_line('FUND IS INVALID'||vfundid);
				retval:=to_char(-1);
		end;
		
		dbms_output.put_line('type/fid:'||vtype||vfid);
		if to_number(retval)= -1 then
			return (-1);
		else
			begin
			select to_char(vouchernumber+1) into vnumchar from eg_numbers where
			vouchertype=concat(vfid,vtype) and fiscialperiodid=fperiodid;
			exception
				WHEN NO_DATA_FOUND then /* generate a new series for the field for that fiscalperiod */
				mustinsert:=1;
				vnumchar:=lpad(to_char(1),4,0);
			end;
			/* dbms_output.put_line('vnum:'||vnumchar); */
			if mustinsert=1 then
				insert into eg_numbers (ID,VOUCHERTYPE,VOUCHERNUMBER,FISCIALPERIODID) 
				values (seq_egf_numbers.nextval,concat(vfid,vtype),1,fperiodid);		
			else
				update eg_numbers set vouchernumber=vouchernumber+1 
				where vouchertype=concat(vfid,vtype) and fiscialperiodid=FPERIODID;
				
			 end if;
			 retval:=concat(vfid,concat(vtype,lpad(vnumchar,4,0)));
			/* dbms_output.put_line('CGVN :'||retval); */
 			 return (retval);
		end if;
 
			
end AP_GENERATECGVN;



FUNCTION AP_CREATEVOUCHERHEADER (vdate date,fperiodid number,vfundid number,vnumber varchar2, vname varchar2,vtype varchar2,vcgntype varchar2,vcgvntype varchar2,vdeptid number,vdes varchar2) return number
as

nextcgnum varchar2(50) default null;
nextcgvn varchar2(20) default null;
vherr number default 0;
vhid number default 0;

begin
vherr:=0;
dbms_output.enable(100000000000000000000); 
nextcgnum:=egovcommon.ap_generatecgn(FPERIODID,vcgntype); /* generate the next cgnum */
nextcgvn:=egovcommon.ap_generatecgvn(FPERIODID,vcgvntype,vfundid); /* generate the next cgvn */

		
insert into voucherheader 
(ID,CGN,CGDATE,NAME,TYPE,EFFECTIVEDATE,VOUCHERNUMBER,VOUCHERDATE,FUNDID,FISCALPERIODID,STATUS,ISCONFIRMED,CREATEDBY,CGVN,DESCRIPTION)
VALUES
(SEQ_VOUCHERHEADER.NEXTVAL,nextcgnum,vdate,vname,vtype,vdate,vnumber,vdate,
vfundid,fperiodid,0,0,2,nextcgvn,vdes);

begin
	select id into vhid from voucherheader where CGN=nextcgnum;
exception
	WHEN NO_DATA_FOUND then
		dbms_output.put_line('Insert into VoucherHeader Failed for VoucherNumber:'||vnumber);
		vherr:=1;
end;
/* dbms_output.put_line('Voucherheader: '||vhid||' Voucher Number: '||vnumber); */

if vherr=0 then
insert into vouchermis 
	(ID,VOUCHERHEADERID,CREATETIMESTAMP,DEPARTMENTID)
	values
	(SEQ_VOUCHERMIS.NEXTVAL,vhid,SYSDATE,VDEPTID);


insert into egf_record_status
	values
	(SEQ_EGF_RECORD_STATUS.NEXTVAL,vtype,0,SYSDATE,2,vhid);
end if; /*vherr*/

if vherr=1 then
	return (-1);
else
 	return (vhid);
end if;

end AP_CREATEVOUCHERHEADER;



function NDMC_CREATEVOUCHERHEADER (vdate date,fperiodid number,vsegmentid number,vsubsegmentid number,vfieldid number,vsubfieldid number,vname varchar2,vtype varchar2,vdes varchar2,vdeptid number) return number
as

vfield number default 0;
nextvnum varchar2(50) default null;
nextcgnum varchar2(50) default null;
vherr number default 0;
vhid number default 0;

begin
vherr:=0;

select bndry_num into vfield from eg_boundary where id_bndry=vfieldid and id_bndry_type=2;

nextvnum:=egovcommon.NDMC_GENERATEVNUMBER(vfield,vdate,FPERIODID); /* generate the next voucher num */
if (vtype='Payment' or vtype='Salary Payment') then nextvnum:=concat(nextvnum,'P');
elsif (vtype='Journal Voucher') then nextvnum:=concat(nextvnum,'J');
elsif ((vtype='Receipt') or (vtype='Receipts')) then nextvnum:=concat(nextvnum,'R');
elsif (vtype='Contra') then nextvnum:=concat(nextvnum,'C');
end if;

nextcgnum:=egovcommon.generatecgn(FPERIODID); /* generate the next cgnum */
dbms_output.put_line('INSERTING VNUMBER->'||nextvnum||':'||nextcgnum);
		
insert into voucherheader 
(ID,CGN,CGDATE,NAME,TYPE,EFFECTIVEDATE,VOUCHERNUMBER,VOUCHERDATE,FUNDID,FISCALPERIODID,STATUS,ISCONFIRMED,CREATEDBY,CGVN,DESCRIPTION,DEPARTMENTID)
VALUES
(SEQ_VOUCHERHEADER.NEXTVAL,nextcgnum,vdate,vname,vtype,sysdate,nextvnum,vdate,
vsegmentid,fperiodid,0,0,2,nextvnum,vdes,vdeptid);

begin
	select id into vhid from voucherheader where CGN=nextcgnum;
exception
	WHEN NO_DATA_FOUND then
		dbms_output.put_line('Insert into VoucherHeader Failed for VoucherNumber:'||nextvnum);
		vherr:=1;
end;
dbms_output.put_line('Voucherheader'||vhid);

if vherr=0 then
	insert into vouchermis 
	(ID,DIVISIONID,VOUCHERHEADERID,SEGMENTID,SUB_SEGMENTID,CREATETIMESTAMP,DEPARTMENTID)
	values
	(SEQ_VOUCHERMIS.NEXTVAL,vSUBFIELDID,vhid,vsegmentid,vsubsegmentid,SYSDATE,1);
	insert into egf_record_status
	values
	(SEQ_EGF_RECORD_STATUS.NEXTVAL,vtype,0,SYSDATE,2,vhid);
end if; /*vherr*/

if vherr=1 then
	return (-1);
else
 	return (vhid);
end if;

end NDMC_CREATEVOUCHERHEADER;


FUNCTION INSERT_DEPT(DEPTNAME varchar2) return number as
deptcode varchar2(5);
retval number;

begin

dbms_output.enable(100000000000000000000); 
/* deptcode:=substr(deptname,1,3); */
/* dbms_output.put_line('Inserted New Department:Code: '||deptcode||' Name '||deptname||' ID: '||retval); */

insert into eg_department (ID_DEPT,DEPT_NAME,DEPT_DETAILS,UPDATETIME)
		values(seq_eg_dept.nextval,DEPTNAME,DEPTNAME,sysdate);

select id_dept into retval from eg_department where DEPT_NAME=DEPTNAME;

/* dbms_output.put_line('Inserted New Department:Code: '||deptcode||' Name '||deptname||' ID: '||retval); */

return (retval);
end INSERT_DEPT;


FUNCTION DETAILKEY(vdettype varchar2,vcode varchar2,vname varchar2,vnarration varchar2) return number as
retval number;
acdettype number;
acdetname varchar2(50);
 
begin
dbms_output.enable(100000000000000000000); 
/* dbms_output.put_line('Inside Detailkey'); */
select id,name into acdettype,acdetname from accountdetailtype where upper(name)=upper(vdettype);
if upper(vdettype)=upper('Creditor') then
	begin
	select akey.detailkey into retval 
	from accountdetailkey akey, relation rl
	where rl.code=vcode and akey.detailkey=rl.id and
	akey.detailtypeid=acdettype;
	exception WHEN NO_DATA_FOUND then
		dbms_output.put_line('Creditor code Not Found'||vcode);
		retval:= -1;
	end;
elsif upper(vdettype)=upper('Employee') then
	/* dbms_output.put_line('Employee Code:'||vcode); */
	begin
	select akey.detailkey into retval 
	from accountdetailkey akey, eg_employee egemp
	where egemp.code=vcode and akey.detailkey=egemp.id and
	akey.detailtypeid=acdettype;
	exception WHEN NO_DATA_FOUND then
			dbms_output.put_line('Inserting Code:'||vcode);
			insert into eg_employee(ID,CODE,EMP_FIRSTNAME,ISACTIVE)
			values (seq_eg_employee.nextval,vcode,vname,1);
			select max(id) into retval from eg_employee; 
			dbms_output.put_line('Inserting ID:'||retval);
			insert into accountdetailkey (id,groupid,detailtypeid,detailname,detailkey)
			values(seq_accountdetailkey.nextval,1,acdettype,'Employee_id',retval);
	end;
else
	begin
	select akey.detailkey into retval 
	from accountdetailkey akey, accountentitymaster aem
	where aem.code=vcode and akey.detailkey=aem.id and
	akey.detailtypeid=acdettype;
	exception WHEN NO_DATA_FOUND then
			dbms_output.put_line('Inserting Code:'||vcode);
			insert into accountentitymaster(ID,CODE,NAME,DETAILTYPEID,NARRATION,ISACTIVE,LASTMODIFIED,CREATED)
			values (seq_accountentitymaster.nextval,vcode,vname,acdettype,vnarration,1,sysdate,sysdate);
			select max(id) into retval from accountentitymaster;
			dbms_output.put_line('Inserting ID:'||retval);
			insert into accountdetailkey (id,groupid,detailtypeid,detailname,detailkey)
			values(seq_accountdetailkey.nextval,1,acdettype,acdetname,retval);
	end;
end if;

return (retval);

end DETAILKEY;

FUNCTION ACCOUNTDETTYPE(vdettype varchar2) return number as

retval number default 0;
begin

begin
select id into retval from accountdetailtype where name=vdettype;
exception when no_data_found then
	insert into ACCOUNTDETAILTYPE(id,name,description,tablename,columnname,attributename,nbroflevels,isactive,created,lastmodified)
	values(seq_accountdetailtype.nextval,vdettype,vdettype,'accountEntityMaster','id',concat(vdettype,'_id'),1,1,sysdate,sysdate);
	select max(id) into retval from accountdetailtype;
end;

return(retval);
	
end ACCOUNTDETTYPE;


FUNCTION CONTROLCODE(vglcode varchar2,vdettype varchar2) return number as
retval number default 1;
acdettype number;
glid number default 0;
vdetailtypeid number default 0;

begin

dbms_output.enable(100000000000000000000); 
select id into vdetailtypeid from accountdetailtype where upper(name)=upper(vdettype);
begin
select cad.detailtypeid into vdetailtypeid from chartofaccountdetail cad,chartofaccounts ca
where cad.glcodeid=ca.id and ca.glcode=vglcode and cad.detailtypeid=vdetailtypeid;
exception
when NO_DATA_FOUND then
	select id into glid from chartofaccounts where glcode=vglcode;
	dbms_output.put_line('inserting COD for:'||vglcode||':ID'||glid||' DetailtypeID: '||vdetailtypeid);
	insert into chartofaccountdetail(ID,GLCODEID,DETAILTYPEID,ISCONTROLCODE)
	values(seq_chartofaccountdetail.nextval,glid,vdetailtypeid,1);
	retval:=0;
end;
if vdetailtypeid>0 then
	retval:=0;
end if;

return (retval);

end CONTROLCODE;

FUNCTION GETNEXTCHEQUE(vbankacid number,vallottedto number) return varchar2 as

nextcheque varchar2(20);
tocheque varchar2(20);
frcheque varchar2(20);
avail number default 0;
cid number default 0;

begin

begin
select id, nextchqno,tochequenumber,fromchequenumber into cid,nextcheque,tocheque,frcheque
from egf_account_cheques where id=(select min(id) from  egf_account_cheques where 
bankaccountid=vbankacid and allotedto=vallottedto and isexhausted=0 and
nvl(nextchqno,' ')<=tochequenumber);
exception when no_data_found then
	avail:=-1;
end;

if avail=0 then
	if (nextcheque=tocheque) then
		update egf_account_cheques set nextchqno=tocheque, isexhausted=1 where id=cid;
	elsif (nextcheque<tocheque) and (nextcheque is not null) then
		update egf_account_cheques set nextchqno=nextchqno+1 where id=cid;
	elsif (nextcheque is null) then
		nextcheque:=frcheque;
		update egf_account_cheques set nextchqno=frcheque where id=cid;
	end if;
	dbms_output.put_line(cid||'-'||nextcheque||'-'||tocheque);
	return(nextcheque);
else
	return (-1);
end if;
	
end GETNEXTCHEQUE;
 	
FUNCTION AVAILCHEQUECOUNT(vbankacid number,vallottedto number,vchcount number) return number as

cursor chqrange is 
select ega.id as egaid,
ega.fromchequenumber as egafrom,ega.tochequenumber as egato,nvl(ega.nextchqno,0) as maxchq, ega.isexhausted as isexhaus
from  egf_account_cheques ega
where
ega.bankaccountid=vbankacid and ega.allotedto=vallottedto and ega.isexhausted=0 and nvl(ega.nextchqno,' ')<=ega.tochequenumber
order by egaid asc;

availchqs number default 0;
avail number default 0;

begin

for chq in chqrange loop
	if chq.maxchq=0 then
		availchqs:=availchqs+(chq.egato-chq.egafrom)+1;
	else
 		availchqs:=availchqs+(chq.egato-chq.maxchq)+1;
 	end if;
end loop;

if (availchqs>=vchcount) then
	dbms_output.put_line('Required Cheques:'||vchcount||': Available Cheques:'||availchqs);
	return availchqs;
else
	dbms_output.put_line('CHEQUES NOT AVAILABLE: Required Cheques:'||vchcount||': Available Cheques:'||availchqs);
	return (availchqs-vchcount);
end if;

end AVAILCHEQUECOUNT;


function INSERTGL(VHID number, vLINEID number, vDATE date, vGLCODE varchar2, vdebitamount number,  vcreditamount number) return number as

vdetailid number default 0;
vglid number default 0;
vglname varchar2(100) default NULL;
retval number default 1;

begin

begin
select id,name into vglid,vglname from chartofaccounts where glcode=vGLCODE;
exception
	when no_data_found then
		dbms_output.put_line('GLCODE not found'||vGLCODE);
		retval:= -1;
end;

if retval>-1 then
	insert into voucherdetail 
	(ID,LINEID,VOUCHERHEADERID,GLCODE,ACCOUNTNAME,DEBITAMOUNT,CREDITAMOUNT)
	values
	(seq_voucherdetail.nextval,vLINEID,VHID,vGLCODE,vglname,vdebitamount,vcreditamount);

	select max(id) into vdetailid from voucherdetail where voucherheaderid=VHID and glcode=vGLCODE;

	/* insert into generalledger */
	begin
	insert into generalledger
	(ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,voucherheaderid)
	values
	(seq_generalledger.nextval,vdetailid,vdate,vglid,vGLCODE,vdebitamount,vcreditamount,vhid);
	exception
	when others then
	     dbms_output.put_line(substr(SQLERRM,1,200));
	     retval:=-1;
	end;
	if retval>-1 then
		select max(id) into retval from generalledger where voucherheaderid=vhid and glcode=vGLCODE;
		dbms_output.put_line('Inserted GL:'||retval);
	end if;
end if;

return (retval);

end insertgl;

function insertsubledger (vglid number, vglcode varchar2, vdetkey number, vdettype number, vamount number) return number as 

vdettype1 number default 0;
vgldtlid number default 0;
retval number default 0;
begin

select cod.detailtypeid into vdettype1 from chartofaccountdetail cod, chartofaccounts coa where coa.id=cod.glcodeid and coa.glcode=vglcode;

if vdettype=vdettype1 then
	insert into generalledgerdetail (id,generalledgerid,detailkeyid,detailtypeid,amount)
	values (seq_generalledgerdetail.nextval,vglid,vdetkey,vdettype,vamount);
	
	select max(id) into vgldtlid from generalledgerdetail where generalledgerid=vglid and detailkeyid=vdetkey;
	
	insert into eg_remittance_gldtl (id,gldtlid,gldtlamt,lastmodifieddate,remittedamt)
	values (seq_eg_remittance_gldtl.nextval,vgldtlid,vamount,sysdate,0);
	retval:=1;
	dbms_output.put_line('Inserted Subledger:'||vgldtlid);
else
	retval:=-1;
end if;

return (retval);
end insertsubledger;


end EGOVCOMMON;
/

