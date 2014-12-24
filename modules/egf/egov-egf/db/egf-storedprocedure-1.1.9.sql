CREATE OR REPLACE package EGF_REPORT is

TYPE vGetResultSet is REF CURSOR;

function RCPPYMNT(vfromdt varchar2,vtodt varchar2,vcash varchar2,vminor number) return egf_report.vGetResultSet;
function INCOMEEXP(vfromdt date,vtodt date,vminor number,vdiv number) return egf_report.vGetResultSet;
function BALANCESHEET(vtodt date,vmajor number,vminor number,vdiv number) return egf_report.vGetResultSet;
function ISACTIVEFUND(vfundid number,vfromdt date,vtodt date) return number;
function LASTYEARDATE(vdt date) return date;
function FIRSTDATE(vdt date) return date;
function FINANCIALYEAR(vdt date) return number;
function EXCESSIE(vfromdt date,vtodt date,vfundid number,vdiv number) return number;
function IESCHEDULE(vfromdt date,vtodt date,vminor number,vdiv number,vscheduleid number) return egf_report.vGetResultSet;
function BSSCHEDULE(vtodt date,vminor number,vdiv number,vscheduleid number) return egf_report.vGetResultSet;
function SCHEMEREPORT(pschemeid number,vfromdt date,vtodt date,vmajor number,filterGlcode varchar2) return egf_report.vGetResultSet;
function OPBALSCHEMEREPORT(pschemeid number,vfromdt date,vmajor number,filterGlcode varchar2) return egf_report.vGetResultSet;

end EGF_REPORT;
/

CREATE OR REPLACE package body EGF_REPORT as

function ISACTIVEFUND(vfundid number,vfromdt date,vtodt date) return number as
retval number default 0;
begin
begin
select count(*) into retval from voucherheader where fundid=vfundid and voucherdate>=vfromdt and voucherdate<=vtodt and status<>4;
exception when no_data_found then
	retval:= 0;
end;

return (retval);
end isactivefund;

function LASTYEARDATE(vdt date) return date is
retdate date;
begin
select to_date(add_months(to_date(vdt),-12)) into retdate from dual;
return (retdate);
end LASTYEARDATE;

function FIRSTDATE(vdt date) return date is
retdate date;
begin
select startingdate into retdate from financialyear where startingdate<=vdt and endingdate>=vdt;
return (retdate);
end FIRSTDATE;

function FINANCIALYEAR(vdt date) return number is
retval number default 0;
begin
select id into retval from financialyear where startingdate<=vdt and endingdate>=vdt;
return retval;
end FINANCIALYEAR;

function EXCESSIE(vfromdt date,vtodt date,vfundid number,vdiv number) return number is
retval number default 0;
begin

begin
select sum(gl.debitamount)-sum(gl.creditamount) into retval
 from voucherheader vh,generalledger gl, chartofaccounts coa
 where vh.ID=gl.VOUCHERHEADERID and vh.status<>4 and
 vh.voucherdate>=to_date(vfromdt) and vh.voucherdate<=to_date(vtodt)
 and vh.fundid=vfundid and coa.ID=gl.glcodeid and coa.type in ('I','E');
exception
when no_data_found then
	retval:=0;
end;

if retval is null then
	retval:=0;
end if;
return round((retval/vdiv),2);
dbms_output.put_line(retval);
end EXCESSIE;



function RCPPYMNT(vfromdt varchar2,vtodt varchar2,vcash varchar2,vminor number) return egf_report.vGetResultSet is


vRSoutput vGetResultSet;
query_part1 varchar2(1000);
query_rpt0 long;
query_pmt0 long;
query_rpt varchar2(10000);
query_pmt varchar2(10000);
query_last varchar2(1000);
query_final varchar2(32000);
rpt_str1 varchar2(5);
pmt_str1 varchar2(5);
rpt_str2 varchar2(256);
pmt_str2 varchar2(256);
c1 number;
rc1 number;

cursor fund_c is select distinct id,name from fund;

