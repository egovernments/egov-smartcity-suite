
CREATE INDEX idx_egdm_detail ON eg_demand_detail_variation (demand_detail);
CREATE INDEX idx_egdm_master ON eg_demand_detail_variation (demand_reason_master);
CREATE INDEX idx_egdm_detail_master ON eg_demand_detail_variation USING btree (demand_detail, demand_reason_master);

