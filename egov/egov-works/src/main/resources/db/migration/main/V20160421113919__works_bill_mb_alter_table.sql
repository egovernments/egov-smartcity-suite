------------------START------------------
ALTER TABLE EGW_CONTRACTORBILL ADD CONSTRAINT pk_egw_contractorbill PRIMARY KEY (ID);

ALTER TABLE EGW_MB_HEADER DROP COLUMN PREPARED_BY;
ALTER TABLE EGW_MB_HEADER DROP COLUMN document_number;

ALTER TABLE EGW_MB_HEADER DROP CONSTRAINT fk_mbh_billregister;
ALTER TABLE EGW_MB_HEADER ADD CONSTRAINT fk_mbh_billregister FOREIGN KEY (billregister_id) REFERENCES EGW_CONTRACTORBILL (id);

ALTER TABLE EGW_MB_HEADER ALTER COLUMN workorder_estimate_id DROP NOT NULL;
ALTER TABLE EGW_MB_HEADER ALTER COLUMN abstract DROP NOT NULL;

ALTER TABLE egw_cancelled_bill DROP CONSTRAINT fk_cb_billreg;
ALTER TABLE egw_cancelled_bill ADD CONSTRAINT fk_cb_billreg FOREIGN KEY (billregister_id) REFERENCES EGW_CONTRACTORBILL (id);

-------------------END-------------------

--rollback ALTER TABLE egw_cancelled_bill DROP CONSTRAINT fk_cb_billreg;
--rollback ALTER TABLE egw_cancelled_bill ADD CONSTRAINT fk_cb_billreg FOREIGN KEY (billregister_id) REFERENCES eg_billregister (id);

--rollback ALTER TABLE EGW_MB_HEADER ALTER COLUMN abstract SET NOT NULL;
--rollback ALTER TABLE EGW_MB_HEADER ALTER COLUMN workorder_estimate_id SET NOT NULL;

--rollback ALTER TABLE EGW_MB_HEADER DROP CONSTRAINT fk_mbh_billregister;
--rollback ALTER TABLE EGW_MB_HEADER ADD CONSTRAINT fk_mbh_billregister FOREIGN KEY (billregister_id) REFERENCES eg_billregister (id);

--rollback ALTER TABLE EGW_MB_HEADER ADD COLUMN document_number bigint; 

--rollback ALTER TABLE EGW_MB_HEADER ADD COLUMN PREPARED_BY bigint NOT NULL;
--rollback ALTER TABLE EGW_MB_HEADER ADD CONSTRAINT fk_mbh_preparedby FOREIGN KEY (prepared_by) REFERENCES egeis_employee (id);
--rollback CREATE INDEX idx_mbh_preparedby ON EGW_MB_HEADER USING btree (prepare_dby);

--rollback ALTER TABLE EGW_CONTRACTORBILL DROP CONSTRAINT pk_egw_contractorbill;