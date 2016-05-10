------------------START------------------

ALTER TABLE egw_abstractestimate DROP COLUMN document_number;
ALTER TABLE egw_abstractestimate ALTER COLUMN user_department DROP NOT NULL;
ALTER TABLE egw_abstractestimate ALTER COLUMN fundsource_id DROP NOT NULL;

ALTER TABLE egw_abstractestimate ADD COLUMN lineestimatedetails bigint; 
ALTER TABLE egw_abstractestimate ADD CONSTRAINT fk_abstractestimate_lineestimatedetails FOREIGN KEY (lineestimatedetails) REFERENCES egw_lineestimate_details (id);
CREATE INDEX idx_abstractestimate_lineestimatedetails ON egw_abstractestimate USING btree (lineestimatedetails);  

-------------------END-------------------
--rollback ALTER TABLE egw_abstractestimate DROP COLUMN lineestimatedetails;
--rollback ALTER TABLE egw_abstractestimate ALTER COLUMN fundsource_id SET NOT NULL;
--rollback ALTER TABLE egw_abstractestimate ALTER COLUMN user_department SET NOT NULL;
--rollback ALTER TABLE egw_abstractestimate ADD COLUMN document_number bigint; 
