drop sequence if exists seq_expense_billnumber_2015_16;

drop sequence if exists seq_expense_billnumber_2016_17;

drop sequence if exists sq_bill_ejv;

create sequence seq_expense_billnumber_2016_17;

create sequence seq_expense_billnumber_2015_16;


DO $$
	DECLARE
	 v_seq1516 bigint;
	 v_seq1617 bigint;
	 v_count1516 bigint;
	 v_count1617 bigint;
	BEGIN
	select count(id) into v_count1516 from eg_billregister where billnumber like '%/EJV/%2015-16' ;
	select count(id) into v_count1617 from eg_billregister where billnumber like '%/EJV/%2016-17' ;
	
       	if v_count1516 > 0 then 
	        select trim(LEADING '0' FROM trim(both '/' from substring(billnumber from '%#"/0_*/#"%' for '#'))) into v_seq1516 from eg_billregister where BILLNUMBER LIKE '%/EJV/%2015-16' ORDER BY id DESC LIMIT 1;
		PERFORM setval('seq_expense_billnumber_2015_16',v_seq1516);
	else
		PERFORM setval('seq_expense_billnumber_2015_16',1);
	end if;

	if v_count1617 > 0 then 
	        select trim(LEADING '0' FROM trim(both '/' from substring(billnumber from '%#"/0_*/#"%' for '#'))) into v_seq1617 from eg_billregister where BILLNUMBER LIKE '%/EJV/%2016-17' ORDER BY id DESC LIMIT 1;
		PERFORM setval('seq_expense_billnumber_2016_17',v_seq1617);
	else
		PERFORM setval('seq_expense_billnumber_2016_17',1);
	end if;
END$$;

delete from eg_script where name = 'egf.bill.number.generator';

