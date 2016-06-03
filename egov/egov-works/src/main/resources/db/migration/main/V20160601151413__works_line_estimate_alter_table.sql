ALTER TABLE egw_lineestimate_details DROP CONSTRAINT unq_lineestimate_details_estimatenumber;
ALTER TABLE egw_projectcode DROP CONSTRAINT unq_projectcode;
ALTER TABLE egw_abstractestimate DROP CONSTRAINT unq_estimate_number;

--rollback ALTER TABLE egw_lineestimate_details ADD CONSTRAINT unq_lineestimate_details_estimatenumber UNIQUE (estimatenumber);
--rollback ALTER TABLE egw_projectcode ADD CONSTRAINT unq_projectcode UNIQUE (code);
--rollback ALTER TABLE egw_abstractestimate ADD CONSTRAINT unq_estimate_number UNIQUE (estimatenumber);