begin

dbms_output.enable(1000000000000000000000000);
c1:=dbms_sql.open_cursor;
rpt_str1:='R';
rpt_str2:=concat('Receipt'',','''Receipts');
pmt_str1:='P';
pmt_str2:=concat('Payment'',',concat('''SubledgerPayment'',','''Subledger'));
query_part1:= 'select distinct sm.schedule,sm.schedulename,coa.receiptscheduleid,coa.paymentscheduleid,';

query_rpt0:='(
select decode(sm.repsubtype,'||'''ROP'''||',sum(gl.creditamount),'||'''RNOP'''||',sum(gl.creditamount),0) from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.receiptscheduleid=coa.RECEIPTSCHEDULEID
and vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')'||' and vh.id in
(select distinct vh.id from voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and vh.status<>4 and gl.glcode like ('''||vcash||'%'') and gl.debitamount>0)';

query_pmt0:='(
select decode(sm.repsubtype, '||'''POP'''||',sum(gl.debitamount),'||'''PNOP'''||',sum(gl.debitamount),0) from generalledger gl, voucherheader vh,chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.paymentscheduleid=coa.PAYMENTSCHEDULEID
and vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')'||' and vh.id in
(select distinct vh.id from voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and vh.status<>4 and gl.glcode like ('''||vcash||'%'') and gl.creditamount>0)';


query_last:=' sm.repsubtype from schedulemapping sm, chartofaccounts coa, chartofaccounts coa1
where sm.id in (coa.receiptscheduleid,coa.paymentscheduleid) and coa.parentid=coa1.id and coa1.glcode not like ('''||vcash||'%'')
group by sm.schedule,sm.schedulename,coa.receiptscheduleid,coa.paymentscheduleid,sm.repsubtype ORDER BY sm.repsubtype DESC, sm.schedule';

for fc in fund_c loop
   if (isactivefund(fc.id,vfromdt,vtodt)>0) then
	query_rpt:=query_rpt||query_rpt0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
	/* print_out(query_rpt); */
	query_pmt:=query_pmt||query_pmt0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
	/* print_out(query_pmt); */
   end if;
end loop;

query_final:=query_part1||query_rpt||query_pmt||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end RCPPYMNT;


function INCOMEEXP(vfromdt date,vtodt date,vminor number,vdiv number) return egf_report.vGetResultSet is

vRSoutput vGetResultSet;

query_part1 varchar2(1000);
query_IncExp0 long;
query_SumCurrentYear long;
query_SumPreviousYear long;
query_IncExp long;
query_Expenditure varchar2(10000);
query_Income varchar2(10000);
query_last varchar2(1000);
query_final varchar2(30000);
c1 number;
rc1 number;
prevfrom date;
prevto date;

cursor fund_c is select distinct id,name from fund;

begin

dbms_output.enable(1000000);

prevfrom:=egf_report.LASTYEARDATE(to_date(vfromdt));
prevto:=egf_report.LASTYEARDATE(to_date(vtodt));
c1:=dbms_sql.open_cursor;
query_part1:= 'select distinct substr(coa1.glcode,1,'||vminor||'),sm.schedule,sm.schedulename,';

query_IncExp0:='(
select decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')';

query_SumCurrentYear:='(
select decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')
and vh.status<>4) as "'||concat('Sum Current Year','",');

query_SumPreviousYear:='(
select decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||prevfrom||''')'||' and vh.voucherdate<='||'to_date('''||prevto||''')
and vh.status<>4) as "'||concat('Sum Previous Year','",');

query_last:='coa.scheduleid,coa.type from chartofaccounts coa, chartofaccounts coa1,schedulemapping sm
where sm.id in (coa.scheduleid) and coa.parentid=coa1.id and sm.reporttype=''IE''
group by sm.schedule,sm.schedulename,coa1.glcode,coa.scheduleid,coa.type order by coa.type desc,sm.schedule asc';



for fc in fund_c loop
if ((isactivefund(fc.id,vfromdt,vtodt)>0) or (isactivefund(fc.id,prevfrom,prevto)>0)) then
	query_IncExp:=query_IncExp||query_IncExp0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
end if;

end loop;

query_final:=query_part1||query_IncExp||query_SumCurrentYear||query_SumPreviousYear||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end INCOMEEXP;




function BALANCESHEET(vtodt date,vmajor number,vminor number,vdiv number) return egf_report.vGetResultSet is

vBSoutput vGetResultSet;
query_part0 varchar2(100);
query_part1 long;
query_OBSpart long;
query_TRANSpart long;
query_finalpart long;
query_OBSprev long;
query_TRANSprev long;
query_OBSTOTALcurr long;
query_TRANSTOTALcurr long;
query_final varchar2(30000);
c1 number;
rc1 number;
prevfrom date;
prevto date;
vfromdt date;
finyear number;
prevfinyear number;
vexcessie number;
vprevexcessie_comp number;
vprevexcessie number default 0;
vcurrexcessie number default 0;

cursor fund_c is select distinct id,code,name from fund where id in (Select distinct fundid from voucherheader);

begin

dbms_output.enable(1000000);
vfromdt:=egf_report.FIRSTDATE(to_date(vtodt));
prevto:=egf_report.LASTYEARDATE(to_date(vtodt));
prevfrom:=egf_report.FIRSTDATE(to_date(prevto));
finyear:=egf_report.FINANCIALYEAR(to_date(vtodt));
prevfinyear:=egf_report.FINANCIALYEAR(to_date(prevto));

c1:=dbms_sql.open_cursor;


query_part0:='select MAJORCODE,SCHEDULEID,SCHEDULE,SCHEDULENAME,SUBSCHEDULENAME,SUBSCHEDULETYPE,';
query_OBSpart:='COATYPE from (select substr(coa.glcode,1,'||vmajor||') MAJORCODE,sm.id SCHEDULEID,sm.schedule SCHEDULE,sm.schedulename SCHEDULENAME,';
query_part1:=' decode(COATYPE,''L'',((sum(CURROBSTOTAL)+sum(CURRTRANSTOTAL))*(-1)),''A'',(sum(CURROBSTOTAL)+sum(CURRTRANSTOTAL))) as "Current Year Total",
               decode(COATYPE,''L'',((sum(PREVOPGBAL)+sum(PREVTRANSBAL))*(-1)),''A'',(sum(PREVOPGBAL)+sum(PREVTRANSBAL))) as "Previous Balance", ';

for fc in fund_c loop
	if ((isactivefund(fc.id,vfromdt,vtodt)>0) or (isactivefund(fc.id,prevfrom,prevto)>0)) then
		vexcessie:=egf_report.excessie(vfromdt,to_date(vtodt),fc.id,vdiv);
		vprevexcessie_comp:=egf_report.excessie(prevfrom,prevto,fc.id,vdiv);
		vcurrexcessie:=vcurrexcessie+vexcessie;
		vprevexcessie:=vprevexcessie+vprevexcessie_comp;

		query_part1:=query_part1||'decode(COATYPE,''L'', ((sum(OPGBAL'||fc.code||')+sum(TRANSBAL'||fc.code||'))*(-1)),''A'', (sum(OPGBAL'||fc.code||')+sum(TRANSBAL'||fc.code||'))) as "'||concat(rtrim(fc.name),'",');
		query_OBSpart:=query_OBSpart||
		' nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
		where ts.glcodeid=coa.id and ts.fundid='||fc.id||' and ts.financialyearid='||finyear||' group by glcodeid),0) as OPGBAL'||fc.code||',';
		query_TRANSpart:=query_TRANSpart||
		' decode(coa.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
		generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.fundid='||fc.id||' and vh.status<>4
		and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
		group by gl.glcodeid),0)+('||vexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
		generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.fundid='||fc.id||' and vh.status<>4
		and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
		group by gl.glcodeid),0)) as TRANSBAL'||fc.code||' ,';
	end if;
