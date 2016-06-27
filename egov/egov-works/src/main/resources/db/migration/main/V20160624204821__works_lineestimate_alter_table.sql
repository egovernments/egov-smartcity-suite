ALTER TABLE EGW_LINEESTIMATE ADD COLUMN abstractEstimateCreated boolean DEFAULT 'false';

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN abstractEstimateCreated;