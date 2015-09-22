ALTER TABLE egadtax_hoarding  DROP status;
ALTER TABLE egadtax_hoarding ADD column status bigint;
ALTER TABLE EGADTAX_hoarding ALTER COLUMN status SET NOT NULL;

ALTER TABLE egadtax_hoarding  DROP type;
ALTER TABLE egadtax_hoarding ADD column type bigint;
ALTER TABLE EGADTAX_hoarding ALTER COLUMN type SET NOT NULL;
