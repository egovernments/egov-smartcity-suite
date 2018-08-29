drop index if exists idx_property_state;
create index idx_property_state on egpt_property(state_id);
