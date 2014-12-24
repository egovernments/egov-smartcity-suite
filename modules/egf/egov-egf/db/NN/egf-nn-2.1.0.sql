UPDATE CHARTOFACCOUNTS SET purposeid=30 WHERE glcode='246800';
-- bug ID 11060 starts
update chartofaccounts set name= replace(name,'&','and') where name like '%&%';
update bankaccount set accounttype= replace(accounttype,'&','and') where accounttype like '%&%';
-- bug ID 11060 ends

-- Bug ID 13303 starts

CREATE OR REPLACE procedure update_PT_finYear as
  cursor dataCur is select distinct period,financialyear  from propertytaxintermediate pt, financialyear fy 
	  where period<>financialyear and substr(period,1,4)=substr(financialyear,1,4);
begin
	DBMS_OUTPUT.ENABLE(100000);
	for i in dataCur loop
		--dbms_output.put_line(i.period||'::'||i.financialyear);
		update propertytaxintermediate set period=i.financialyear where period=i.period;
	 end loop; 
	 commit;
end;
/
-- Bug ID 13303 ends 

-- issue found during transfer closing balance (in gokak)
update chartofaccounts set type='I'  where substr(glcode,1,1)='1' and type is  null;
update chartofaccounts set type='E'  where substr(glcode,1,1)='2' and type is  null;
update chartofaccounts set type='L'  where substr(glcode,1,1)='3' and type is  null;
update chartofaccounts set type='A'  where substr(glcode,1,1)='4' and type is  null;
alter  table chartofaccounts modify (type NOT NULL);
COMMIT;
