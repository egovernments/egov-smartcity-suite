------------------START--------------------------
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN technicalsanctionnumber varchar(50);
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN technicalsanctiondate timestamp without time zone;
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN technicalsanctionby bigint;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_technicalsanctionby FOREIGN KEY (technicalsanctionby) REFERENCES eg_user (id);
CREATE INDEX idx_lineestimate_technicalsanctionby ON EGW_LINEESTIMATE USING btree (technicalsanctionby);

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN technicalsanctiondate;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN technicalsanctionnumber;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN technicalsanctionby;

-----------------END------------------------------