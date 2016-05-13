ALTER TABLE egpt_mv_propertyinfo ADD COLUMN ARREAR_DEMAND double precision;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN ARREAR_collection double precision;
ALTER TABLE egpt_mv_propertyinfo ADD COLUMN is_under_courtcase boolean NOT NULL DEFAULT FALSE;
