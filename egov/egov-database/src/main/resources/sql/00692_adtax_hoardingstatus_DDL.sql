ALTER TABLE egadtax_hoarding DROP CONSTRAINT fk_adtax_hoarding_status;
ALTER TABLE egadtax_hoarding  DROP status;
ALTER TABLE egadtax_hoarding ADD column status character varying(50);
ALTER TABLE EGADTAX_AGENCY ALTER COLUMN status SET NOT NULL;

