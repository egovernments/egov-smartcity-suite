drop index if exists idx_wfstate_status;
drop index if exists idx_wfstate_position;
create index idx_wfstate_status on eg_wf_states(status);
create index idx_wfstate_position on eg_wf_states(owner_pos);