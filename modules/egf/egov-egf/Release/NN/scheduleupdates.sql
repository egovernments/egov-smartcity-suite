/* Schedule upload */
CREATE OR REPLACE procedure scheduleupdates AS

cursor BSIEschdmap is select id,glcode,scheduleid from chartofaccounts@egovdb where scheduleid is not null and scheduleid in (select id from schedulemapping where reporttype in ('BS','IE'));

coaid number default 0;
skip number default 0;

begin

for  bsmap in BSIEschdmap loop

begin
select id into coaid from chartofaccounts where glcode=bsmap.glcode;
exception when no_data_found then
		  dbms_output.put_line('GL CODE NOT FOUND!'||bsmap.glcode);
		  skip:=1;
end;
if skip=0 then
   update chartofaccounts set scheduleid=bsmap.scheduleid where id=coaid;
end if; 

end loop;
/*
		for rpmap in RPschdmap loop

		begin
		select id into coaid from chartofaccounts where glcode=rpmap.glcode;
		exception when no_data_found then
				  dbms_output.put_line('GL CODE NOT FOUND!'||rpmap.glcode);
				  skip:=1;
		end;
		if skip=0 then
		   update chartofaccounts set receiptscheduleid=rpmap.receiptscheduleid,paymentscheduleid=rpmap.paymentscheduleid,
		   receiptoperation=rpmap.receiptoperation, paymentoperation=rpmap.paymentoperation 
		   where id=coaid;
		end if;

		end loop;
*/
end scheduleupdates;
/

exec scheduleupdates;


commit;
exit;

