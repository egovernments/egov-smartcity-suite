ALTER TABLE eg_demand_reason_master ALTER COLUMN isdemand SET NOT NULL;
--rollback ALTER TABLE eg_demand_reason_master ALTER COLUMN isdemand DROP NOT NULL;
