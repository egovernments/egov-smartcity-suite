DO $$
    BEGIN
       IF EXISTS(SELECT 0 FROM pg_class where relname = 'seq_jv_billnumber_2016_17' )
	THEN
		IF NOT EXISTS(SELECT 0 FROM pg_class where relname = 'sq_billnumber_mn' )
			THEN
				CREATE SEQUENCE sq_billnumber_mn  minvalue 0 start with 2;
		END IF;
		DROP SEQUENCE seq_jv_billnumber_2016_17;
		CREATE SEQUENCE seq_jv_billnumber_2016_17 minvalue 0 start with 1;
		PERFORM setval('seq_jv_billnumber_2016_17', ((Select case when seq = null then 2 else case when seq=0 then 2 else seq end end from (select nextval('sq_billnumber_mn') as seq) t )-1));
        ELSE
		IF NOT EXISTS(SELECT 0 FROM pg_class where relname = 'sq_billnumber_mn' )
			THEN
				CREATE SEQUENCE sq_billnumber_mn;
		END IF;
		CREATE SEQUENCE seq_jv_billnumber_2016_17 minvalue 0 start with 1;
		PERFORM setval('seq_jv_billnumber_2016_17', ((Select case when seq = null then 2 else case when seq=0 then 2 else seq end end from (select nextval('sq_billnumber_mn') as seq) t )-1));
		drop sequence sq_billnumber_mn;
	END IF;

	IF EXISTS(SELECT 0 FROM pg_class where relname = 'sq_billnumber_mn' )
		THEN
			DROP SEQUENCE sq_billnumber_mn;
	END IF;

END$$;

delete from eg_script where name = 'autobillnumber';
