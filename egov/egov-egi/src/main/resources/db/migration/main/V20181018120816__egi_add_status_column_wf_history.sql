ALTER TABLE eg_wf_state_history ADD COLUMN status numeric(1);
UPDATE eg_wf_state_history SET status=1;
