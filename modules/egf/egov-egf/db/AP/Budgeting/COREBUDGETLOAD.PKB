CREATE OR REPLACE package corebudgetload as
function createbudgroup(vglcode varchar2) return number;
function BUDGETLOAD(vfunctioncode varchar2,vglcode varchar2) return number;
procedure CREATEBUDGET(vrootid number,vfinyearid number);
end corebudgetload;
/
CREATE OR REPLACE package body corebudgetload as

function createbudgroup(vglcode varchar2) return number as

coaid number default 0;
ctype varchar2(2) default null;
cclassn number default 0;
cname varchar2(256) default null;
retval number default 0;
vbacctype number default 0;
vbudtype number default 0;
vmajorcode number default null;
vmincode number default null;
vmaxcode number default null;
mustinsert number default 0;

begin

begin
dbms_output.put_line('Creating budgetgroup for'||vglcode);
select id,type,classification,name into coaid,ctype,cclassn,cname from chartofaccounts where glcode=vglcode;
exception
    when no_data_found then
	dbms_output.put_line('GLCODE Not found '||vglcode);
	retval:=-1;
end;
if retval>-1 then
   dbms_output.put_line('coaid:'||coaid);
   dbms_output.put_line('cclassn:'||cclassn);
   dbms_output.put_line('Processing:'||vglcode);
   begin
   select id into retval from egf_budgetgroup where mincode=coaid or maxcode=coaid or majorcode=coaid;
   exception when no_data_found then
   			 mustinsert:=1;
	end;
	if mustinsert=1 then
     	if ctype='E' then
  		vbacctype:=2;
  		vbudtype:=3;
  	elsif ctype='A' then
  		vbacctype:=4;
  		vbudtype:=3;
  	elsif ctype='L' then
  		vbacctype:=3;
  		vbudtype:=2;
  	elsif ctype='I' then
  		vbacctype:=1;
  		vbudtype:=2;
  	end if;
  	if cclassn=1 then
  		vmajorcode:=coaid;
	elsif cclassn=2 then
  		vmaxcode:=coaid;
  	elsif cclassn=4 then
  		vmincode:=coaid;
  	end if;
  	dbms_output.put_line('About to insert for'||vmincode||':'||vmajorcode);
  	begin
  	insert into egf_budgetgroup(id,name,mincode,maxcode,majorcode,description,updatetime,acctype,budgetingtype)
  	values
  	(S_70_3_egf_budgetgroup.nextval, cname,vmincode,vmaxcode,vmajorcode,cname,sysdate,vbacctype,vbudtype);
  	exception
  		when others then
  		dbms_output.put_line('Error inserting into EGF_BUDGETGROUP'||vglcode||':'||SUBSTR(SQLERRM,1,100));
  		retval:=-1;
  	end;
  	if retval>-1 then
  		select max(id) into retval from egf_budgetgroup;
  		dbms_output.put_line('Budget Group loaded'||retval||':'||vglcode);
  	end if;

	end if; /*musinsert=1*/
end if; /* glcode exists */

return (retval);

end createbudgroup;


function BUDGETLOAD(vfunctioncode varchar2,vglcode varchar2) return number as

cursor BLOAD is select FUNCTIONCODE, GLCODE, BUDGETESTIMATE FROM BUDGETDATA where functioncode=vfunctioncode and glcode=vglcode;

skip number default 0;
vbudgroupid number default 0;
budamt number default 0;
vfunid number default 0;
vbudgetid number default 0;
vfunctionname varchar2(100);

begin

for BL in BLOAD loop

dbms_output.put_line('Loading details for '||vfunctioncode);
dbms_output.put_line('Loading details for '||vglcode);

begin
select id,name into vfunid,vfunctionname from function where code=bl.FUNCTIONCODE;
exception
	when no_data_found then
		dbms_output.put_line('Function does not exist'||bl.FUNCTIONCODE);
		skip:=1;
end;

begin
select id into vbudgetid from egf_budget where name=vfunctionname||'-'||vglcode;
exception
	when no_data_found then
		dbms_output.put_line('Budget Name does not exist'||vfunctionname||'-'||vglcode);
		skip:=1;
end;


if skip=0 then
	vbudgroupid:=0;
	vbudgroupid:=corebudgetload.createbudgroup(bl.glcode);
	if vbudgroupid>-1 then
	budamt:=bl.budgetestimate;
	if budamt>0 then
		dbms_output.put_line('Inserting budgetline');
		insert into egf_budgetdetail(id,budgetgroupid,budgetid,originalamt,revisedamt,budgetavailable,departmentid,functionid,wardid,statusid,updatetime,add_less) values (S_70_3_EGF_BUDGET_DETAIL.nextval,vbudgroupid,vbudgetid,budamt,0,budamt,NULL,vfunid,NULL,2,sysdate,NULL);
  		dbms_output.put_line('inserted budget line for'||vbudgroupid||':'||budamt);
  	end if; /* budamt>0 */
	end if;

end if;
end loop;

return (skip);

end budgetload;

/* create a root budget: It will then call BUDGETLOAD which will insert
rows for this budget */

procedure CREATEBUDGET(vrootid number,vfinyearid number)
AS

cursor budgetname is select FUNCTIONCODE, GLCODE, BUDGETESTIMATE FROM BUDGETDATA;

vbudgetname varchar2(256);
vfieldid number default 0;
vbudid number default 0;
vbuddetailid number default 0;
vchildbudid number default 0;
skip number default 0;
retval number default 0;
vfunctionname varchar2(100);

begin

dbms_output.enable(10000000000000000000000000);

for bn in budgetname loop
	begin
	select name into vfunctionname from function where code=bn.functioncode;
	exception
		when no_data_found then
			dbms_output.put_line('Function name does not exist for function code '||bn.functioncode);
			skip:=1;
	end;

	begin
	select id into vbudid from egf_budget where name=vfunctionname||'-'||bn.glcode;
	exception when no_data_found then
		dbms_output.put_line('New parent budget being inserted'||vfunctionname||'-'||bn.glcode);
		select s_egf_budget.nextval into vbudid from dual;
		insert into egf_budget(id,name,description,financialyearid,statusid,isactivebudget,parent,updatetime,isprimarybudget)
		values (vbudid,vfunctionname||'-'||bn.glcode,vfunctionname||'-'||bn.glcode,vfinyearid,2,1,vrootid,sysdate,0);

	end;


	retval:=corebudgetload.BUDGETLOAD(bn.functioncode,bn.glcode);
	if retval=0 then
		dbms_output.put_line('Budget details inserted for'||vfunctionname||'-'||bn.glcode);
	else
		dbms_output.put_line('Budget details failed for'||vfunctionname||'-'||bn.glcode);
	end if;
end loop;

end createbudget;

end corebudgetload;
/

