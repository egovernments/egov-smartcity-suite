create or replace package EGF_REPORT is 

TYPE vGetResultSet is REF CURSOR;

function RCPPYMNT(vfromdt varchar2,vtodt varchar2,vcash varchar2,vminor number) return egf_report.vGetResultSet;
function INCOMEEXP(vfromdt date,vtodt date,vminor number,vdiv number) return egf_report.vGetResultSet;
function LASTYEARDATE(vdt date) return date;

end EGF_REPORT;
/

create or replace package body EGF_REPORT as

function LASTYEARDATE(vdt date) return date is

retdate date;

begin
SELECT TO_DATE((LPAD(extract(day from to_date(vdt)),2,0)||LPAD(extract(month from to_date(vdt)),2,0)||(extract(year from to_date(vdt))-1)),'DDMMYYYY') into retdate FROM DUAL;
return (retdate);
end LASTYEARDATE;


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

cursor fund_c is select distinct id,name from fund where id in (select distinct fundid from voucherheader);

begin

dbms_output.enable(1000000); 
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
	query_rpt:=query_rpt||query_rpt0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
	/* print_out(query_rpt); */
	query_pmt:=query_pmt||query_pmt0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
	/* print_out(query_pmt); */
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

cursor fund_c is select distinct id,name from fund where id in (Select distinct fundid from voucherheader);

begin

dbms_output.enable(1000000); 

prevfrom:=egf_report.LASTYEARDATE(vfromdt);
prevto:=egf_report.LASTYEARDATE(vtodt);
c1:=dbms_sql.open_cursor;
query_part1:= 'select distinct concat(substr(coa1.glcode,1,'||vminor||'),0),sm.schedule,sm.schedulename,';

query_IncExp0:='(
select decode(coa.type,''I'',sum(gl.creditamount)-sum(gl.debitamount),''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')';

query_SumCurrentYear:='(
select decode(coa.type,''I'',sum(gl.creditamount)-sum(gl.debitamount),''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''') 
and vh.status<>4) as "'||concat('Sum Current Year','",');

query_SumPreviousYear:='(
select decode(coa.type,''I'',sum(gl.creditamount)-sum(gl.debitamount),''E'',(sum(gl.debitamount)-sum(gl.creditamount))/'||vdiv||')
from generalledger gl, voucherheader vh, chartofaccounts coa1
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vminor||')=coa1.glcode and coa1.scheduleid=coa.SCHEDULEID and
vh.voucherdate>='||'to_date('''||prevfrom||''')'||' and vh.voucherdate<='||'to_date('''||prevto||''') 
and vh.status<>4) as "'||concat('Sum Previous Year','",');

query_last:='coa.scheduleid from chartofaccounts coa, chartofaccounts coa1,schedulemapping sm
where sm.id in (coa.scheduleid) and coa.parentid=coa1.id and sm.reporttype=''IE''
group by sm.schedule,sm.schedulename,coa1.glcode,coa.scheduleid,coa.type';



for fc in fund_c loop
	query_IncExp:=query_IncExp||query_IncExp0||' and vh.fundid='||fc.id||' and vh.status<>4 ) as "'||concat(rtrim(fc.name),'",');
	
end loop;

query_final:=query_part1||query_IncExp||query_SumCurrentYear||query_SumPreviousYear||query_last; 
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vRSoutput for query_final; 
dbms_sql.close_cursor(c1);
return vRSoutput;

end INCOMEEXP;


end EGF_REPORT;

/