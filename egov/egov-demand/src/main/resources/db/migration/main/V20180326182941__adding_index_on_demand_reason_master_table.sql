DROP INDEX eg_drm_code_idx;
CREATE INDEX idx_eg_drm_code ON eg_demand_reason_master (code);
