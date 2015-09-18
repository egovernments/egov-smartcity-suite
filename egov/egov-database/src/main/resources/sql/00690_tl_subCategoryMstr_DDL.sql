-- Unique Constraint
ALTER TABLE egtl_mstr_sub_category ADD CONSTRAINT UNQ_TLSUBCATEGORY_NAME UNIQUE (NAME);
ALTER TABLE egtl_mstr_sub_category ADD CONSTRAINT UNQ_TLSUBCATEGORY_CODE UNIQUE (CODE);

-- Drop Constraint
alter table  egtl_mstr_sub_category alter id_license_type drop not null;

