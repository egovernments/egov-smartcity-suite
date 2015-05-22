ALTER TABLE egcl_servicedetails ADD COLUMN created_by bigint NOT NULL;
ALTER TABLE egcl_servicedetails ADD COLUMN created_date timestamp without time zone NOT NULL;
ALTER TABLE egcl_servicedetails ADD COLUMN modified_by bigint NOT NULL;
ALTER TABLE egcl_servicedetails ADD COLUMN modified_date timestamp without time zone NOT NULL;
