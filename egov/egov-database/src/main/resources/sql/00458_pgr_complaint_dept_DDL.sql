ALTER TABLE egpgr_complaint ADD COLUMN department bigint;
ALTER table egpgr_complaint add CONSTRAINT fk_department foreign key (department) 
REFERENCES eg_department (id);

--rollback ALTER TABLE egpgr_complaint DROP COLUMN department;
