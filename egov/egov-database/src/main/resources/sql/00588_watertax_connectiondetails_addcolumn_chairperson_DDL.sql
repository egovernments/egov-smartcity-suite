ALTER TABLE egwtr_connectiondetails ADD COLUMN chairperson bigint;
ALTER TABLE egwtr_connectiondetails ADD CONSTRAINT fk_connectiondetails_chairperson FOREIGN KEY (chairperson) REFERENCES eg_chairperson (id);
CREATE INDEX IDX_connectiondetails_chairperson ON egwtr_connectiondetails USING BTREE (chairperson);

-- rollback ALTER TABLE egwtr_connectiondetails DROP COLUMN chairperson;