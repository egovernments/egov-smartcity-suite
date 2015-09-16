ALTER TABLE egadtax_agency DROP CONSTRAINT fk_adtax_agency_status;
ALTER TABLE egadtax_agency  DROP status;
ALTER TABLE egadtax_agency ADD column status character varying(50);
UPDATE egadtax_agency  SET status ='ACTIVE' ;
ALTER TABLE EGADTAX_AGENCY ALTER COLUMN status SET NOT NULL;
