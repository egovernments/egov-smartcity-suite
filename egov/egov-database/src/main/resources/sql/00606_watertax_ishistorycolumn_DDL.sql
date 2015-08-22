ALTER TABLE egwtr_connectiondetails ADD COLUMN ishistory boolean;

-- rollback ALTER TABLE egwtr_connectiondetails DROP COLUMN ishistory;
