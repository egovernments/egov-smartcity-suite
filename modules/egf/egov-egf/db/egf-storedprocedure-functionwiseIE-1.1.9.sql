CREATE OR REPLACE package EGF_REPORT_FUNCTION is

TYPE vGetResultSet is REF CURSOR;

function FUNCTIONWISEIE(vincexp varchar2,vfundid number,vfromdt date,vtodt date,vmajor number) return egf_report.vGetResultSet;

end EGF_REPORT_FUNCTION;
/

CREATE OR REPLACE package body EGF_REPORT_FUNCTION as

function FUNCTIONWISEIE(vincexp varchar2,vfundid number,vfromdt date,vtodt date,vmajor number) return egf_report.vGetResultSet is
vRSoutput vGetResultSet;

query_part1 varchar2(30000);
query_IncExp0 long;
query_IncExp long;
query_last varchar2(30000);
query_final varchar2(30000);
query_temp varchar2(30000);
c1 number;

cursor glcode_c is select distinct substr(gl.glcode,1,vmajor) as temp,c.name from generalledger gl,chartofaccounts c 
where gl.voucherheaderid in (select id from voucherheader where voucherdate>=to_date(vfromdt) and voucherdate<=to_date(vtodt) 
and fundid=vfundid and status<>4) and c.type=vincexp and substr(gl.glcode,1,vmajor)=c.glcode order by 1;

begin

dbms_output.enable(10000000);

c1:=dbms_sql.open_cursor;
query_part1:= 'select distinct fn1.code,';

query_IncExp0:='(select (decode('''||vincexp||''',''I'',(sum(gl.creditamount)-sum(gl.debitamount)),''E'',(sum(gl.debitamount)-sum(gl.creditamount))))
from generalledger gl, voucherheader vh, chartofaccounts coa,function fn
where vh.ID=gl.voucherheaderid and substr(gl.glcode,1,'||vmajor||')=coa.glcode and coa.type='''||vincexp||'''
and vh.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh.voucherdate<='||'to_date('''||vtodt||''')
and vh.status<>4 and fn.id = gl.functionid and fn.id =fn1.id and vh.fundid='||vfundid||'';

query_last:='fn1.name from chartofaccounts coa1,voucherheader vh1,generalledger gl1,function fn1
where vh1.ID=gl1.voucherheaderid and substr(gl1.glcode,1,'||vmajor||')=coa1.glcode and coa1.type='''||vincexp||'''
and vh1.voucherdate>='||'to_date('''||vfromdt||''')'||' and vh1.voucherdate<='||'to_date('''||vtodt||''')
and vh1.status<>4 and fn1.id = gl1.functionid and vh1.fundid='||vfundid||' 
group by fn1.code,fn1.id,fn1.name
order by fn1.code ';

for fc in glcode_c loop
	query_IncExp:=query_IncExp||query_IncExp0||' and gl.glcode like '''||fc.temp||'%'') as "'||concat(trim(fc.temp),'",');
end loop;

query_final:=query_part1||query_IncExp||query_last;
print_out(query_final);
dbms_sql.parse(c1,query_final,dbms_sql.native);
open vRSoutput for query_final;
dbms_sql.close_cursor(c1);
return vRSoutput;

end FUNCTIONWISEIE;

end EGF_REPORT_FUNCTION;
/
