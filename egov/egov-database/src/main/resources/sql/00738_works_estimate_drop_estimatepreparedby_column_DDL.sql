
ALTER TABLE EGW_ABSTRACTESTIMATE DROP COLUMN PREPAREDBY;

--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ADD COLUMN preparedby bigint NOT NULL, 
--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ADD CONSTRAINT fk_estimate_preparedby FOREIGN KEY (preparedby) REFERENCES eg_employee (id),
--rollback CREATE INDEX idx_est_preparedby ON egw_abstractestimate USING btree (preparedby);