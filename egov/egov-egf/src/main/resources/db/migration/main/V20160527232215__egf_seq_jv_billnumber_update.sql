DO $$
	DECLARE
	 v_seq bigint;
	 v_count bigint;
	    BEGIN
	select count(id) into v_count from eg_billregister where billnumber like '%MN%' ;
	
       	if v_count > 0 then 
	        select trim(LEADING '0' FROM trim(both '/' from substring(billnumber from '%#"/0_*/#"%' for '#'))) into v_seq from eg_billregister where BILLNUMBER LIKE '%/MN/%' ORDER BY id DESC LIMIT 1;
		PERFORM setval('seq_jv_billnumber_2016_17',v_seq);
	else
		PERFORM setval('seq_jv_billnumber_2016_17',1);
	end if;
END$$;

delete from eg_script where name = 'autobillnumber';
