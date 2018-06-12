DROP INDEX IF EXISTS idx_demand;
CREATE INDEX idx_demand on eg_demand_details(id_demand);
