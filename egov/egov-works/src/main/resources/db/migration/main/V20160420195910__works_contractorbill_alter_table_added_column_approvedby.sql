------------------START------------------
ALTER TABLE EGW_CONTRACTORBILL ADD COLUMN approvedby bigint;
ALTER TABLE EGW_CONTRACTORBILL ADD CONSTRAINT fk_contractorbill_approvedby FOREIGN KEY (approvedby) REFERENCES eg_user (id);
CREATE INDEX idx_contractorbill_approvedby ON EGW_CONTRACTORBILL USING btree (approvedby);

--rollback ALTER TABLE EGW_CONTRACTORBILL DROP COLUMN approvedby; 

-------------------END-------------------