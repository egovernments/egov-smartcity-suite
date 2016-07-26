ALTER TABLE EG_UOM RENAME COLUMN lastmodified TO lastmodifieddate;

ALTER TABLE EG_UOM  ADD COLUMN version numeric ;

update EG_UOM set version=0 where version is null;

ALTER TABLE eg_uom ALTER COLUMN baseuom type  boolean 
USING CASE WHEN baseuom = 0 THEN FALSE
	   WHEN baseuom = 1 THEN TRUE
	   ELSE NULL
	   END;

