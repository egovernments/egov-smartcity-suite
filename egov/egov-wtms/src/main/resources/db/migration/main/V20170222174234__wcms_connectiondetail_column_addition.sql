ALTER TABLE egwtr_connectiondetails ADD COLUMN buildingname character varying (1024);

--rollback ALTER TABLE egwtr_connectiondetails DROP COLUMN buildingname;