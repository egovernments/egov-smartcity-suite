ALTER TABLE schedulemapping ADD(isRemission NUMBER(1));

show user;

-- creating procedure

CREATE OR REPLACE PROCEDURE print_out(
  IN_TEXT        VARCHAR2,
  IN_TEXT_LENGTH NUMBER   DEFAULT 255,
  IN_DIVIDER     VARCHAR2 DEFAULT CHR(32),
  IN_NEW_LINE    VARCHAR2 DEFAULT NULL)
IS
  lv_print_text        VARCHAR2(32767);
  ln_position          PLS_INTEGER;
  ln_divider_position  PLS_INTEGER;
  ln_total_printed     PLS_INTEGER;
  ln_string_length     PLS_INTEGER;

  PROCEDURE printText (IN_PRINT VARCHAR2)
  IS
  BEGIN
    dbms_output.put_line( IN_PRINT );
  END printText;

BEGIN

  IF IN_TEXT_LENGTH >255
  THEN
    ln_string_length := 255;
  ELSE
    ln_string_length := IN_TEXT_LENGTH;
  END IF;

  IF LENGTHB(IN_TEXT) <=IN_TEXT_LENGTH
  THEN
    printText(IN_TEXT);
  ELSE

    ln_position := 1;
    ln_total_printed := 0;

    LOOP
      lv_print_text := SUBSTR( IN_TEXT,ln_position, ln_string_length );

      IF IN_NEW_LINE IS NULL
      THEN
        ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
      ELSE
        ln_divider_position := INSTR(lv_print_text, IN_NEW_LINE, -1);
        IF ln_divider_position = 0
        THEN
          ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
        END IF;
      END IF;

      IF ln_divider_position = 0
      THEN
        ln_divider_position := ln_string_length;
      END IF;

      IF ln_divider_position <=ln_string_length
      THEN
        lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);

        IF LENGTH( lv_print_text ) <> LENGTHB(lv_print_text)
        THEN
          ln_divider_position := ln_divider_position-(LENGTHB(lv_print_text)-LENGTH( lv_print_text ));
          lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);

          IF IN_NEW_LINE IS NULL
          THEN
            ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
          ELSE
            ln_divider_position := INSTR(lv_print_text, IN_NEW_LINE, -1);
            IF ln_divider_position = 0
            THEN
              ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
            END IF;
          END IF;

          IF ln_divider_position = 0
          THEN
            ln_divider_position := ln_string_length-(LENGTHB(lv_print_text)-LENGTH( lv_print_text ));
          END IF;

          lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);
        END IF;

        IF ln_divider_position = 0
        THEN
          ln_divider_position := ln_string_length;
        END IF;

        ln_position := ln_position+ln_divider_position;
      END IF;

      ln_total_printed := ln_total_printed+LENGTHB(lv_print_text);

      lv_print_text := TRIM( lv_print_text );
      --dbms_output.put_line('***');
      printText(lv_print_text);

      EXIT WHEN ln_position >= LENGTH(TRIM(IN_TEXT));

    END LOOP;

    IF ln_position <ln_total_printed  -- printed not everything
    THEN
      printText(SUBSTR( IN_TEXT, ln_position, ln_total_printed ));
    END IF;

  END IF;
EXCEPTION
  WHEN OTHERS
  THEN
    dbms_output.put_line( 'ERROR :'||SQLERRM );
    dbms_output.put_line( 'ln_position: '||ln_position );
    dbms_output.put_line( 'ln_divider_position: '||ln_divider_position );

END print_out;
/

-- end procedure

-- creating package for receipt payment report

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

cursor fund_c is select distinct id,name from fund where id in (select fundid from voucherheader);

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

cursor fund_c is select distinct id,name from fund where id in (select fundid from voucherheader);

begin

dbms_output.enable(1000000); 

