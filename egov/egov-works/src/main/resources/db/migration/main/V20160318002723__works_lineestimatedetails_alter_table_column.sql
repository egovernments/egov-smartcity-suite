------------------START--------------------------
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN actualestimateamount double precision;

ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN projectcode bigint;
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD CONSTRAINT fk_lineestimate_details_projectcode FOREIGN KEY (projectcode) REFERENCES egw_projectcode (id);
CREATE INDEX idx_lineestimate_details_projectcode ON EGW_LINEESTIMATE_DETAILS USING btree (projectcode);

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN projectcode;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN actualestimateamount;

-----------------END------------------------------