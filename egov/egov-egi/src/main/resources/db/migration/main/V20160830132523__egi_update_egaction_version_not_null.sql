--Updating version column as not null
UPDATE eg_action SET version=0 WHERE version IS NULL;

ALTER TABLE eg_action ALTER COLUMN version SET NOT NULL;
