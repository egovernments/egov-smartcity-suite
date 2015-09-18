-- Adding version column 
ALTER TABLE egtl_mstr_category ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egtl_mstr_sub_category ADD COLUMN version bigint DEFAULT 0;