prevfrom:=egf_report.LASTYEARDATE(vfromdt);
prevto:=egf_report.LASTYEARDATE(vtodt);
c1:=dbms_sql.open_cursor;
query_part1:= 'select distinct substr(coa1.glcode,1,'||vminor||'),sm.schedule,sm.schedulename,';

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
where sm.id in (coa.scheduleid) and coa.parentid=coa1.id
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

-- end of package

-- menutree id=130 is receipt/payment List report.  setting actionid to eceipt/payment List report
UPDATE MENUTREE SET ACTIONID=98 WHERE id=130;

create or replace procedure actionRoleMapForULBADMIN
is
roleid number;
begin
	 select id_role into roleid  from eg_roles where 	upper(role_name)='ULB ADMIN';
	INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,98); 
	DBMS_OUTPUT.PUT_LINE(' PROCEDURE CREATED');
end;

/

exec actionRoleMapForULBADMIN;
select 'procedure executed' from dual;

-- ===========================
-- old RP schedule cleaning and New RP schedules creating and updating in  Chartofaccounts table script

-- setting existing RP scheduleid in COA to null
update chartofaccounts set RECEIPTSCHEDULEID=null,RECEIPTOPERATION=null,PAYMENTSCHEDULEID=null, PAYMENTOPERATION=null

-- delete RP schedules from schedulemapping
delete from schedulemapping where reporttype='RP';

-- update sequence

DROP SEQUENCE SEQ_SCHEDULEMAPPING;

CREATE SEQUENCE SEQ_SCHEDULEMAPPING
  START WITH 1
  NOMAXVALUE
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;
	
	 alter sequence seq_schedulemapping
    increment by 143;



    alter sequence seq_schedulemapping
    increment by 1;

