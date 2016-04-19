ALTER TABLE egwtr_water_source ALTER COLUMN description DROP NOT NULL;

SELECT setval('SEQ_EGWTR_WATER_SOURCE', COALESCE((SELECT MAX(id)+1 FROM egwtr_water_source), 1), false);
