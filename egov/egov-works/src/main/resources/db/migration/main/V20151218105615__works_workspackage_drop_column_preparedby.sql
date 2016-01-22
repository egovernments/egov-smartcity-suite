------------------START------------------
ALTER TABLE EGW_WORKSPACKAGE DROP COLUMN PREPAREDBY;
-------------------END-------------------

--rollback ALTER TABLE EGW_WORKSPACKAGE ADD COLUMN preparedby bigint NOT NULL, 
--rollback ALTER TABLE EGW_WORKSPACKAGE ADD CONSTRAINT fk_workspackage_employee FOREIGN KEY (preparedby) REFERENCES egeis_employee (id),
--rollback CREATE INDEX idx_workspackage_preparedby ON EGW_WORKSPACKAGE USING btreep  (preparedby);