-- insert into RP schedule to schedulemapping

Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (100, 'RP', 'R-01', 'Opening Cash and Bank Balances', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (101, 'RP', 'R-02', 'Tax Revenue', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (102, 'RP', 'R-03', 'Assigned Revenues  Compensations', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (103, 'RP', 'R-04', 'Rental Income from Municipal Properties', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (104, 'RP', 'R-05', 'Fees User Charges and Other Charges', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (105, 'RP', 'R-06', 'Sale   Hire Charges', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (106, 'RP', 'R-07', 'Grants and Contributions', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (107, 'RP', 'R-08', 'Interest/ Dividend Earned', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (108, 'RP', 'R-09', 'Other Income', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (109, 'RP', 'R-10', 'Prior Period Item', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'ROP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (110, 'RP', 'R-11', 'Grants and Contributions for Specific Purposes', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (111, 'RP', 'R-12', 'Secured Loans Received', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (112, 'RP', 'R-13', 'Unsecured Loans Received', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (113, 'RP', 'R-14', 'Deposits Received', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (114, 'RP', 'R-15', 'Other Liabilities', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (115, 'RP', 'R-17', 'Investments', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (117, 'RP', 'R-18', 'Recovery of Loans, Advances and Deposits', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'RNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (118, 'RP', 'R-19', 'Remission   Refund', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 1);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (119, 'RP', 'R-20', 'Human Resource Expenses', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (120, 'RP', 'R-21', 'General Expenses', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (121, 'RP', 'R-22', 'Operations   Maintenance', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (122, 'RP', 'R-23', 'Interest   Finance Charges', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (123, 'RP', 'R-24', 'Programme Expenses, Grants etc.', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (124, 'RP', 'R-25', 'Prior Period Item', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (125, 'RP', 'R-26', 'Purchase of Stores', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (126, 'RP', 'R-27', 'Expenditure out of Earmarked Funds', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'POP', 0);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (127, 'RP', 'R-28', 'Repayment of Secured Loans', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (128, 'RP', 'R-29', 'Repayment of Unsecured Loans', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (129, 'RP', 'R-30', 'Refund of Deposits Received', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (130, 'RP', 'R-31', 'Repayment of Other Liabilities', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (139, 'RP', 'R-32', 'Payment for acquisition of Fixed Assets', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (140, 'RP', 'R-33', 'Payment for Capital Work in progress (CWIP)', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (141, 'RP', 'R-34', 'Investments', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (142, 'RP', 'R-35', 'Loans, Advances and Deposits', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);
Insert into schedulemapping
   (ID, REPORTTYPE, SCHEDULE, SCHEDULENAME, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, REPSUBTYPE, ISREMISSION)
 Values
   (143, 'RP', 'R-36', 'Miscellaneous Expenditure', 1, 
    TO_DATE('02/08/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL, NULL, 'PNOP', NULL);

-- update COA table with RP scheduleid
update chartofaccounts set RECEIPTSCHEDULEID=101,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=40  and glcode=     '111' ;
update chartofaccounts set RECEIPTSCHEDULEID=101,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=41  and glcode=     '112' ;
update chartofaccounts set RECEIPTSCHEDULEID=101,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=42  and glcode=     '118' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=118,  PAYMENTOPERATION= 'A' where id=43  and glcode=     '119' ;
update chartofaccounts set RECEIPTSCHEDULEID=102,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=44  and glcode=     '121' ;
update chartofaccounts set RECEIPTSCHEDULEID=103,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=45  and glcode=     '131' ;
update chartofaccounts set RECEIPTSCHEDULEID=103,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=46  and glcode=     '132' ;
update chartofaccounts set RECEIPTSCHEDULEID=103,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=47  and glcode=     '133' ;
update chartofaccounts set RECEIPTSCHEDULEID=103,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=48  and glcode=     '134' ;
update chartofaccounts set RECEIPTSCHEDULEID=103,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=49  and glcode=     '138' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=118,  PAYMENTOPERATION= 'A' where id=50  and glcode=     '139' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=51  and glcode=     '141' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=52  and glcode=     '142' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=53  and glcode=     '143' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=54  and glcode=     '144' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=55  and glcode=     '145' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=56  and glcode=     '146' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=57  and glcode=     '147' ;
update chartofaccounts set RECEIPTSCHEDULEID=104,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=58  and glcode=     '148' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=118,  PAYMENTOPERATION= 'A' where id=59  and glcode=     '149' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=60  and glcode=     '151' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=61  and glcode=     '152' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=62  and glcode=     '153' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=63  and glcode=     '154' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=64  and glcode=     '155' ;
update chartofaccounts set RECEIPTSCHEDULEID=105,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=65  and glcode=     '158' ;
update chartofaccounts set RECEIPTSCHEDULEID=106,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=66  and glcode=     '161' ;
update chartofaccounts set RECEIPTSCHEDULEID=106,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=67  and glcode=     '162' ;
update chartofaccounts set RECEIPTSCHEDULEID=106,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=68  and glcode=     '163' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=69  and glcode=     '171' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=70  and glcode=     '172' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=71  and glcode=     '173' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=72  and glcode=     '174' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=73  and glcode=     '175' ;
update chartofaccounts set RECEIPTSCHEDULEID=107,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=74  and glcode=     '178' ;
update chartofaccounts set RECEIPTSCHEDULEID=108,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=76  and glcode=     '182' ;
update chartofaccounts set RECEIPTSCHEDULEID=108,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=77  and glcode=     '183' ;
update chartofaccounts set RECEIPTSCHEDULEID=108,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=79  and glcode=     '185' ;
update chartofaccounts set RECEIPTSCHEDULEID=108,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=82  and glcode=     '188' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=119,  PAYMENTOPERATION= 'A' where id=84  and glcode=     '211' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=119,  PAYMENTOPERATION= 'A' where id=85  and glcode=     '212' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=119,  PAYMENTOPERATION= 'A' where id=86  and glcode=     '213' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=119,  PAYMENTOPERATION= 'A' where id=87  and glcode=     '214' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=119,  PAYMENTOPERATION= 'A' where id=88  and glcode=     '215' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=89  and glcode=     '221' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=90  and glcode=     '222' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=91  and glcode=     '223' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=93  and glcode=     '224' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=94  and glcode=     '225' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=95  and glcode=     '226' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=96  and glcode=     '227' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=120,  PAYMENTOPERATION= 'A' where id=97  and glcode=     '228' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=98  and glcode=     '231' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=99  and glcode=     '232' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=100  and glcode=     '233' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=101  and glcode=     '234' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=102  and glcode=     '235' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=103  and glcode=     '236' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=105  and glcode=     '237' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=106  and glcode=     '238' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=121,  PAYMENTOPERATION= 'A' where id=107  and glcode=     '239' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=108  and glcode=     '241' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=109  and glcode=     '242' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=110  and glcode=     '243' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=111  and glcode=     '244' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=112  and glcode=     '245' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=113  and glcode=     '246' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=122,  PAYMENTOPERATION= 'A' where id=114  and glcode=     '248' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=116  and glcode=     '251' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=117  and glcode=     '252' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=118  and glcode=     '253' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=119  and glcode=     '254' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=120  and glcode=     '255' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=123,  PAYMENTOPERATION= 'A' where id=121  and glcode=     '256' ;
update chartofaccounts set RECEIPTSCHEDULEID=109,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=135  and glcode=     '281' ;
update chartofaccounts set RECEIPTSCHEDULEID=109,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=136  and glcode=     '282' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=124,  PAYMENTOPERATION= 'A' where id=139  and glcode=     '286' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=124,  PAYMENTOPERATION= 'A' where id=1524  and glcode=     '288' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=126,  PAYMENTOPERATION= 'A' where id=143  and glcode=     '321' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=126,  PAYMENTOPERATION= 'A' where id=144  and glcode=     '325' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=126,  PAYMENTOPERATION= 'A' where id=145  and glcode=     '327' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=126,  PAYMENTOPERATION= 'A' where id=1544  and glcode=     '328' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=152  and glcode=     '341' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=153  and glcode=     '342' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=154  and glcode=     '343' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=155  and glcode=     '344' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=156  and glcode=     '345' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=157  and glcode=     '346' ;
update chartofaccounts set RECEIPTSCHEDULEID=110,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=158  and glcode=    '348' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=160  and glcode=     '351' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=161  and glcode=     '352' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=162  and glcode=     '353' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=163  and glcode=     '354' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=164  and glcode=     '355' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=1543  and glcode=     '357' ;
update chartofaccounts set RECEIPTSCHEDULEID=111,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=127,  PAYMENTOPERATION= 'A' where id=166  and glcode=     '358' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=167  and glcode=     '361' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=168  and glcode=     '362' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=169  and glcode=     '363' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=170  and glcode=     '364' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=171  and glcode=     '365' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=172  and glcode=     '367' ;
update chartofaccounts set RECEIPTSCHEDULEID=112,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=128,  PAYMENTOPERATION= 'A' where id=173  and glcode=     '368' ;
update chartofaccounts set RECEIPTSCHEDULEID=113,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=129,  PAYMENTOPERATION= 'A' where id=174  and glcode=    '371' ;
update chartofaccounts set RECEIPTSCHEDULEID=113,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=129,  PAYMENTOPERATION= 'A' where id=175  and glcode=     '372' ;
update chartofaccounts set RECEIPTSCHEDULEID=113,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=129,  PAYMENTOPERATION= 'A' where id=176  and glcode=     '373' ;
update chartofaccounts set RECEIPTSCHEDULEID=113,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=129,  PAYMENTOPERATION= 'A' where id=177  and glcode=     '374' ;
update chartofaccounts set RECEIPTSCHEDULEID=113,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=129,  PAYMENTOPERATION= 'A' where id=178  and glcode=     '378' ;
update chartofaccounts set RECEIPTSCHEDULEID=114,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=130,  PAYMENTOPERATION= 'A' where id=182  and glcode=     '384' ;
update chartofaccounts set RECEIPTSCHEDULEID=114,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=130,  PAYMENTOPERATION= 'A' where id=183  and glcode=     '385' ;
update chartofaccounts set RECEIPTSCHEDULEID=114,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=130,  PAYMENTOPERATION= 'A' where id=184  and glcode=     '386' ;
update chartofaccounts set RECEIPTSCHEDULEID=114,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=130,  PAYMENTOPERATION= 'A' where id=186  and glcode=     '388' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=190  and glcode=     '411' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=191  and glcode=     '412' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=192  and glcode=     '413' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=193  and glcode=     '414' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=194  and glcode=     '415' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=195  and glcode=     '416' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=196  and glcode=     '417' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=197  and glcode=     '418' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=139,  PAYMENTOPERATION= 'A' where id=198  and glcode=     '419' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=140,  PAYMENTOPERATION= 'A' where id=207  and glcode=     '432' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=140,  PAYMENTOPERATION= 'A' where id=208  and glcode=     '433' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=140,  PAYMENTOPERATION= 'A' where id=209  and glcode=     '434' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=140,  PAYMENTOPERATION= 'A' where id=210  and glcode=     '435' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=140,  PAYMENTOPERATION= 'A' where id=211  and glcode=     '438' ;
update chartofaccounts set RECEIPTSCHEDULEID=115,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=141,  PAYMENTOPERATION= 'A' where id=212  and glcode=     '441' ;
update chartofaccounts set RECEIPTSCHEDULEID=115,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=141,  PAYMENTOPERATION= 'A' where id=213  and glcode=     '442' ;
update chartofaccounts set RECEIPTSCHEDULEID=115,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=141,  PAYMENTOPERATION= 'A' where id=214  and glcode=     '443' ;
update chartofaccounts set RECEIPTSCHEDULEID=115,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=141,  PAYMENTOPERATION= 'A' where id=215  and glcode=     '445' ;
update chartofaccounts set RECEIPTSCHEDULEID=115,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=141,  PAYMENTOPERATION= 'A' where id=216  and glcode=     '448' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=125,  PAYMENTOPERATION= 'A' where id=218  and glcode=     '451' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=125,  PAYMENTOPERATION= 'A' where id=219  and glcode=     '452' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=227  and glcode=     '471' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=228  and glcode=     '472' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=229  and glcode=     '473' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=230  and glcode=     '474' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=231  and glcode=     '478' ;
update chartofaccounts set RECEIPTSCHEDULEID=100,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID= NULL,  PAYMENTOPERATION= NULL where id=1540  and glcode=     '479' ;
update chartofaccounts set RECEIPTSCHEDULEID=117,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=142,  PAYMENTOPERATION= 'A' where id=232  and glcode=     '481' ;
update chartofaccounts set RECEIPTSCHEDULEID=117,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=142,  PAYMENTOPERATION= 'A' where id=233  and glcode=     '482' ;
update chartofaccounts set RECEIPTSCHEDULEID=117,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=142,  PAYMENTOPERATION= 'A' where id=236  and glcode=     '485' ;
update chartofaccounts set RECEIPTSCHEDULEID=117,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=142,  PAYMENTOPERATION= 'A' where id=237  and glcode=     '486' ;
update chartofaccounts set RECEIPTSCHEDULEID=117,  RECEIPTOPERATION= 'A',  PAYMENTSCHEDULEID=142,  PAYMENTOPERATION= 'A' where id=238  and glcode=     '488' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=143,  PAYMENTOPERATION= 'A' where id=240  and glcode=     '491' ;
update chartofaccounts set RECEIPTSCHEDULEID=NULL,  RECEIPTOPERATION= NULL,  PAYMENTSCHEDULEID=143,  PAYMENTOPERATION= 'A' where id=242  and glcode=     '498' ;
commit;

exit;