end loop;

query_OBSprev:='  nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
	where ts.glcodeid=coa.id and ts.financialyearid='||prevfinyear||' group by glcodeid),0) as PREVOPGBAL,';
query_TRANSprev:='  decode(coa.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||prevto||''') and vh.voucherdate>=to_date('''||prevfrom||''')
	group by gl.glcodeid),0)+('||vprevexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||prevto||''') and vh.voucherdate>=to_date('''||prevfrom||''')
	group by gl.glcodeid),0)) as PREVTRANSBAL, ';

query_OBSTOTALcurr:='  nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
	where ts.glcodeid=coa.id and ts.financialyearid='||finyear||' group by glcodeid),0) as CURROBSTOTAL,';
query_TRANSTOTALcurr:='  decode(coa.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by gl.glcodeid),0)+('||vcurrexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by gl.glcodeid),0)) as CURRTRANSTOTAL, ';

query_finalpart:=' coa.type COATYPE,subs.subschedulename SUBSCHEDULENAME, sm.repsubtype SUBSCHEDULETYPE from chartofaccounts coa,chartofaccounts coa1, schedulemapping sm, egf_subschedule subs
where substr(coa.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid in (sm.id) and sm.reporttype=''BS'' and coa.type in (''A'',''L'') and subs.subschname=sm.repsubtype
) group by MAJORCODE,SCHEDULEID,SCHEDULE,SCHEDULENAME,COATYPE,SUBSCHEDULENAME,SUBSCHEDULETYPE
order by majorcode asc';

