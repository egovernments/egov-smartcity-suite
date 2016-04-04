ALTER TABLE egpt_mv_current_prop_det DROP COLUMN if exists category_type;
ALTER TABLE egpt_mv_current_prop_det ADD COLUMN category_type character varying(256);

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists isactive;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN isactive boolean DEFAULT true; 

ALTER TABLE egpt_mv_propertyinfo DROP COLUMN if exists category_type;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN category_type character varying(256);