alter table egf_instrumenttype alter column isactive type  boolean 
USING CASE WHEN isactive = '0' THEN FALSE
	   WHEN isactive = '1' THEN TRUE
	   ELSE NULL
	   END;
