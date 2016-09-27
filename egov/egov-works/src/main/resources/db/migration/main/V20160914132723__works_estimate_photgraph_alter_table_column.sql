ALTER TABLE egw_estimate_photographs  DROP COLUMN abstractestimate;
ALTER TABLE egw_estimate_photographs  DROP COLUMN image;
ALTER TABLE egw_estimate_photographs add column lineestimatedetails bigint;
ALTER TABLE egw_estimate_photographs ADD CONSTRAINT fk_lineestimatedetails_id FOREIGN KEY (lineestimatedetails) REFERENCES egw_lineestimate_details (id);
CREATE INDEX idx_lineestimatedetails_id ON egw_estimate_photographs USING btree (lineestimatedetails);
ALTER TABLE egw_estimate_photographs add column filestore bigint;
alter table egw_estimate_photographs  ALTER COLUMN latitude drop not null;
alter table egw_estimate_photographs  ALTER COLUMN longitude drop not null;
alter table egw_estimate_photographs  ALTER COLUMN dateofcapture drop not null;
ALTER TABLE egw_estimate_photographs ADD COLUMN workprogress character varying(50);

--rollback ALTER TABLE egw_estimate_photographs  DROP COLUMN lineestimatedetails;
--rollback ALTER TABLE egw_estimate_photographs add column abstractestimate bigint;
--rollback ALTER TABLE egw_estimate_photographs add column image bytea;
--rollback alter table egw_estimate_photographs  ALTER COLUMN latitude set not null;
--rollback alter table egw_estimate_photographs  ALTER COLUMN longitude set not null;
--rollback alter table egw_estimate_photographs  ALTER COLUMN dateofcapture set not null;
--rollback ALTER TABLE egw_estimate_photographs DROP COLUMN workprogress;