query_final:=query_part0||query_part1||query_OBSpart||query_TRANSpart||query_OBSTOTALcurr||query_TRANSTOTALcurr||query_OBSprev||query_TRANSprev||query_finalpart;

print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vBSoutput for query_final;
dbms_sql.close_cursor(c1);
return vBSoutput;

end BALANCESHEET;


function IESCHEDULE(vfromdt date,vtodt date,vminor number,vdiv number,vscheduleid number) return egf_report.vGetResultSet is

vRSoutput vGetResultSet;

query_part1 varchar2(1000);
query_IncExp0 long;
query_SumCurrentYear long;
query_SumPreviousYear long;
query_IncExp long;
query_Expenditure varchar2(10000);
query_Income varchar2(10000);
query_last varchar2(1000);
query_final varchar2(30000);
c1 number;
rc1 number;
prevfrom date;
prevto date;

cursor fund_c is select distinct id,name from fund where id in (Select distinct fundid from voucherheader);

begin

dbms_output.enable(1000000);

prevfrom:=egf_report.LASTYEARDATE(to_date(vfromdt));
prevto:=egf_report.LASTYEARDATE(to_date(vtodt));
c1:=dbms_sql.open_cursor;


query_part1:= 'select distinct coa.glcode as "Code", decode(coa.operation,''A'',coa.name,''L'',concat(''Less:'',coa.name)) as "Head of Account", ';

