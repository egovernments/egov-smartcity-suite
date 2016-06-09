------------------START------------------
ALTER TABLE EGW_ABSTRACTESTIMATE ADD COLUMN approvedby bigint;
ALTER TABLE EGW_ABSTRACTESTIMATE ADD CONSTRAINT fk_abstractestimate_approvedby FOREIGN KEY (approvedby) REFERENCES eg_user (id);
CREATE INDEX idx_abstractestimate_approvedby ON EGW_ABSTRACTESTIMATE USING btree (approvedby);

--rollback ALTER TABLE EGW_ABSTRACTESTIMATE DROP COLUMN approvedby;