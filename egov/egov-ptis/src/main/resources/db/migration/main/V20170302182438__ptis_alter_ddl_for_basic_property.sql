DO $$ 
BEGIN
BEGIN
ALTER TABLE egpt_basic_property ADD COLUMN is_intg_bill_created character varying DEFAULT 'N';
EXCEPTION
WHEN duplicate_column THEN RAISE NOTICE 'column is_intg_bill_created already exists in egpt_basic_property.';
END;
END;
$$