query_IncExp0:='(
select round(nvl(decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||'),0),2)
from generalledger gl, voucherheader vh
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa.glcode and vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')';

query_SumCurrentYear:='(
select round(nvl(decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||'),0),2)
from generalledger gl, voucherheader vh
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa.glcode and vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')
and vh.status<>4) as "'||concat('Sum Current Year','",');

query_SumPreviousYear:='(
select round(nvl(decode(coa.type,''I'',(sum(gl.creditamount)-sum(gl.debitamount))/'||vdiv||',''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||'),0),2)
from generalledger gl, voucherheader vh
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa.glcode and
vh.voucherdate>='||'to_date('''||prevfrom||''')'||' and vh.voucherdate<='||'to_date('''||prevto||''')
and vh.status<>4) as "'||concat('Sum Previous Year','" ');

query_last:='from chartofaccounts coa where coa.scheduleid='||vscheduleid||' group by coa.glcode,coa.name,coa.operation,coa.type,coa.scheduleid order by coa.glcode asc';



for fc in fund_c loop
	query_IncExp:=query_IncExp||query_IncExp0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');

end loop;

query_final:=query_part1||query_IncExp||query_SumCurrentYear||query_SumPreviousYear||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end IESCHEDULE;



function BSSCHEDULE(vtodt date,vminor number,vdiv number,vscheduleid number) return egf_report.vGetResultSet is

vBSoutput vGetResultSet;
query_part0 varchar2(100);
query_part1 long;
query_OBSpart long;
query_TRANSpart long;
query_finalpart long;
query_OBSprev long;
query_TRANSprev long;
query_OBSTOTALcurr long;
query_TRANSTOTALcurr long;
query_final varchar2(30000);
c1 number;
rc1 number;
prevfrom date;
prevto date;
vfromdt date;
finyear number;
prevfinyear number;
vexcessie number;
vprevexcessie number default 0;
vcurrexcessie number default 0;
vprevexcessie_comp number default 0;

cursor fund_c is select distinct id,code,name from fund where id in (Select distinct fundid from voucherheader);

begin

dbms_output.enable(1000000);

vfromdt:=egf_report.FIRSTDATE(to_date(vtodt));
prevto:=egf_report.LASTYEARDATE(to_date(vtodt));
prevfrom:=egf_report.FIRSTDATE(to_date(prevto));
finyear:=egf_report.FINANCIALYEAR(to_date(vtodt));
prevfinyear:=egf_report.FINANCIALYEAR(to_date(prevto));

c1:=dbms_sql.open_cursor;


query_part0:='select MINORCODE,MINORNAME,';
query_OBSpart:='COATYPE from (select coa.glcode MINORCODE,coa.name as MINORNAME,';
query_part1:=' decode(COATYPE,''L'',((sum(CURROBSTOTAL)+sum(CURRTRANSTOTAL))*(-1)),''A'',(sum(CURROBSTOTAL)+sum(CURRTRANSTOTAL))) as "Current Year Total", decode(COATYPE,''L'',((sum(PREVOPGBAL)+sum(PREVTRANSBAL))*(-1)),''A'',(sum(PREVOPGBAL)+sum(PREVTRANSBAL))) as "Previous Balance", ';

for fc in fund_c loop
	vexcessie:=egf_report.excessie(vfromdt,vtodt,fc.id,vdiv);
	vcurrexcessie:=vcurrexcessie+vexcessie;
	vprevexcessie_comp:=egf_report.excessie(prevfrom,prevto,fc.id,vdiv);
	vprevexcessie:=vprevexcessie+vprevexcessie_comp;

	query_part1:=query_part1||'decode(COATYPE,''L'', ((sum(OPGBAL'||fc.code||')+sum(TRANSBAL'||fc.code||'))*(-1)),''A'', (sum(OPGBAL'||fc.code||')+sum(TRANSBAL'||fc.code||'))) as "'||concat(rtrim(fc.name),'",');
	query_OBSpart:=query_OBSpart||
	' nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
	where ts.glcodeid=coa1.id and ts.fundid='||fc.id||' and ts.financialyearid='||finyear||' group by substr(coa1.glcode,1,'||vminor||')),0) as OPGBAL'||fc.code||',';
	query_TRANSpart:=query_TRANSpart||
	' decode(coa1.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.fundid='||fc.id||' and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)+('||vexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.fundid='||fc.id||' and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)) as TRANSBAL'||fc.code||' ,';
end loop;

query_OBSprev:='  nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
	where ts.glcodeid=coa1.id and ts.financialyearid='||prevfinyear||' group by substr(coa1.glcode,1,'||vminor||')),0) as PREVOPGBAL,';
