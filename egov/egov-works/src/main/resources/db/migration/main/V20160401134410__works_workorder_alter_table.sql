
ALTER TABLE EGW_WORKORDER ALTER COLUMN defect_liability_period TYPE double precision;
ALTER TABLE EGW_WORKORDER ALTER COLUMN workorder_amount TYPE double precision;

--rollback ALTER TABLE EGW_WORKORDER ALTER COLUMN workorder_amount TYPE bigint;
--rollback ALTER TABLE EGW_WORKORDER ALTER COLUMN defect_liability_period TYPE bigint;

