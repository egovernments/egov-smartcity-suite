ALTER TABLE fiscalperiod RENAME COLUMN created TO createddate;

ALTER TABLE fiscalperiod RENAME COLUMN modifiedby TO lastmodifiedby;

ALTER TABLE fiscalperiod RENAME COLUMN lastmodified TO lastmodifieddate;

ALTER TABLE fiscalperiod  ADD COLUMN version numeric ;

ALTER TABLE fiscalperiod ADD COLUMN createdby bigint;


alter table fiscalperiod alter column isactive type  boolean 
USING CASE WHEN isactive = 0 THEN FALSE
	   WHEN isactive = 1 THEN TRUE
	   ELSE NULL
	   END;
alter table fiscalperiod alter column isactiveforposting type  boolean 
USING CASE WHEN isactiveforposting = 0 THEN FALSE
	   WHEN isactiveforposting = 1 THEN TRUE
	   ELSE NULL
	   END;

alter table fiscalperiod alter column isclosed type  boolean 
USING CASE WHEN isclosed = 0 THEN FALSE
	   WHEN isclosed = 1 THEN TRUE
	   ELSE NULL
	   END;