query_TRANSprev:='  decode(coa1.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||prevto||''') and vh.voucherdate>=to_date('''||prevfrom||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)+('||vprevexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||prevto||''') and vh.voucherdate>=to_date('''||prevfrom||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)) as PREVTRANSBAL, ';

query_OBSTOTALcurr:='  nvl((select round((sum(openingdebitbalance)-sum(openingcreditbalance))/'||vdiv||',2) from transactionsummary ts
	where ts.glcodeid=coa1.id and ts.financialyearid='||finyear||' group by substr(coa1.glcode,1,'||vminor||')),0) as CURROBSTOTAL,';
query_TRANSTOTALcurr:='  decode(coa1.purposeid,7,(nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)+('||vcurrexcessie||')), nvl((select round((sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||',2) from
	generalledger gl,voucherheader vh where gl.glcodeid=coa1.id and gl.voucherheaderid=vh.id and vh.status<>4
	and vh.VOUCHERDATE<=to_date('''||vtodt||''') and vh.voucherdate>=to_date('''||vfromdt||''')
	group by substr(coa1.glcode,1,'||vminor||')),0)) as CURRTRANSTOTAL, ';

query_finalpart:=' coa.type COATYPE from chartofaccounts coa,chartofaccounts coa1
where coa.scheduleid='||vscheduleid||' and substr(coa1.glcode,1,'||vminor||')=coa.glcode) group by MINORCODE,MINORNAME,COATYPE
order by MINORCODE asc';

query_final:=query_part0||query_part1||query_OBSpart||query_TRANSpart||query_OBSTOTALcurr||query_TRANSTOTALcurr||query_OBSprev||query_TRANSprev||query_finalpart;

print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vBSoutput for query_final;
dbms_sql.close_cursor(c1);
return vBSoutput;

end BSSCHEDULE;


function SCHEMEREPORT(pschemeid number,vfromdt date,vtodt date,vmajor number,filterGlcode varchar2) return egf_report.vGetResultSet is

vRSoutput vGetResultSet;

query_part1 varchar2(1000);
query_SchemeRpt0 long;
query_SchemeRpt long;
query_last varchar2(1000);
query_final varchar2(30000);
c1 number;

cursor subscheme_c is SELECT distinct id,name FROM SUB_SCHEME WHERE schemeid= pschemeid order by name;

begin

dbms_output.enable(1000000);

c1:=dbms_sql.open_cursor;
query_part1:= 'SELECT DISTINCT SUBSTR(coa1.glcode,1,'||vmajor||') MAJORCODE,(RTRIM(SUBSTR(coa1.name,1,30))) COANAME,';

query_SchemeRpt0:='(
SELECT  DECODE(coa1.TYPE,
''L'',(SUM(gl.creditamount)-SUM(gl.debitamount)),
''I'',(SUM(gl.creditamount)-SUM(gl.debitamount)),
''E'',(SUM(gl.debitamount)-SUM(gl.creditamount)),
''A'',(SUM(gl.debitamount)-SUM(gl.creditamount))
)
FROM generalledger gl, voucherheader vh,VOUCHERMIS vmis,chartofaccounts coa2,scheme s1
WHERE  vh.ID=vmis.voucherheaderid AND vh.ID=gl.voucherheaderid
AND vh.status<>4 AND vmis.SCHEMEID='||pschemeid||' 
AND SUBSTR(coa2.glcode,1,'||vmajor||') NOT IN ('||filterGlcode||')
AND SUBSTR(gl.glcode,1,'||vmajor||')=coa2.glcode
AND coa2.id=coa1.id
AND vmis.schemeid=s1.id
and vh.voucherdate>='||'to_date('''||vfromdt||''')'||'
and vh.voucherdate<='||'to_date('''||vtodt||''')';

query_last:='coa1.TYPE CTYPE,(DECODE(coa1.TYPE,
''L'',(SUM(gl1.creditamount)-SUM(gl1.debitamount)),
''I'',(SUM(gl1.creditamount)-SUM(gl1.debitamount)),
''E'',(SUM(gl1.debitamount)-SUM(gl1.creditamount)),
''A'',(SUM(gl1.debitamount)-SUM(gl1.creditamount))
)) AS SCHAMT
 
FROM chartofaccounts coa1,generalledger gl1,voucherheader vh1,
vouchermis vmis1,scheme s2
WHERE
vh1.ID=vmis1.voucherheaderid AND vh1.id=gl1.voucherheaderid
AND vh1.status<>4 AND vmis1.schemeid='||pschemeid||' 
AND vmis1.schemeid=s2.id
AND SUBSTR(coa1.glcode,1,'||vmajor||') NOT IN ('||filterGlcode||')
AND SUBSTR(gl1.glcode,1,'||vmajor||')=coa1.glcode

AND vh1.voucherdate>='||'TO_DATE('''||vfromdt||''')'||'
AND vh1.voucherdate<='||'TO_DATE('''||vtodt||''')

GROUP BY coa1.glcode,coa1.name,coa1.TYPE,coa1.id ORDER BY coa1.TYPE DESC,MAJORCODE ASC';

for sc in subscheme_c loop
query_SchemeRpt:=query_SchemeRpt||query_SchemeRpt0||' and vmis.SUBSCHEMEID='||sc.id||') as "'||concat(rtrim(substr(sc.name,1,30)),'",');
end loop;

query_final:=query_part1||query_SchemeRpt||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native); 
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end SCHEMEREPORT;


function OPBALSCHEMEREPORT(pschemeid number,vfromdt date,vmajor number,filterGlcode varchar2) return egf_report.vGetResultSet is

vRSoutput vGetResultSet;

query_part1 varchar2(1000);
query_SchemeRpt0 long;
query_SchemeRpt long;
query_last varchar2(1000);
query_final varchar2(30000);
c1 number;
vstartdt date;

cursor subscheme_c is SELECT distinct id,name FROM SUB_SCHEME WHERE schemeid= pschemeid order by name;

begin

dbms_output.enable(1000000);
vstartdt:=egf_report.FIRSTDATE(to_date(vfromdt));

c1:=dbms_sql.open_cursor;
query_part1:= 'SELECT';

query_SchemeRpt0:=' (SELECT (SUM(gl.creditamount)-SUM(gl.debitamount))

FROM generalledger gl, voucherheader vh,VOUCHERMIS vmis,chartofaccounts coa2,scheme s1
WHERE  vh.ID=vmis.voucherheaderid AND vh.ID=gl.voucherheaderid
AND vh.status<>4 AND vmis.SCHEMEID='||pschemeid||' 
AND SUBSTR(coa2.glcode,1,'||vmajor||') NOT IN ('||filterGlcode||')
AND SUBSTR(gl.glcode,1,'||vmajor||')=coa2.glcode
AND vmis.schemeid=s1.id
and vh.voucherdate>='||'to_date('''||vstartdt||''')'||'
and vh.voucherdate<'||'to_date('''||vfromdt||''')';

query_last:='SUM(gl1.creditamount)-SUM(gl1.debitamount)AS SCHAMT
 
FROM chartofaccounts coa1,generalledger gl1,voucherheader vh1,
vouchermis vmis1,scheme s2
WHERE
vh1.ID=vmis1.voucherheaderid AND vh1.id=gl1.voucherheaderid
AND vh1.status<>4 AND vmis1.schemeid='||pschemeid||' 
AND vmis1.schemeid=s2.id
AND SUBSTR(coa1.glcode,1,'||vmajor||') NOT IN ('||filterGlcode||')
AND SUBSTR(gl1.glcode,1,'||vmajor||')=coa1.glcode

AND vh1.voucherdate>='||'TO_DATE('''||vstartdt||''')'||'
AND vh1.voucherdate<'||'TO_DATE('''||vfromdt||''')
GROUP BY vmis1.schemeid';

for sc in subscheme_c loop
query_SchemeRpt:=query_SchemeRpt||query_SchemeRpt0||' and vmis.SUBSCHEMEID='||sc.id||') as "'||concat(rtrim(substr(sc.name,1,30)),'",');
end loop;

query_final:=query_part1||query_SchemeRpt||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native); 
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end OPBALSCHEMEREPORT;


end EGF_REPORT;
/
