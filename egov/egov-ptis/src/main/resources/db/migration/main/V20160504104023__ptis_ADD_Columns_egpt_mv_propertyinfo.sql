DO $$ 
BEGIN
BEGIN
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN aadharno character varying;
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column aadharno already exists in egpt_mv_propertyinfo.';
END;
END;
$$