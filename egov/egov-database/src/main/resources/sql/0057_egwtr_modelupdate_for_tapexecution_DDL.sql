ALTER TABLE egwtr_connection ADD COLUMN initialreading BIGINT;
ALTER TABLE egwtr_connection RENAME COLUMN meternumber TO meterserialnumber;
ALTER TABLE egwtr_connectiondetails ADD COLUMN executiondate DATE;
