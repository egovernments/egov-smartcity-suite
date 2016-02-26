alter table fund alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table fund alter column isnotleaf type  boolean 
USING CASE WHEN isnotleaf = 0 THEN FALSE
	   WHEN isnotleaf = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table Functionary alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;


alter table function alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table function alter column isnotleaf type  boolean 
USING CASE WHEN isnotleaf = 0 THEN FALSE
	   WHEN isnotleaf = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table sub_scheme alter column isactive type  boolean 
USING CASE WHEN isactive = '0' THEN FALSE
	   WHEN isactive = '1' THEN TRUE
	   ELSE NULL
	   END;

alter table bank alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;


alter table bankaccount alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;


alter table bankbranch alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table financialyear alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table financialyear alter column isactiveforposting type  boolean 
USING CASE WHEN isactiveforposting = 0 THEN FALSE
	   WHEN isactiveforposting = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table financialyear alter column isclosed type  boolean 
USING CASE WHEN isclosed = 0 THEN FALSE
	   WHEN isclosed = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table financialyear alter column transferclosingbalance type  boolean 
USING CASE WHEN transferclosingbalance = 0 THEN FALSE
	   WHEN transferclosingbalance = 1 THEN TRUE
	   ELSE NULL
	   END;


alter table fundsource alter column isnotleaf type  boolean 
USING CASE WHEN isnotleaf = 0 THEN FALSE
	   WHEN isnotleaf = 1 THEN TRUE
	   ELSE NULL
	   END;


